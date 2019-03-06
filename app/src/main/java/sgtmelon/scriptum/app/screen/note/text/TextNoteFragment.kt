package sgtmelon.scriptum.app.screen.note.text

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import sgtmelon.safedialog.MessageDialog
import sgtmelon.safedialog.MultiplyDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.control.MenuControl
import sgtmelon.scriptum.app.control.MenuControlAnim
import sgtmelon.scriptum.app.control.input.InputCallback
import sgtmelon.scriptum.app.control.input.InputDef
import sgtmelon.scriptum.app.control.input.InputTextWatcher
import sgtmelon.scriptum.app.factory.DialogFactory
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.screen.note.NoteCallback
import sgtmelon.scriptum.databinding.FragmentTextNoteBinding
import sgtmelon.scriptum.office.annot.def.ColorDef
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.data.NoteData
import sgtmelon.scriptum.office.state.NoteState
import sgtmelon.scriptum.office.utils.AppUtils.hideKeyboard
import sgtmelon.scriptum.office.utils.AppUtils.inflateBinding
import sgtmelon.scriptum.widget.color.ColorDialog

/**
 * Фрагмент для отображения тектовой заметки
 */
class TextNoteFragment : Fragment(), TextNoteCallback {

    private lateinit var activity: Activity
    private lateinit var noteCallback: NoteCallback

    private lateinit var binding: FragmentTextNoteBinding

    private val viewModel: TextNoteViewModel by lazy {
        ViewModelProviders.of(this).get(TextNoteViewModel::class.java)
    }

    private val toolbar: Toolbar? by lazy {
        view?.findViewById<Toolbar>(R.id.toolbar_note_container)
    }
    private val indicator: View?  by lazy {
        view?.findViewById<View>(R.id.toolbar_note_color_view)
    }

    private val menuControl: MenuControl by lazy {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            MenuControl(activity, activity.window, toolbar, indicator)
        } else {
            MenuControlAnim(activity, activity.window, toolbar, indicator)
        }
    }

    private val nameEnter: EditText? by lazy {
        view?.findViewById<EditText>(R.id.toolbar_note_enter)
    }
    private val textEnter: EditText? by lazy {
        view?.findViewById<EditText>(R.id.text_note_content_enter)
    }

    private val rankDialog: MultiplyDialog by lazy {
        DialogFactory.getRankDialog(activity, fragmentManager)
    }
    private val colorDialog: ColorDialog by lazy {
        DialogFactory.getColorDialog(fragmentManager)
    }
    private val convertDialog: MessageDialog by lazy {
        DialogFactory.getConvertDialog(activity, fragmentManager, NoteType.TEXT)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        activity = context as Activity
        noteCallback = context as NoteCallback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_text_note, container)

        viewModel.callback = this
        viewModel.noteCallback = noteCallback

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setupData(arguments ?: savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveData(outState)
    }

    override fun setupBinding(rankEmpty: Boolean) {
        binding.menuClick = viewModel
        binding.rankEmpty = rankEmpty
    }

    override fun setupToolbar(@ColorDef color: Int, noteState: NoteState) {
        menuControl.apply {
            setColor(color)
            setDrawable(drawableOn = noteState.isEdit && !noteState.isCreate, needAnim = false)
        }

        toolbar?.setNavigationOnClickListener { viewModel.onClickBackArrow() }
    }

    override fun setupDialog(rankNameArray: Array<String>) {
        rankDialog.name = rankNameArray
        rankDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultRankDialog(rankDialog.check)
        }

        colorDialog.title = activity.getString(R.string.dialog_title_color)
        colorDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultColorDialog(colorDialog.check)
        }

        convertDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultConvertDialog()
        }
    }

    override fun setupEnter(inputCallback: InputCallback) {
        nameEnter?.addTextChangedListener(
                InputTextWatcher(nameEnter, InputDef.name, viewModel, inputCallback)
        )

        nameEnter?.setOnEditorActionListener { _, i, _ ->
            if (i != EditorInfo.IME_ACTION_NEXT) {
                textEnter?.requestFocus()
                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }

        textEnter?.addTextChangedListener(
                InputTextWatcher(textEnter, InputDef.text, viewModel, inputCallback)
        )
    }

    override fun bindEdit(mode: Boolean, noteItem: NoteItem) =
            binding.apply {
                keyEdit = mode
                this.noteItem = noteItem
            }.executePendingBindings()

    override fun bindInput(isUndoAccess: Boolean, isRedoAccess: Boolean) =
            binding.apply {
                undoAccess = isUndoAccess
                redoAccess = isRedoAccess
                saveEnabled = !TextUtils.isEmpty(textEnter?.text.toString())
            }.executePendingBindings()

    override fun onPressBack() = viewModel.onPressBack()

    override fun tintToolbar(from: Int, to: Int) =
            menuControl.apply { setColorFrom(from) }.startTint(to)

    override fun tintToolbar(color: Int) = menuControl.startTint(color)

    override fun changeToolbarIcon(drawableOn: Boolean, needAnim: Boolean) =
            menuControl.setDrawable(drawableOn, needAnim)

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

    override fun hideKeyboard() = activity.hideKeyboard()

    override fun showRankDialog(rankCheck: BooleanArray) {
        hideKeyboard()

        rankDialog.setArguments(rankCheck)
        rankDialog.show(fragmentManager, DialogDef.RANK)
    }

    override fun showColorDialog(color: Int) {
        hideKeyboard()

        menuControl.setColorFrom(color)

        colorDialog.setArguments(color)
        colorDialog.show(fragmentManager, DialogDef.COLOR)
    }

    override fun showConvertDialog() {
        hideKeyboard()

        convertDialog.show(fragmentManager, DialogDef.CONVERT)
    }

    companion object {
        fun getInstance(id: Long) = TextNoteFragment().apply {
            arguments = Bundle().apply { putLong(NoteData.Intent.ID, id) }
        }
    }

}