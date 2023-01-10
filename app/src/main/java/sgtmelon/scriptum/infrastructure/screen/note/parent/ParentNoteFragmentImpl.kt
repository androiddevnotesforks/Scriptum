package sgtmelon.scriptum.infrastructure.screen.note.parent

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.databinding.ViewDataBinding
import java.util.Calendar
import sgtmelon.extensions.collect
import sgtmelon.extensions.toCalendar
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.iconanim.callback.IconChangeCallback
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.addOnNextAction
import sgtmelon.scriptum.cleanup.extension.bindBoolTint
import sgtmelon.scriptum.cleanup.extension.bindDrawable
import sgtmelon.scriptum.data.noteHistory.HistoryAction
import sgtmelon.scriptum.data.noteHistory.HistoryMoveAvailable
import sgtmelon.scriptum.databinding.IncNotePanelContentBinding
import sgtmelon.scriptum.databinding.IncToolbarNoteBinding
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.listener.HistoryTextWatcher
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity
import sgtmelon.scriptum.infrastructure.screen.note.NoteConnector
import sgtmelon.scriptum.infrastructure.screen.parent.BindingFragment
import sgtmelon.scriptum.infrastructure.utils.extensions.hideKeyboard
import sgtmelon.scriptum.infrastructure.utils.extensions.isFalse
import sgtmelon.scriptum.infrastructure.utils.extensions.isTrue
import sgtmelon.scriptum.infrastructure.utils.extensions.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveRank
import sgtmelon.scriptum.infrastructure.utils.extensions.note.isSaveEnabled
import sgtmelon.scriptum.infrastructure.utils.extensions.setOnTouchSelectionListener
import sgtmelon.scriptum.infrastructure.utils.icons.BackToCancelIcon
import sgtmelon.scriptum.infrastructure.utils.tint.TintNoteToolbar
import sgtmelon.test.idling.getIdling

/**
 * Parent class for fragments which will be displayed in [NoteActivity].
 */
