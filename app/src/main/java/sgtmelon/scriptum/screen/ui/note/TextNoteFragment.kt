package sgtmelon.scriptum.screen.ui.note

import android.app.Activity
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.input.InputCallback
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.control.input.watcher.InputTextWatcher
import sgtmelon.scriptum.control.menu.MenuControl
import sgtmelon.scriptum.control.menu.MenuControlAnim
import sgtmelon.scriptum.databinding.FragmentTextNoteBinding
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.factory.DialogFactory
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.InputAction
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.screen.ui.callback.note.INoteChild
import sgtmelon.scriptum.screen.ui.callback.note.text.ITextNoteFragment
import sgtmelon.scriptum.screen.vm.callback.note.ITextNoteViewModel
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel


/**
 * Фрагмент для отображения тектовой заметки
 *
 * @author SerjantArbuz
 */
class TextNoteFragment : Fragment(), ITextNoteFragment {

    private var binding: FragmentTextNoteBinding? = null

    private val openState = OpenState()
    private val rankDialog by lazy {
        DialogFactory.Note.getRankDialog(context as Activity, fragmentManager)
    }
    private val colorDialog by lazy {
        DialogFactory.Note.getColorDialog(context as Activity, fragmentManager)
    }
    private val convertDialog by lazy {
        DialogFactory.Note.getConvertDialog(context as Activity, fragmentManager, NoteType.TEXT)
    }

    // TODO #RELEASE
    private val dateDialog by lazy { DialogFactory.Note.getDateDialog(fragmentManager) }
    private val timeDialog by lazy { DialogFactory.Note.getTimeDialog(fragmentManager) }

    private val iViewModel: ITextNoteViewModel by lazy {
        ViewModelProviders.of(this).get(TextNoteViewModel::class.java).apply {
            callback = this@TextNoteFragment
            parentCallback = context as INoteChild
        }
    }

    private lateinit var menuControl: MenuControl

    private var nameEnter: EditText? = null
    private var textEnter: EditText? = null
    private var panelContainer: ViewGroup? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_text_note, container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        openState.get(savedInstanceState)

        iViewModel.onSetupData(savedInstanceState ?: arguments)

        panelContainer = view.findViewById(R.id.note_panel_container)
    }

    override fun onPause() {
        super.onPause()
        iViewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        iViewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply {
                openState.save(bundle = this)
                iViewModel.onSaveData(bundle = this)
            })

    fun onCancelNoteBind() = iViewModel.onCancelNoteBind()

    override fun setupBinding(@Theme theme: Int, rankEmpty: Boolean) {
        binding?.apply {
            currentTheme = theme
            menuCallback = iViewModel
            this.rankEmpty = rankEmpty
        }
    }

    override fun setupToolbar(@Theme theme: Int, @Color color: Int, noteState: NoteState) {
        val toolbar: Toolbar? = view?.findViewById(R.id.toolbar_note_container)
        val indicator: View? = view?.findViewById(R.id.toolbar_note_color_view)

        activity?.let {
            menuControl = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                MenuControl(theme, it, it.window, toolbar, indicator)
            } else {
                MenuControlAnim(theme, it, it.window, toolbar, indicator)
            }
        }

        menuControl.apply {
            setColor(color)
            setDrawable(drawableOn = noteState.isEdit && !noteState.isCreate, needAnim = false)
        }

        toolbar?.setNavigationOnClickListener { iViewModel.onClickBackArrow() }
    }

    override fun setupDialog(rankNameArray: List<String>) {
        rankDialog.apply {
            name = rankNameArray
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                iViewModel.onResultRankDialog(rankDialog.check)
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }

        colorDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                iViewModel.onResultColorDialog(colorDialog.check)
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }

        convertDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                iViewModel.onResultConvertDialog()
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }
    }

    override fun setupEnter(inputCallback: InputCallback) {
        nameEnter = view?.findViewById(R.id.toolbar_note_enter)
        view?.findViewById<View>(R.id.toolbar_note_scroll)?.requestFocusOnVisible(nameEnter)

        nameEnter?.let {
            it.addTextChangedListener(
                    InputTextWatcher(nameEnter, InputAction.name, iViewModel, inputCallback)
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
                InputTextWatcher(textEnter, InputAction.text, iViewModel, inputCallback)
        )
    }

    override fun bindNote(noteModel: NoteModel) {
        binding?.apply { this.noteModel = noteModel }?.executePendingBindings()
    }

    override fun bindEdit(editMode: Boolean, noteModel: NoteModel) {
        panelContainer?.let {
            TransitionManager.beginDelayedTransition(it,
                    AutoTransition().setOrdering(AutoTransition.ORDERING_TOGETHER).setDuration(100)
            )
        }

        binding?.apply {
            this.editMode = editMode
            this.noteModel = noteModel
        }?.executePendingBindings()
    }

    override fun bindInput(inputAccess: InputControl.Access, noteModel: NoteModel) {
        binding?.apply {
            this.inputAccess = inputAccess
            this.noteModel = noteModel
        }?.executePendingBindings()
    }

    override fun onPressBack() = iViewModel.onPressBack()

    override fun tintToolbar(from: Int, to: Int) =
            menuControl.apply { setColorFrom(from) }.startTint(to)

    override fun tintToolbar(@Color color: Int) = menuControl.startTint(color)

    override fun changeToolbarIcon(drawableOn: Boolean, needAnim: Boolean) =
            menuControl.setDrawable(drawableOn, needAnim)

    override fun focusOnEdit() {
        nameEnter?.requestSelectionFocus()
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

    override fun hideKeyboard() {
        activity?.hideKeyboard()
    }

    override fun showRankDialog(rankCheck: BooleanArray) = openState.tryInvoke {
        hideKeyboard()
        fragmentManager?.let {
            rankDialog.apply { setArguments(rankCheck) }.show(it, DialogFactory.Note.RANK)
        }
    }

    override fun showColorDialog(@Color color: Int) = openState.tryInvoke {
        menuControl.setColorFrom(color)

        hideKeyboard()
        fragmentManager?.let {
            colorDialog.apply { setArguments(color) }.show(it, DialogFactory.Note.COLOR)
        }
    }

    // TODO #RELEASE
    override fun showDateDialog() {
        fragmentManager?.let { timeDialog.show(it, DialogFactory.Note.TIME) }
    }

    override fun showConvertDialog() = openState.tryInvoke {
        hideKeyboard()
        fragmentManager?.let { convertDialog.show(it, DialogFactory.Note.CONVERT) }
    }

    companion object {
        fun getInstance(id: Long) = TextNoteFragment().apply {
            arguments = Bundle().apply { putLong(NoteData.Intent.ID, id) }
        }
    }

}