package sgtmelon.scriptum.presentation.screen.ui.impl.note

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.transition.AutoTransition
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sgtmelon.iconanim.IconBlockCallback
import sgtmelon.iconanim.IconChangeCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.control.input.IInputControl
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.control.input.watcher.InputTextWatcher
import sgtmelon.scriptum.control.toolbar.IToolbarTintControl
import sgtmelon.scriptum.control.toolbar.ToolbarTintControl
import sgtmelon.scriptum.control.toolbar.icon.NavigationIconControl
import sgtmelon.scriptum.control.toolbar.icon.NavigationIconControlAnim
import sgtmelon.scriptum.databinding.FragmentTextNoteBinding
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.InputAction
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.presentation.factory.DialogFactory
import sgtmelon.scriptum.presentation.receiver.NoteReceiver
import sgtmelon.scriptum.presentation.screen.ui.ParentFragment
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.note.text.ITextNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.note.ITextNoteViewModel
import java.util.*
import javax.inject.Inject

/**
 * Fragment for display text note.
 */
class TextNoteFragment : ParentFragment(), ITextNoteFragment,
        NoteReceiver.Callback,
        IconBlockCallback {

    private var binding: FragmentTextNoteBinding? = null

    @Inject internal lateinit var viewModel: ITextNoteViewModel

    private val alarmControl by lazy { AlarmControl[context] }
    private val bindControl by lazy { BindControl[context] }

    private var toolbarTintControl: IToolbarTintControl? = null
    private var navigationIconControl: IconChangeCallback? = null


    private val openState = OpenState()
    private val dialogFactory by lazy { DialogFactory.Note(context, fm) }

    private val rankDialog by lazy { dialogFactory.getRankDialog() }
    private val colorDialog by lazy { dialogFactory.getColorDialog() }

    private val dateDialog by lazy { dialogFactory.getDateDialog() }
    private val timeDialog by lazy { dialogFactory.getTimeDialog() }
    private val convertDialog by lazy { dialogFactory.getConvertDialog(NoteType.TEXT) }

    /**
     * Setup manually because after rotation lazy function will return null.
     */
    private var parentContainer: ViewGroup? = null
    private var nameEnter: EditText? = null
    private var textEnter: EditText? = null
    private var panelContainer: ViewGroup? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_text_note, container)

        ScriptumApplication.component.getTextNoteBuilder().set(fragment = this).build()
                .inject(fragment = this)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alarmControl.initLazy()
        bindControl.initLazy()

        openState.get(savedInstanceState)

        viewModel.onSetup(bundle = savedInstanceState ?: arguments)

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
        super.onSaveInstanceState(outState.apply {
            openState.save(bundle = this)
            viewModel.onSaveData(bundle = this)
        })
    }

    //region Callback functions

    override fun onReceiveUnbindNote(id: Long) = viewModel.onReceiveUnbindNote(id)

    override fun setEnabled(enabled: Boolean) {
        openState.value = !enabled
    }

    //endregion

    override val isDialogOpen: Boolean get() = openState.value

    override fun hideKeyboard() {
        activity?.hideKeyboard()
    }


    override fun setupBinding(@Theme theme: Int) {
        binding?.apply {
            this.theme = theme
            this.menuCallback = viewModel
        }
    }

    override fun setupToolbar(@Theme theme: Int, @Color color: Int) {
        val toolbar: Toolbar? = view?.findViewById(R.id.toolbar_note_content_container)
        val indicator: View? = view?.findViewById(R.id.toolbar_note_color_view)

        activity?.let {
            toolbarTintControl = ToolbarTintControl(it, it.window, toolbar, indicator, theme, color)

            navigationIconControl = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                NavigationIconControl(it, toolbar)
            } else {
                NavigationIconControlAnim(it, toolbar, blockCallback = this)
            }
        }

        toolbar?.setNavigationOnClickListener { viewModel.onClickBackArrow() }
    }

    override fun setupDialog(rankNameArray: Array<String>) {
        rankDialog.apply {
            itemArray = rankNameArray
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                viewModel.onResultRankDialog(check = rankDialog.check - 1)
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }

        colorDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                viewModel.onResultColorDialog(colorDialog.check)
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }

        dateDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                openState.skipClear = true
                viewModel.onResultDateDialog(dateDialog.calendar)
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
            neutralListener = DialogInterface.OnClickListener { _, _ ->
                viewModel.onResultDateDialogClear()
            }
        }

        timeDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                viewModel.onResultTimeDialog(timeDialog.calendar)
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }

        convertDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                viewModel.onResultConvertDialog()
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }
    }

    override fun setupEnter(iInputControl: IInputControl) {
        nameEnter = view?.findViewById(R.id.toolbar_note_enter)
        view?.findViewById<View>(R.id.toolbar_note_scroll)?.requestFocusOnVisible(nameEnter)

        nameEnter?.let {
            it.addTextChangedListener(
                    InputTextWatcher(it, InputAction.NAME, viewModel, iInputControl)
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
                InputTextWatcher(textEnter, InputAction.TEXT, viewModel, iInputControl)
        )
    }


    override fun onBindingLoad(rankEmpty: Boolean) {
        parentContainer?.let {
            val time = resources.getInteger(R.integer.fade_anim_time)
            val transition = Fade().setDuration(time.toLong())

            TransitionManager.beginDelayedTransition(it, transition)
        }

        binding?.apply {
            this.dataLoad = true
            this.rankEmpty = rankEmpty
        }?.executePendingBindings()
    }

    override fun onBindingNote(item: NoteItem) {
        binding?.apply { this.item = item }?.executePendingBindings()
    }

    override fun onBindingEdit(editMode: Boolean, item: NoteItem) {
        panelContainer?.let {
            val time = resources.getInteger(R.integer.fade_anim_time)
            val transition = AutoTransition()
                    .setOrdering(AutoTransition.ORDERING_TOGETHER)
                    .setDuration(time.toLong())

            TransitionManager.beginDelayedTransition(it, transition)
        }

        binding?.apply {
            this.item = item
            this.editMode = editMode
        }?.executePendingBindings()
    }

    override fun onBindingInput(item: NoteItem, inputAccess: InputControl.Access) {
        binding?.apply {
            this.item = item
            this.inputAccess = inputAccess
        }?.executePendingBindings()
    }


    override fun onPressBack() = viewModel.onPressBack()

    override fun tintToolbar(from: Int, to: Int) {
        toolbarTintControl?.setColorFrom(from)?.startTint(to)
    }

    override fun tintToolbar(@Color color: Int) {
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


    override fun showRankDialog(check: Int) = openState.tryInvoke {
        hideKeyboard()
        rankDialog.setArguments(check).show(fm, DialogFactory.Note.RANK)
    }

    override fun showColorDialog(@Color color: Int, @Theme theme: Int) = openState.tryInvoke {
        toolbarTintControl?.setColorFrom(color)

        hideKeyboard()
        colorDialog.setArguments(color, theme).show(fm, DialogFactory.Note.COLOR)
    }

    override fun showDateDialog(calendar: Calendar, resetVisible: Boolean) = openState.tryInvoke {
        openState.tag = OpenState.Tag.DIALOG

        hideKeyboard()
        dateDialog.setArguments(calendar, resetVisible).show(fm, DialogFactory.Note.DATE)
    }

    override fun showTimeDialog(calendar: Calendar, dateList: List<String>) {
        openState.tryInvoke(OpenState.Tag.DIALOG) {
            hideKeyboard()
            timeDialog.setArguments(calendar, dateList).show(fm, DialogFactory.Note.TIME)
        }
    }

    override fun showConvertDialog() = openState.tryInvoke {
        hideKeyboard()
        convertDialog.show(fm, DialogFactory.Note.CONVERT)
    }


    override fun showSaveToast(success: Boolean) {
        val text = if (success) R.string.toast_note_save_done else R.string.toast_note_save_error

        context?.showToast(text)
    }


    override fun setAlarm(calendar: Calendar, id: Long) {
        alarmControl.set(calendar, id)
    }

    override fun cancelAlarm(id: Long) = alarmControl.cancel(id)

    override fun notifyNoteBind(item: NoteItem, rankIdVisibleList: List<Long>) {
        bindControl.notifyNote(item, rankIdVisibleList)
    }

    override fun cancelNoteBind(id: Int) = bindControl.cancelNote(id)

    override fun notifyInfoBind(count: Int) = bindControl.notifyInfo(count)

    companion object {
        operator fun get(id: Long, @Color color: Int) = TextNoteFragment().apply {
            arguments = Bundle().apply {
                putLong(NoteData.Intent.ID, id)
                putInt(NoteData.Intent.COLOR, color)
            }
        }
    }

}