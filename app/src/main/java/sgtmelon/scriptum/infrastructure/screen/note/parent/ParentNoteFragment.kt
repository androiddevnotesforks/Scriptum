package sgtmelon.scriptum.infrastructure.screen.note.parent

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import sgtmelon.extensions.collect
import sgtmelon.extensions.emptyString
import sgtmelon.extensions.toCalendar
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.iconanim.callback.IconChangeCallback
import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.safedialog.dialog.time.DateDialog
import sgtmelon.safedialog.dialog.time.TimeDialog
import sgtmelon.safedialog.utils.DialogOwner
import sgtmelon.safedialog.utils.DialogStorage
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.bindBoolTint
import sgtmelon.scriptum.cleanup.extension.bindDrawable
import sgtmelon.scriptum.cleanup.extension.bindTextColor
import sgtmelon.scriptum.data.noteHistory.model.HistoryAction
import sgtmelon.scriptum.data.noteHistory.model.HistoryMoveAvailable
import sgtmelon.scriptum.databinding.IncNotePanelBinding
import sgtmelon.scriptum.databinding.IncToolbarNoteBinding
import sgtmelon.scriptum.domain.model.result.HistoryResult
import sgtmelon.scriptum.infrastructure.dialogs.ColorDialog
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.listener.HistoryTextWatcher
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Filter
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity
import sgtmelon.scriptum.infrastructure.screen.note.NoteConnector
import sgtmelon.scriptum.infrastructure.screen.note.history.HistoryTicker
import sgtmelon.scriptum.infrastructure.screen.note.save.NoteSave
import sgtmelon.scriptum.infrastructure.screen.note.save.NoteSaveImpl
import sgtmelon.scriptum.infrastructure.screen.parent.BindingFragment
import sgtmelon.scriptum.infrastructure.utils.extensions.hideKeyboard
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.doOnApplyWindowInsets
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.updatePadding
import sgtmelon.scriptum.infrastructure.utils.extensions.isFalse
import sgtmelon.scriptum.infrastructure.utils.extensions.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveRank
import sgtmelon.scriptum.infrastructure.utils.extensions.note.isSaveEnabled
import sgtmelon.scriptum.infrastructure.utils.extensions.requestFocusWithCursor
import sgtmelon.scriptum.infrastructure.utils.extensions.setEditorNextAction
import sgtmelon.scriptum.infrastructure.utils.extensions.setOnTouchSelectionListener
import sgtmelon.scriptum.infrastructure.utils.extensions.setTextIfDifferent
import sgtmelon.scriptum.infrastructure.utils.extensions.setTextSelectionSafe
import sgtmelon.scriptum.infrastructure.utils.icons.BackToCancelIcon
import sgtmelon.scriptum.infrastructure.utils.tint.TintNoteToolbar
import sgtmelon.test.idling.getIdling
import java.util.Calendar

/**
 * Parent class for fragments which will be displayed in [NoteActivity].
 */
