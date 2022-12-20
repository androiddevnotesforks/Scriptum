package sgtmelon.scriptum.infrastructure.screen.note.text

import android.os.Bundle
import android.view.View
import android.widget.EditText
import java.util.Calendar
import javax.inject.Inject
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.iconanim.callback.IconChangeCallback
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.annotation.InputAction
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.addOnNextAction
import sgtmelon.scriptum.cleanup.extension.requestFocusOnVisible
import sgtmelon.scriptum.cleanup.extension.requestSelectionFocus
import sgtmelon.scriptum.cleanup.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.cleanup.presentation.control.note.input.InputControl
import sgtmelon.scriptum.cleanup.presentation.control.note.input.watcher.InputTextWatcher
import sgtmelon.scriptum.databinding.FragmentTextNoteBinding
import sgtmelon.scriptum.databinding.IncToolbarNoteBinding
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity
import sgtmelon.scriptum.infrastructure.screen.note.NoteMenu
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteFragmentImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.hideKeyboard
import sgtmelon.scriptum.infrastructure.utils.tint.TintNoteToolbar

/**
 * Fragment for display text note.
 */
class TextNoteFragmentImpl : ParentNoteFragmentImpl<NoteItem.Text, FragmentTextNoteBinding>(),
    TextNoteFragment,
    IconBlockCallback {

    override val layoutId: Int = R.layout.fragment_text_note

    @Inject override lateinit var viewModel: TextNoteViewModel

    override val appBar: IncToolbarNoteBinding? get() = binding?.appBar

    // TODO PLAN:
    // TODO 1. Change isEdit/noteState via new livedata value (if first time - skip animation - no views visible)
    //         - Move all binding related with it into UI classes
    // TODO 2. Make common use case for undo/redo (use flow?)
    // TODO 3. Move common functions into use cases? (don't use parent vm class?)


    override fun setupBinding(callback: NoteMenu) {
        binding?.menuCallback = callback
    }

    //region Cleanup

    private var tintToolbar: TintNoteToolbar? = null
    private var navigationIcon: IconChangeCallback? = null

    private val dialogs by lazy { DialogFactory.Note(context, fm) }

    private val rankDialog by lazy { dialogs.getRank() }
    private val colorDialog by lazy { dialogs.getColor() }
    private val dateDialog by lazy { dialogs.getDate() }
    private val timeDialog by lazy { dialogs.getTime() }
    private val convertDialog by lazy { dialogs.getConvert(NoteType.TEXT) }

    private val nameEnter: EditText?
        get() = binding?.appBar?.content?.nameEnter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onSetup(bundle = arguments ?: savedInstanceState)
    }

    // TODO not save way to finish activity (view model is lateinit value)
    override fun inject(component: ScriptumComponent) {
        val bundleProvider = (activity as? NoteActivity)?.bundleProvider
        val (isEdit, noteState) = bundleProvider?.state ?: return run { activity?.finish() }
        val (id, _, color) = bundleProvider.data

        if (id == null || color == null) {
            activity?.finish()
            return
        }

        component.getTextNoteBuilder()
            .set(fragment = this)
            .set(isEdit)
            .set(noteState)
            .set(id)
            .set(color)
            .build()
            .inject(fragment = this)
    }

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

    // TODO remove
    //    override fun setupBinding() {
    //        binding?.apply { this.menuCallback = viewModel }
    //    }
    //
    //    override fun setupToolbar(color: Color) {
    //        val toolbar: Toolbar? = binding?.appBar?.content?.toolbar
    //        val indicator: View? = binding?.appBar?.indicator?.colorView
    //
    //        activity?.let {
    //            tintToolbar = TintNoteToolbar(it, it.window, toolbar, indicator, color)
    //            navigationIcon = BackToCancelIcon(it, toolbar, callback = this)
    //        }
    //
    //        toolbar?.setNavigationOnClickListener { viewModel.onClickBackArrow() }
    //    }

    override fun setupDialog(rankNameArray: Array<String>) {
        rankDialog.apply {
            itemArray = rankNameArray

            onPositiveClick { viewModel.onResultRankDialog(check = rankDialog.check - 1) }
            onDismiss { open.clear() }
        }

        colorDialog.apply {
            onPositiveClick { viewModel.onResultColorDialog(colorDialog.check) }
            onDismiss { open.clear() }
        }

        dateDialog.apply {
            onPositiveClick {
                open.skipClear = true
                viewModel.onResultDateDialog(dateDialog.calendar)
            }
            onNeutralClick { viewModel.onResultDateDialogClear() }
            onDismiss { open.clear() }
        }

        timeDialog.apply {
            onPositiveClick { viewModel.onResultTimeDialog(timeDialog.calendar) }
            onDismiss { open.clear() }
        }

        convertDialog.apply {
            onPositiveClick { viewModel.onResultConvertDialog() }
            onDismiss { open.clear() }
        }
    }

    override fun setupEnter(inputControl: IInputControl) {
        binding?.appBar?.content?.scrollView?.requestFocusOnVisible(nameEnter)

        nameEnter?.let {
            it.addTextChangedListener(
                    InputTextWatcher(it, InputAction.NAME, viewModel, inputControl)
            )
            it.addOnNextAction {
                binding?.textEnter?.apply {
                    requestFocus()
                    setSelection(text.toString().length)
                }
            }
        }

        binding?.contentScroll?.requestFocusOnVisible(binding?.textEnter)

        val inputWatcher = InputTextWatcher(
            binding?.textEnter,
            InputAction.TEXT,
            viewModel,
            inputControl
        )
        binding?.textEnter?.addTextChangedListener(inputWatcher)
    }


    override fun onBindingLoad(isRankEmpty: Boolean) {
        binding?.apply {
            this.isDataLoad = true
            this.isRankEmpty = isRankEmpty
        }?.executePendingBindings()
    }

    override fun onBindingNote(item: NoteItem.Text) {
        binding?.apply { this.item = item }?.executePendingBindings()
    }

    override fun onBindingEdit(item: NoteItem.Text, isEditMode: Boolean) {
        binding?.apply {
            this.item = item
            this.isEditMode = isEditMode
        }?.executePendingBindings()
    }

    override fun onBindingInput(item: NoteItem.Text, inputAccess: InputControl.Access) {
        binding?.apply {
            this.item = item
            this.inputAccess = inputAccess
        }?.executePendingBindings()
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

    override fun focusOnEdit(isCreate: Boolean) {
        view?.post {
            if (isCreate) {
                nameEnter?.requestSelectionFocus()
            } else {
                binding?.textEnter?.requestSelectionFocus()
            }
        }
    }

    override fun changeName(text: String, cursor: Int) {
        nameEnter?.apply {
            requestFocus()
            setText(text)
            setSelection(cursor)
        }
    }

    override fun changeText(text: String, cursor: Int) {
        binding?.textEnter?.apply {
            requestFocus()
            setText(text)
            setSelection(cursor)
        }
    }


    override fun showRankDialog(check: Int) = open.attempt {
        hideKeyboard()
        rankDialog.setArguments(check).safeShow(DialogFactory.Note.RANK, owner = this)
    }

    override fun showColorDialog(color: Color) = open.attempt {
        tintToolbar?.setColorFrom(color)

        hideKeyboard()
        colorDialog.setArguments(color).safeShow(DialogFactory.Note.COLOR, owner = this)
    }

    override fun showDateDialog(calendar: Calendar, resetVisible: Boolean) = open.attempt {
        open.tag = OpenState.Tag.DIALOG

        hideKeyboard()
        dateDialog.setArguments(calendar, resetVisible)
            .safeShow(DialogFactory.Note.DATE, owner = this)
    }

    override fun showTimeDialog(calendar: Calendar, dateList: List<String>) {
        open.attempt(OpenState.Tag.DIALOG) {
            hideKeyboard()
            timeDialog.setArguments(calendar, dateList)
                .safeShow(DialogFactory.Note.TIME, owner = this)
        }
    }

    override fun showConvertDialog() = open.attempt {
        hideKeyboard()
        convertDialog.safeShow(DialogFactory.Note.CONVERT, owner = this)
    }


    override fun showSaveToast(isSuccess: Boolean) {
        val text = if (isSuccess) R.string.toast_note_save_done else R.string.toast_note_save_error
        system.toast.show(context, text)
    }

    override fun finish() {
        activity?.finish()
    }

    //region Broadcast functions

    override fun sendSetAlarmBroadcast(id: Long, calendar: Calendar, showToast: Boolean) {
        system.broadcast.sendSetAlarm(id, calendar, showToast)
    }

    override fun sendCancelAlarmBroadcast(id: Long) = system.broadcast.sendCancelAlarm(id)

    override fun sendNotifyNotesBroadcast() = system.broadcast.sendNotifyNotesBind()

    override fun sendCancelNoteBroadcast(id: Long) = system.broadcast.sendCancelNoteBind(id)

    override fun sendNotifyInfoBroadcast(count: Int?) {
        system.broadcast.sendNotifyInfoBind(count)
    }

    //endregion

    //endregion

}