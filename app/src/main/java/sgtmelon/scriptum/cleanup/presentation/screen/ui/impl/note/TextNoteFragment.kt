package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.transition.Fade
import androidx.transition.TransitionManager
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
import sgtmelon.scriptum.cleanup.extension.hideKeyboard
import sgtmelon.scriptum.cleanup.extension.requestFocusOnVisible
import sgtmelon.scriptum.cleanup.extension.requestSelectionFocus
import sgtmelon.scriptum.cleanup.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.cleanup.presentation.control.note.input.InputControl
import sgtmelon.scriptum.cleanup.presentation.control.note.input.watcher.InputTextWatcher
import sgtmelon.scriptum.cleanup.presentation.control.toolbar.icon.NavigationIconControl
import sgtmelon.scriptum.cleanup.presentation.control.toolbar.tint.IToolbarTintControl
import sgtmelon.scriptum.cleanup.presentation.control.toolbar.tint.ToolbarTintControl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note.ITextNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.ITextNoteViewModel
import sgtmelon.scriptum.databinding.FragmentTextNoteBinding
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.NoteType
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.parent.ParentFragment
import sgtmelon.test.idling.addIdlingListener
import sgtmelon.test.idling.getIdling

/**
 * Fragment for display text note.
 */
class TextNoteFragment : ParentFragment<FragmentTextNoteBinding>(),
    ITextNoteFragment,
    UnbindNoteReceiver.Callback,
    IconBlockCallback {

    override val layoutId: Int = R.layout.fragment_text_note

    @Inject lateinit var viewModel: ITextNoteViewModel

    private var toolbarTintControl: IToolbarTintControl? = null
    private var navigationIconControl: IconChangeCallback? = null

    private val dialogs by lazy { DialogFactory.Note(context, fm) }

    private val rankDialog by lazy { dialogs.getRank() }
    private val colorDialog by lazy { dialogs.getColor() }
    private val dateDialog by lazy { dialogs.getDate() }
    private val timeDialog by lazy { dialogs.getTime() }
    private val convertDialog by lazy { dialogs.getConvert(NoteType.TEXT) }

    /**
     * Setup manually because after rotation lazy function will return null.
     */
    private var parentContainer: ViewGroup? = null
    private var nameEnter: EditText? = null
    private var textEnter: EditText? = null
    private var panelContainer: ViewGroup? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView(view)
        viewModel.onSetup(bundle = arguments ?: savedInstanceState)
    }

    override fun inject(component: ScriptumComponent) {
        component.getTextNoteBuilder()
            .set(fragment = this)
            .build()
            .inject(fragment = this)
    }

    private fun setupView(view: View) {
        parentContainer = view.findViewById(R.id.text_note_parent_container)
        panelContainer = view.findViewById(R.id.note_panel_container)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveData(outState)
    }

    //region Callback functions

    override fun onReceiveUnbindNote(noteId: Long) = viewModel.onReceiveUnbindNote(noteId)

    override fun setEnabled(isEnabled: Boolean) {
        getIdling().change(!isEnabled, IdlingTag.Anim.ICON)
        open.isBlocked = !isEnabled
    }

    //endregion

    override val isDialogOpen: Boolean get() = open.isBlocked

    override fun hideKeyboard() {
        activity?.hideKeyboard()
    }


    override fun setupBinding() {
        binding?.apply { this.menuCallback = viewModel }
    }

    override fun setupToolbar(color: Color) {
        val toolbar: Toolbar? = view?.findViewById(R.id.toolbar_note_content_container)
        val indicator: View? = view?.findViewById(R.id.toolbar_note_color_view)

        activity?.let {
            toolbarTintControl = ToolbarTintControl(it, it.window, toolbar, indicator, color)
            navigationIconControl = NavigationIconControl(it, toolbar, callback = this)
        }

        toolbar?.setNavigationOnClickListener { viewModel.onClickBackArrow() }
    }

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
        nameEnter = view?.findViewById(R.id.toolbar_note_enter)
        view?.findViewById<View>(R.id.toolbar_note_scroll)?.requestFocusOnVisible(nameEnter)

        nameEnter?.let {
            it.addTextChangedListener(
                    InputTextWatcher(it, InputAction.NAME, viewModel, inputControl)
            )
            it.addOnNextAction {
                textEnter?.apply {
                    requestFocus()
                    setSelection(text.toString().length)
                }
            }
        }

        textEnter = view?.findViewById(R.id.text_note_content_enter)
        view?.findViewById<View>(R.id.text_note_content_scroll)?.requestFocusOnVisible(textEnter)

        textEnter?.addTextChangedListener(
                InputTextWatcher(textEnter, InputAction.TEXT, viewModel, inputControl)
        )
    }


    override fun onBindingLoad(isRankEmpty: Boolean) {
        parentContainer?.let {
            val time = resources.getInteger(R.integer.note_open_time)
            val transition = Fade().setDuration(time.toLong()).addIdlingListener()

            TransitionManager.beginDelayedTransition(it, transition)
        }

        binding?.apply {
            this.isDataLoad = true
            this.isRankEmpty = isRankEmpty
        }?.executePendingBindings()
    }

    override fun onBindingNote(item: NoteItem.Text) {
        binding?.apply { this.item = item }?.executePendingBindings()
    }

    override fun onBindingEdit(item: NoteItem.Text, isEditMode: Boolean) {
        panelContainer?.let {
            val time = resources.getInteger(R.integer.note_change_time).toLong()
            TransitionManager.beginDelayedTransition(it, Fade().setDuration(time))
        }

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
        toolbarTintControl?.setColorFrom(from)?.startTint(to)
    }

    override fun tintToolbar(color: Color) {
        toolbarTintControl?.startTint(color)
    }

    override fun setToolbarBackIcon(isCancel: Boolean, needAnim: Boolean) {
        navigationIconControl?.setDrawable(isCancel, needAnim)
    }

    override fun focusOnEdit(isCreate: Boolean) {
        if (isCreate) {
            view?.post { nameEnter?.requestSelectionFocus() }
        } else {
            view?.post { textEnter?.requestSelectionFocus() }
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
        textEnter?.apply {
            requestFocus()
            setText(text)
            setSelection(cursor)
        }
    }


    override fun showRankDialog(check: Int) = open.attempt {
        hideKeyboard()
        rankDialog.setArguments(check).safeShow(fm, DialogFactory.Note.RANK, owner = this)
    }

    override fun showColorDialog(color: Color) = open.attempt {
        toolbarTintControl?.setColorFrom(color)

        hideKeyboard()
        colorDialog.setArguments(color).safeShow(fm, DialogFactory.Note.COLOR, owner = this)
    }

    override fun showDateDialog(calendar: Calendar, resetVisible: Boolean) = open.attempt {
        open.tag = OpenState.Tag.DIALOG

        hideKeyboard()
        dateDialog.setArguments(calendar, resetVisible)
            .safeShow(fm, DialogFactory.Note.DATE, owner = this)
    }

    override fun showTimeDialog(calendar: Calendar, dateList: List<String>) {
        open.attempt(OpenState.Tag.DIALOG) {
            hideKeyboard()
            timeDialog.setArguments(calendar, dateList)
                .safeShow(fm, DialogFactory.Note.TIME, owner = this)
        }
    }

    override fun showConvertDialog() = open.attempt {
        hideKeyboard()
        convertDialog.safeShow(fm, DialogFactory.Note.CONVERT, owner = this)
    }


    override fun showSaveToast(isSuccess: Boolean) {
        val text = if (isSuccess) R.string.toast_note_save_done else R.string.toast_note_save_error
        delegators.toast.show(context, text)
    }

    //region Broadcast functions

    override fun sendSetAlarmBroadcast(id: Long, calendar: Calendar, showToast: Boolean) {
        delegators.broadcast.sendSetAlarm(id, calendar, showToast)
    }

    override fun sendCancelAlarmBroadcast(id: Long) = delegators.broadcast.sendCancelAlarm(id)

    override fun sendNotifyNotesBroadcast() = delegators.broadcast.sendNotifyNotesBind()

    override fun sendCancelNoteBroadcast(id: Long) = delegators.broadcast.sendCancelNoteBind(id)

    override fun sendNotifyInfoBroadcast(count: Int?) {
        delegators.broadcast.sendNotifyInfoBind(count)
    }

    //endregion

    companion object {
        operator fun get(id: Long, color: Color) = TextNoteFragment().apply {
            arguments = Bundle().apply {
                putLong(Note.Intent.ID, id)
                putInt(Note.Intent.COLOR, color.ordinal)
            }
        }
    }
}