abstract class ParentNoteFragment<N : NoteItem, T : ViewDataBinding> : BindingFragment<T>(),
    IconBlockCallback {

    protected val connector get() = activity as NoteConnector

    abstract val type: NoteType

    abstract val viewModel: ParentNoteViewModel<N>
    abstract val noteSave: NoteSave

    abstract val appBar: IncToolbarNoteBinding?
    abstract val panelBar: IncNotePanelBinding?

    private val animation by lazy {
        with(viewModel) { ParentNoteAnimation(noteState.value, isEdit.value) }
    }

    private var tintToolbar: TintNoteToolbar? = null
    private var navigationIcon: IconChangeCallback? = null

    private val dialogs by lazy { DialogFactory.Note(resources) }
    private val dialogOwner: DialogOwner get() = this
    private val convertDialog = DialogStorage(
        DialogFactory.Note.CONVERT, dialogOwner,
        create = { dialogs.getConvert(type) },
        setup = { setupConvertDialog(it) }
    )
    private val rankDialog = DialogStorage(
        DialogFactory.Note.RANK, dialogOwner,
        create = { dialogs.getRank() },
        setup = { setupRankDialog(it) }
    )
    private val colorDialog = DialogStorage(
        DialogFactory.Note.COLOR, dialogOwner,
        create = { dialogs.getColor() },
        setup = { setupColorDialog(it) }
    )
    private val dateDialog = DialogStorage(
        DialogFactory.Note.DATE, dialogOwner,
        create = { dialogs.getDate() },
        setup = { setupDateDialog(it) }
    )
    private val timeDialog = DialogStorage(
        DialogFactory.Note.TIME, dialogOwner,
        create = { dialogs.getTime() },
        setup = { setupTimeDialog(it) }
    )

    private val historyTicker = HistoryTicker(lifecycleScope) { system }

    //region Setup functions

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.fetchData()
    }

    override fun setupInsets() {
        super.setupInsets()

        panelBar?.panelContainer?.doOnApplyWindowInsets { view, insets, isFirstTime, padding, _ ->
            view.updatePadding(InsetsDir.BOTTOM, insets, padding, !isFirstTime)
            return@doOnApplyWindowInsets insets
        }
    }

    override fun setupView() {
        super.setupView()

        /**
         * This functions must be called before [setupObservers], to init all needed variables,
         * like a [navigationIcon].
         */
        setupToolbar(requireContext(), appBar?.content?.toolbar)
        setupPanel()
        setupContent()
    }

    @CallSuper open fun setupToolbar(context: Context, toolbar: Toolbar?) {
        val color = viewModel.color.value ?: return

        val colorIndicator = appBar?.indicator?.colorView
        tintToolbar = TintNoteToolbar(context, activity?.window, toolbar, colorIndicator, color)
        navigationIcon = BackToCancelIcon(context, toolbar, callback = this)

        /**
         * Don't need block back button while data not loaded. Because, if data exists (and
         * loading) - it means not edit mode and we can't somehow harm any data. Button close
         * the screen in this case.
         */
        toolbar?.setNavigationOnClickListener(toolbarBackListener)

        /** Show cancel button (for undo all changes) only if note exists and in edit mode. */
        val isCancel = with(viewModel) { noteState.value != NoteState.CREATE && isEditMode }
        navigationIcon?.setDrawable(isCancel, needAnim = false)

        appBar?.content?.scrollView?.setOnTouchSelectionListener(appBar?.content?.nameEnter)
        appBar?.content?.nameEnter?.let {
            /** Save changes of name to noteItem model (available only in edit mode). */
            it.doOnTextChanged { text, _, _, _ ->
                viewModel.changeName(value = text?.toString() ?: return@doOnTextChanged)
            }

            it.addTextChangedListener(HistoryTextWatcher(it, viewModel) { value, cursor ->
                HistoryAction.Name(value, cursor)
            })

            it.setEditorNextAction { focusOnEnter() }
        }
    }

    override fun setIconEnabled(isEnabled: Boolean) {
        getIdling().change(!isEnabled, IdlingTag.Common.ICON_ANIM)
        open.isBlocked = !isEnabled
    }

    abstract fun focusOnEnter()

    @CallSuper open fun setupPanel() {
        val panelBar = panelBar ?: return

        panelBar.restoreButton.setOnClickListener {
            viewModel.restore().collect(owner = this) {
                /** We can surely say NOTES page will display a list. */
                system?.broadcast?.sendInfoChangeUi(ShowListState.List, Filter.NOTES)
                activity?.finish()
            }
        }
        panelBar.restoreOpenButton.setOnClickListener {
            /** We can surely say NOTES page will display a list. */
            system?.broadcast?.sendInfoChangeUi(ShowListState.List, Filter.NOTES)
            viewModel.restoreOpen()
        }
        panelBar.clearButton.setOnClickListener {
            viewModel.deleteForever().collect(owner = this) { activity?.finish() }
        }

        val collectUndo = { viewModel.undoAction().collect(owner = this) { collectUndoRedo(it) } }
        panelBar.undoButton.setOnClickListener { open.ifNotBlocked { collectUndo() } }
        panelBar.undoButton.setOnLongClickListener {
            open.attempt {
                historyTicker.start(it, collectUndo) { open.isBlocked = false }
            }
            return@setOnLongClickListener true
        }

        val collectRedo = { viewModel.redoAction().collect(owner = this) { collectUndoRedo(it) } }
        panelBar.redoButton.setOnClickListener { open.ifNotBlocked { collectRedo() } }
        panelBar.redoButton.setOnLongClickListener {
            open.attempt {
                historyTicker.start(it, collectRedo) { open.isBlocked = false }
            }
            return@setOnLongClickListener true
        }

        panelBar.rankButton.setOnClickListener { showRankDialog() }
        panelBar.colorButton.setOnClickListener { showColorDialog() }
        panelBar.saveButton.setOnClickListener {
            open.ifNotBlocked { viewModel.save(changeMode = true) }
        }
        panelBar.saveButton.setOnLongClickListener {
            saveAndContinueEdit()
            return@setOnLongClickListener true
        }
        panelBar.notificationButton.setOnClickListener { showDateDialog() }
        panelBar.bindButton.setOnClickListener { viewModel.switchBind() }
        panelBar.convertButton.setOnClickListener { showConvertDialog() }
        panelBar.deleteButton.setOnClickListener {
            open.ifNotBlocked {
                viewModel.delete().collect(owner = this) {
                    /** We can surely say BIN page will display a list. */
                    system?.broadcast?.sendInfoChangeUi(ShowListState.List, Filter.BIN)
                    /** We can surely say NOTIFICATIONS screen state changed. */
                    system?.broadcast?.sendInfoChangeUi(it.id, Filter.NOTIFICATION)

                    system?.broadcast?.sendCancelAlarm(it)
                    system?.broadcast?.sendCancelNoteBind(it)
                    system?.broadcast?.sendNotifyInfoBind()
                    activity?.finish()
                }
            }
        }
        panelBar.editButton.setOnClickListener { open.ifNotBlocked { viewModel.edit() } }

        val bindDrawable = when (type) {
            NoteType.TEXT -> R.drawable.ic_bind_text
            NoteType.ROLL -> R.drawable.ic_bind_roll
        }
        panelBar.bindButton.bindDrawable(bindDrawable, R.attr.clContent)

        val convertDescription = when (type) {
            NoteType.TEXT -> R.string.description_note_convert_text
            NoteType.ROLL -> R.string.description_note_convert_roll
        }
        panelBar.convertButton.contentDescription = getString(convertDescription)
    }

    @CallSuper open fun setupContent() = Unit

    override fun setupDialogs() {
        super.setupDialogs()

        convertDialog.restore()
        rankDialog.restore()
        colorDialog.restore()
        dateDialog.restore()
        timeDialog.restore()
    }

    private fun setupConvertDialog(dialog: MessageDialog): Unit = with(dialog) {
        onPositiveClick {
            viewModel.convert().collect(owner = this@ParentNoteFragment) {
                connector.convertNote(it)
            }
        }
        onDismiss {
            convertDialog.release()
            open.clear()
        }
    }

    private fun setupRankDialog(dialog: SingleDialog): Unit = with(dialog) {
        itemArray = viewModel.rankDialogItems.value ?: emptyArray()
        onPositiveClick { viewModel.changeRank(check = check - 1) }
        onDismiss {
            rankDialog.release()
            open.clear()
        }
    }

    private fun setupColorDialog(dialog: ColorDialog): Unit = with(dialog) {
        onPositiveClick { viewModel.changeColor(check) }
        onDismiss {
            colorDialog.release()
            open.clear()
        }
    }

    private fun setupDateDialog(dialog: DateDialog): Unit = with(dialog) {
        onPositiveClick {
            open.skipClear = true
            viewModel.notificationsDateList.collect(owner = this@ParentNoteFragment) {
                showTimeDialog(calendar, it)
            }
        }
        onNeutralClick {
            viewModel.removeNotification().collect(owner = this@ParentNoteFragment) {
                /** We can surely say NOTIFICATIONS screen state changed. */
                system?.broadcast?.sendInfoChangeUi(it.id, Filter.NOTIFICATION)

                system?.broadcast?.sendCancelAlarm(it)
                system?.broadcast?.sendNotifyInfoBind()
            }
        }
        onDismiss {
            dateDialog.release()
            open.clear()
        }
    }

    private fun setupTimeDialog(dialog: TimeDialog): Unit = with(dialog) {
        onPositiveClick {
            val calendar = calendar
            viewModel.setNotification(calendar).collect(owner = this) {
                system?.broadcast?.sendSetAlarm(it, calendar)
                system?.broadcast?.sendNotifyInfoBind()
            }
        }
        onDismiss {
            timeDialog.release()
            open.clear()
        }
    }

    //endregion

    /** Use this function to collect undo/redo [result] and change something directly in UI. */
    abstract fun collectUndoRedo(result: HistoryResult)

    protected fun onHistoryName(result: HistoryResult.Name) = viewModel.disableHistoryChanges {
        appBar?.content?.nameEnter?.setTextSelectionSafe(result.value, result.cursor)
    }

    //region Observable staff

    // TODO проверить, чтобы не было задвоений вызовов (а они наверное есть, см invalidatePanelState)
    override fun setupObservers() {
        super.setupObservers()

        observeWithHistoryDisable(viewModel.noteItem) { observeNoteItem(it) }
        observeWithHistoryDisable(viewModel.noteState) { observeState(connector.init.state, it) }
        observeWithHistoryDisable(viewModel.isEdit) { observeEdit(connector.init.isEdit, it) }
        observeWithHistoryDisable(viewModel.color) { observeColor(it) }
        observeWithHistoryDisable(viewModel.rankDialogItems) { invalidateRankButton() }
        observeWithHistoryDisable(viewModel.historyAvailable) { observeHistoryAvailable(it) }
    }

    /**
     * Watch observable [data] with disabling history-changes tracker. It's needed for skip
     * e.g. TextWatchers calls on any data setup.
     */
    private inline fun <T> Fragment.observeWithHistoryDisable(
        data: LiveData<T>,
        crossinline onCall: (T) -> Unit
    ) {
        data.observe(this) { viewModel.disableHistoryChanges { onCall(it) } }
    }

    @CallSuper open fun observeNoteItem(item: N) {
        connector.init.noteItem = item
        invalidatePanelData(item)

        if (viewModel.noteState.value == NoteState.EXIST) {
            /**
             * Call it only in read mode - bad choice, because sometimes need invalidate bind
             * notes during edit mode (e.g. click unbind in status bar during edit mode).
             */
            system?.broadcast?.sendNotifyNotesBind()
        }
    }

    @CallSuper open fun observeState(previousState: NoteState, state: NoteState) {
        connector.init.state = state

        val isEdit = viewModel.isEditMode

        invalidatePanelState(isEdit)

        /**
         * Detect note was created and saved.
         *
         * Need check [previousState], because screen may be rotated and in this case all
         * observe staff will be called (it comes to animation false call). Need to determinate
         * case when [state] really was changed.
         */
        if (previousState == NoteState.CREATE && state == NoteState.EXIST) {
            /** We can surely say NOTES page will display a list. */
            system?.broadcast?.sendInfoChangeUi(ShowListState.List, Filter.NOTES)

            /**
             * If [NoteState.EXIST] and in isEdit mode - that means note was created and saved
             * without changing edit mode. This may happens if auto save is on.
             *
             * And that's why need change icon from ARROW to CANCEL.
             */
            if (isEdit) {
                navigationIcon?.setDrawable(isEnterIcon = true, needAnim = true)
            }
        }
    }

    @CallSuper open fun observeEdit(previousEdit: Boolean, isEdit: Boolean) {
        connector.init.isEdit = isEdit
        noteSave.changeAutoSaveWork(isEdit)

        if (isEdit) {
            /** If note was just created and data not filled. */
            if (viewModel.noteState.value == NoteState.CREATE && isContentEmpty()) {
                /** Focus on name enter if it's empty, otherwise (if filled) [focusOnEnter]. */
                appBar?.content?.nameEnter?.let {
                    if (it.text.isEmpty()) it.requestFocusWithCursor(binding) else focusOnEnter()
                }
            } else {
                focusOnEnter()
            }
        } else {
            hideKeyboard()
        }

        invalidateToolbar()
        invalidatePanelState(isEdit)

        /**
         * If [isEdit] not equals [previousEdit] - that means edit mode was changed.
         *
         * Need check [previousEdit], because screen may be rotated and in this case all
         * observe staff will be called (it comes to animation false call). Need to determinate
         * case when [isEdit] really was changed.
         */
        if (previousEdit != isEdit) {
            navigationIcon?.setDrawable(isEdit, needAnim = true)
        }
    }

    @CallSuper open fun observeColor(color: Color) {
        connector.updateHolder(color)
        tintToolbar?.startTint(color)
    }

    private fun observeHistoryAvailable(available: HistoryMoveAvailable) {
        panelBar?.undoButton?.apply {
            isEnabled = available.undo
            bindBoolTint(available.undo, R.attr.clContent, R.attr.clDisable)
        }
        panelBar?.redoButton?.apply {
            isEnabled = available.redo
            bindBoolTint(available.redo, R.attr.clContent, R.attr.clDisable)
        }
    }

    private fun invalidateToolbar() {
        val item = viewModel.noteItem.value ?: return
        val isEdit = viewModel.isEditMode

        appBar?.content?.run {
            /**
             * Don't need check ready data or not. Because:
             * [NoteState.CREATE] - don't have any pre-binding -> show just enter;
             * [NoteState.EXIST] - have pre-binding name -> show read and hide enter;
             * [NoteState.DELETE] - same situation as with [NoteState.EXIST].
             */
            nameEnter.makeVisibleIf(isEdit) { makeInvisible() }
            nameRead.makeVisibleIf(!isEdit) { makeInvisible() }

            nameEnter.setTextIfDifferent(item.name)
            /** Set empty text needed for EditTExt has ability to change size inside scrollView. */
            nameRead.text = if (isEdit) emptyString() else item.name
        }
    }

    @CallSuper open fun invalidatePanelState(isEdit: Boolean) {
        val state = viewModel.noteState.value ?: return
        animation.startPanelFade(panelBar, isEdit, state)
    }

    private fun invalidatePanelData(item: N) {
        val panelBar = panelBar ?: return

        invalidateRankButton()
        invalidateSaveButton(item)
        panelBar.notificationButton.bindBoolTint(item.haveAlarm, R.attr.clAccent, R.attr.clContent)

        panelBar.bindButton.bindBoolTint(item.isStatus, R.attr.clAccent, R.attr.clContent)

        val bindDescription = if (item.isStatus) {
            R.string.description_note_unbind
        } else {
            R.string.description_note_bind
        }
        panelBar.bindButton.contentDescription = getString(bindDescription)
    }

    private fun invalidateRankButton() {
        val item = viewModel.noteItem.value ?: return
        val rankItems = viewModel.rankDialogItems.value
        val isRankEmpty = rankItems == null || rankItems.size == 1

        val trueColor = if (item.haveRank) R.attr.clAccent else R.attr.clContent
        panelBar?.rankButton?.bindBoolTint(!isRankEmpty, trueColor, R.attr.clDisable)
        panelBar?.rankButton?.isEnabled = !isRankEmpty
    }

    protected fun invalidateSaveButton(item: N) {
        val isEnabled = item.isSaveEnabled

        panelBar?.saveButton?.apply {
            this.isEnabled = isEnabled
            bindTextColor(isEnabled, R.attr.clContent, R.attr.clDisable)
        }
    }

    //endregion

    /** Function for detect created notes with not filled content. */
    abstract fun isContentEmpty(): Boolean

    protected val noteSaveCallback = object : NoteSaveImpl.Callback {

        override fun onAutoSave() = saveAndContinueEdit()

        override val isAutoSaveAvailable: Boolean get() = viewModel.isEditMode

        override val isPauseSaveAvailable: Boolean
            get() = viewModel.isEditMode && connector.isOrientationChanging().isFalse()
    }

    private val toolbarBackListener = View.OnClickListener {
        if (open.isBlocked) return@OnClickListener

        val isDataRestored = viewModel.restoreDataOrExit()

        if (!isDataRestored) {
            noteSave.skipPauseSave()
            activity?.finish()
        }
    }

    /**
     * FALSE - will call super.onBackPress() in parent activity.
     * TRUE  - will do nothing.
     */
    fun onPressBack(): Boolean {
        /** If user click fastly 2 times back (and icon is animating). */
        if (open.isBlocked) return true

        val isScreenOpen = viewModel.saveOrRestoreData()

        if (!isScreenOpen) {
            /** If note can't be saved and activity will be closed (because return FALSE). */
            noteSave.skipPauseSave()
        }

        return isScreenOpen
    }

    /** In this case we don't change edit mode, just notice user about saving. */
    private fun saveAndContinueEdit() {
        val text = if (viewModel.save(changeMode = false)) {
            R.string.toast_note_save_done
        } else {
            R.string.toast_note_save_error
        }

        system?.toast?.show(context, text)
    }

    //region Dialogs

    private fun showRankDialog() {
        if (viewModel.isReadMode) return

        /** +1 because in rank dialog all items has shift by one (due to "no category" item). */
        val check = (viewModel.noteItem.value?.rank?.position ?: return) + 1

        hideKeyboard()
        open.attempt {
            rankDialog.show { setArguments(check) }
        }
    }

    private fun showColorDialog() {
        if (viewModel.isReadMode) return

        val color = viewModel.noteItem.value?.color ?: return

        hideKeyboard()
        open.attempt {
            colorDialog.show { setArguments(color) }
        }
    }

    private fun showDateDialog() {
        if (viewModel.isEditMode) return

        val item = viewModel.noteItem.value ?: return
        val calendar = item.alarm.date.toCalendar()

        hideKeyboard()
        open.attempt {
            open.tag = OpenState.Tag.DIALOG
            dateDialog.show { setArguments(calendar, item.haveAlarm) }
        }
    }

    private fun showTimeDialog(calendar: Calendar, dateList: List<String>) {
        if (viewModel.isEditMode) return

        hideKeyboard()
        open.attempt(OpenState.Tag.DIALOG) {
            timeDialog.show { setArguments(calendar, dateList) }
        }
    }

    private fun showConvertDialog() {
        if (viewModel.isEditMode) return

        hideKeyboard()
        open.attempt {
            convertDialog.show()
        }
    }

    //endregion

}