abstract class ParentNoteFragmentImpl<N : NoteItem, T : ViewDataBinding> : BindingFragment<T>(),
    ParentNoteFragment<N>,
    IconBlockCallback {

    // TODO update name in connector init

    protected val connector get() = activity as NoteConnector

    abstract val type: NoteType

    abstract val viewModel: ParentNoteViewModel<N>

    abstract val appBar: IncToolbarNoteBinding?
    abstract val panelBar: IncNotePanelContentBinding?

    private var tintToolbar: TintNoteToolbar? = null
    private var navigationIcon: IconChangeCallback? = null

    private val dialogs by lazy { DialogFactory.Note(context, fm) }

    private val rankDialog by lazy { dialogs.getRank() }
    private val colorDialog by lazy { dialogs.getColor() }
    private val dateDialog by lazy { dialogs.getDate() }
    private val timeDialog by lazy { dialogs.getTime() }
    private val convertDialog by lazy { dialogs.getConvert(type) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(view.context, appBar?.content?.toolbar)
        setupPanel()
        setupContent()
    }

    override fun setupDialogs() {
        super.setupDialogs()

        rankDialog.apply {
            onPositiveClick { viewModel.changeRank(check = rankDialog.check - 1) }
            onDismiss { open.clear() }
        }

        colorDialog.apply {
            onPositiveClick { viewModel.changeColor(colorDialog.check) }
            onDismiss { open.clear() }
        }

        dateDialog.apply {
            onPositiveClick {
                open.skipClear = true
                viewModel.notificationsDateList.collect(owner = this) {
                    showTimeDialog(dateDialog.calendar, it)
                }
            }
            onNeutralClick {
                viewModel.removeNotification().collect(owner = this) {
                    system.broadcast.sendCancelAlarm(it)
                    system.broadcast.sendNotifyInfoBind()
                }
            }
            onDismiss { open.clear() }
        }

        timeDialog.apply {
            onPositiveClick {
                val calendar = timeDialog.calendar
                viewModel.setNotification(calendar).collect(owner = this) {
                    system.broadcast.sendSetAlarm(it, calendar)
                    system.broadcast.sendNotifyInfoBind()
                }
            }
            onDismiss { open.clear() }
        }

        convertDialog.apply {
            onPositiveClick {
                viewModel.convert().collect(owner = this) { connector.convertNote() }
            }
            onDismiss { open.clear() }
        }
    }

    //region setupObservers staff

    override fun setupObservers() {
        super.setupObservers()

        viewModel.isDataReady.observe(this) { observeDataReady(it) }
        viewModel.isEdit.observe(this) { observeEdit(it) }
        viewModel.noteState.observe(this) { observeState(it) }
        viewModel.id.observe(this) { connector.init.id = it }
        viewModel.color.observe(this) { observeColor(it) }
        viewModel.rankDialogItems.observe(this) { rankDialog.itemArray = it }
        viewModel.noteItem.observe(this) { observeNoteItem(it) }
        viewModel.historyAvailable.observe(this) { observeHistoryAvailable(it) }
    }

    // TODO("block some buttons in panel bar while data not loaded")
    // TODO("change enable of button, fields and etc")
    @CallSuper open fun observeDataReady(it: Boolean) {
        invalidateToolbar()
    }

    @CallSuper open fun observeEdit(it: Boolean) {
        connector.init.isEdit = it

        invalidateToolbar()
        invalidatePanelState(it)
    }

    @CallSuper open fun observeState(it: NoteState) {
        connector.init.state = it
    }

    @CallSuper open fun observeColor(it: Color) {
        connector.init.color = it
        connector.updateHolder(it)
    }

    @CallSuper open fun observeNoteItem(it: N) {
        invalidatePanelData(it)
    }

    @CallSuper open fun observeHistoryAvailable(available: HistoryMoveAvailable) {
        panelBar?.undoButton?.apply {
            isEnabled = available.undo
            bindBoolTint(available.undo, R.attr.clContent, R.attr.clDisable)
        }
        panelBar?.redoButton?.apply {
            isEnabled = available.redo
            bindBoolTint(available.redo, R.attr.clContent, R.attr.clDisable)
        }
    }

    //endregion

    @CallSuper open fun setupToolbar(context: Context, toolbar: Toolbar?) {
        val color = connector.init.color

        val colorIndicator = appBar?.indicator?.colorView
        tintToolbar = TintNoteToolbar(context, activity?.window, toolbar, colorIndicator, color)
        navigationIcon = BackToCancelIcon(context, toolbar, callback = this)

        /**
         * Don't need block back button while data not loaded. Because, if data exists (and
         * loading) - it means not edit mode and we can't somehow harm any data. Button close
         * the screen in this case.
         */
        toolbar?.setNavigationOnClickListener { viewModel.onClickBackArrow() }

        /** Show cancel button (for undo all changes) only if note exists and in edit mode. */
        val isCancel = with(connector.init) { state != NoteState.CREATE && isEdit }
        setToolbarBackIcon(isCancel, needAnim = false)

        appBar?.content?.scrollView?.setOnTouchSelectionListener(appBar?.content?.nameEnter)
        appBar?.content?.nameEnter?.let {
            /** Save changes of name to noteItem model (available only in edit mode). */
            it.doOnTextChanged { text, _, _, _ ->
                viewModel.noteItem.value?.name = text?.toString() ?: return@doOnTextChanged
            }

            it.addTextChangedListener(HistoryTextWatcher(it, viewModel) { value, cursor ->
                HistoryAction.Name(value, cursor)
            })

            it.addOnNextAction { focusAfterNameAction() }
        }
    }

    override fun setIconEnabled(isEnabled: Boolean) {
        getIdling().change(!isEnabled, IdlingTag.Anim.ICON)
        open.isBlocked = !isEnabled
    }

    abstract fun focusAfterNameAction()

    @CallSuper open fun setupPanel() {
        val panelBar = panelBar ?: return

        panelBar.restoreButton.setOnClickListener {
            viewModel.restore().collect(owner = this) { activity?.finish() }
        }
        panelBar.restoreOpenButton.setOnClickListener { viewModel.restoreOpen() }
        panelBar.clearButton.setOnClickListener {
            viewModel.deleteForever().collect(owner = this) { activity?.finish() }
        }
        panelBar.undoButton.setOnClickListener { viewModel.undoAction() }
        panelBar.redoButton.setOnClickListener { viewModel.redoAction() }
        panelBar.rankButton.setOnClickListener { showRankDialog() }
        panelBar.colorButton.setOnClickListener { showColorDialog() }
        panelBar.saveButton.setOnClickListener { viewModel.save(changeMode = true) }
        panelBar.saveButton.setOnLongClickListener { viewModel.save(changeMode = false) }
        panelBar.notificationButton.setOnClickListener { showDateDialog() }
        panelBar.bindButton.setOnClickListener {
            if (isEditMode) return@setOnClickListener

            viewModel.switchBind().collect(owner = this) {
                system.broadcast.sendNotifyNotesBind()
            }
        }
        panelBar.convertButton.setOnClickListener { showConvertDialog() }
        panelBar.deleteButton.setOnClickListener {
            if (open.isBlocked || isEditMode) return@setOnClickListener

            viewModel.delete().collect(owner = this) {
                system.broadcast.sendCancelAlarm(it)
                system.broadcast.sendCancelNoteBind(it)
                system.broadcast.sendNotifyInfoBind()
                activity?.finish()
            }
        }
        panelBar.editButton.setOnClickListener { viewModel.edit() }

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

    @CallSuper open fun invalidateToolbar() {
        val isDataReady = viewModel.isDataReady.value ?: return
        val isEdit = connector.init.isEdit

        appBar?.content?.run {
            /**
             * Don't need check ready data or not. Because:
             * [NoteState.CREATE] - don't have any pre-binding -> show just enter;
             * [NoteState.EXIST] - have pre-binding name -> show read and hide enter;
             * [NoteState.DELETE] - same situation as with [NoteState.EXIST].
             */
            nameEnter.makeVisibleIf(isEdit) { makeInvisible() }
            nameRead.makeVisibleIf(!isEdit) { makeInvisible() }

            if (isDataReady) {
                /** Always will notNull - because isDataReady==true. */
                val item = viewModel.noteItem.value ?: return

                nameEnter.setText(item.name)
                /**
                 * Set empty text needed for nameEnter has ability to change size
                 * inside scrollView.
                 */
                nameRead.text = if (isEdit) "" else item.name
            } else {
                /**
                 * Name in init only may exists if note is already in [NoteState.EXIST] state,
                 * whats why don't need set text for nameEnter.
                 */
                nameRead.text = connector.init.name
            }
        }
    }

    @CallSuper open fun invalidatePanelState(isEdit: Boolean) {
        val panelBar = panelBar ?: return

        if (connector.init.state == NoteState.DELETE) {
            panelBar.binContainer.makeVisible()
            panelBar.editContainer.makeInvisible()
            panelBar.readContainer.makeInvisible()
        } else {
            panelBar.binContainer.makeInvisible()
            panelBar.editContainer.makeVisibleIf(isEdit) { makeInvisible() }
            panelBar.readContainer.makeVisibleIf(!isEdit) { makeInvisible() }
        }
    }

    @CallSuper open fun invalidatePanelData(item: N) {
        val panelBar = panelBar ?: return

        /** rankDialogItems always will be notNull, because it's loaded before [item]. */
        val rankItems = viewModel.rankDialogItems.value
        if (rankItems != null) {
            val isRankEmpty = rankItems.size == 1

            val trueColor = if (item.haveRank) R.attr.clAccent else R.attr.clContent
            panelBar.rankButton.bindBoolTint(!isRankEmpty, trueColor, R.attr.clDisable)
            panelBar.rankButton.isEnabled = !isRankEmpty
        }

        panelBar.saveButton.isEnabled = item.isSaveEnabled
        panelBar.notificationButton.bindBoolTint(item.haveAlarm, R.attr.clAccent, R.attr.clContent)

        panelBar.bindButton.bindBoolTint(item.isStatus, R.attr.clAccent, R.attr.clContent)

        val bindDescription = if (item.isStatus) {
            R.string.description_note_unbind
        } else {
            R.string.description_note_bind
        }
        panelBar.bindButton.contentDescription = getString(bindDescription)
    }

    //region Cleanup

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override val isDialogOpen: Boolean get() = open.isBlocked

    override fun hideKeyboard() {
        activity?.hideKeyboard()
    }

    override fun onPressBack() = viewModel.onPressBack()

    override fun tintToolbar(from: Color, to: Color) {
        tintToolbar?.setColorFrom(from)?.startTint(to)
    }

    override fun tintToolbar(color: Color) {
        tintToolbar?.startTint(color)
    }

    override fun setToolbarBackIcon(isCancel: Boolean, needAnim: Boolean) {
        navigationIcon?.setDrawable(isCancel, needAnim)
    }

    override fun showSaveToast(isSuccess: Boolean) {
        val text = if (isSuccess) R.string.toast_note_save_done else R.string.toast_note_save_error
        system.toast.show(context, text)
    }

    override fun finish() {
        activity?.finish()
    }

    override fun sendNotifyNotesBroadcast() = system.broadcast.sendNotifyNotesBind()

    //endregion

    //region Dialogs

    private fun showRankDialog() {
        if (isReadMode) return

        /** +1 because in rank dialog all items has shift by one (due to "no category" item). */
        val check = (viewModel.noteItem.value?.rank?.position ?: return) + 1

        hideKeyboard()
        open.attempt {
            rankDialog.setArguments(check).safeShow(DialogFactory.Note.RANK, owner = this)
        }
    }

    private fun showColorDialog() {
        if (isReadMode) return

        val color = viewModel.noteItem.value?.color ?: return

        hideKeyboard()
        open.attempt {
            tintToolbar?.setColorFrom(color)
            colorDialog.setArguments(color).safeShow(DialogFactory.Note.COLOR, owner = this)
        }
    }

    private fun showDateDialog() {
        if (isEditMode) return

        val item = viewModel.noteItem.value ?: return
        val calendar = item.alarm.date.toCalendar()

        hideKeyboard()
        open.attempt {
            open.tag = OpenState.Tag.DIALOG
            dateDialog.setArguments(calendar, item.haveAlarm)
                .safeShow(DialogFactory.Note.DATE, owner = this)
        }
    }

    private fun showTimeDialog(calendar: Calendar, dateList: List<String>) {
        if (isEditMode) return

        hideKeyboard()
        open.attempt(OpenState.Tag.DIALOG) {
            timeDialog.setArguments(calendar, dateList)
                .safeShow(DialogFactory.Note.TIME, owner = this)
        }
    }

    private fun showConvertDialog() {
        if (isEditMode) return

        hideKeyboard()
        open.attempt {
            convertDialog.safeShow(DialogFactory.Note.CONVERT, owner = this)
        }
    }

    //endregion

    private val isEditMode get() = viewModel.isEdit.value.isTrue()
    private val isReadMode get() = viewModel.isEdit.value.isFalse()

}