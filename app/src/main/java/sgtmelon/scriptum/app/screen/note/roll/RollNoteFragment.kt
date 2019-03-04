package sgtmelon.scriptum.app.screen.note.roll

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
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import sgtmelon.safedialog.MessageDialog
import sgtmelon.safedialog.MultiplyDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.RollAdapter
import sgtmelon.scriptum.app.control.MenuControl
import sgtmelon.scriptum.app.control.MenuControlAnim
import sgtmelon.scriptum.app.control.input.InputCallback
import sgtmelon.scriptum.app.control.input.InputDef
import sgtmelon.scriptum.app.control.input.InputTextWatcher
import sgtmelon.scriptum.app.control.touch.RollTouchControl
import sgtmelon.scriptum.app.factory.DialogFactory
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.screen.note.NoteCallback
import sgtmelon.scriptum.databinding.FragmentRollNoteBinding
import sgtmelon.scriptum.office.AppTextWatcher
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.data.NoteData
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.state.NoteState
import sgtmelon.scriptum.office.utils.AppUtils.clear
import sgtmelon.scriptum.office.utils.AppUtils.hideKeyboard
import sgtmelon.scriptum.office.utils.AppUtils.inflateBinding
import sgtmelon.scriptum.widget.color.ColorDialog

/**
 * Фрагмент для отображения заметки списка
 */
class RollNoteFragment : Fragment(), RollNoteCallback {

    private lateinit var activity: Activity
    private lateinit var noteCallback: NoteCallback

    private lateinit var binding: FragmentRollNoteBinding

    private val viewModel: RollNoteViewModel by lazy {
        ViewModelProviders.of(this).get(RollNoteViewModel::class.java)
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

    private val adapter: RollAdapter by lazy { RollAdapter(activity) }
    private val layoutManager by lazy { LinearLayoutManager(activity) }

    private val nameEnter: EditText? by lazy {
        view?.findViewById<EditText>(R.id.toolbar_note_enter)
    }
    private val rollEnter: EditText? by lazy {
        view?.findViewById<EditText>(R.id.roll_note_enter)
    }
    private val recyclerView: RecyclerView? by lazy {
        view?.findViewById<RecyclerView>(R.id.roll_note_recycler)
    }

    private val rankDialog: MultiplyDialog by lazy {
        DialogFactory.getRankDialog(activity, fragmentManager)
    }
    private val colorDialog: ColorDialog by lazy {
        DialogFactory.getColorDialog(fragmentManager)
    }
    private val convertDialog: MessageDialog by lazy {
        DialogFactory.getConvertDialog(activity, fragmentManager, NoteType.ROLL)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        activity = context as Activity
        noteCallback = context as NoteCallback
    }

    override fun onResume() {
        super.onResume()

        viewModel.onUpdateData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_roll_note, container)

        viewModel.callback = this
        viewModel.noteCallback = noteCallback

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setupData(arguments ?: savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveData(outState)
    }

    override fun setupBinding(rankEmpty: Boolean) {
        binding.menuClick = viewModel
        binding.rankEmpty = rankEmpty
    }

    override fun setupToolbar(color: Int, noteState: NoteState) {
        menuControl.setColor(color)
        menuControl.setDrawable(
                drawableOn = noteState.isEdit && !noteState.isCreate, needAnim = false
        )

        toolbar?.setNavigationOnClickListener { TODO("onArrowBack click") }
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

    override fun setupRecycler() {
        (recyclerView?.itemAnimator as SimpleItemAnimator?)?.supportsChangeAnimations = false

        // TODO adapter click listener
        adapter.clickListener = ItemListener.ClickListener { _, p -> viewModel.onClickItemCheck(p) }

        val touchCallback = RollTouchControl(viewModel)
        adapter.dragListener = touchCallback

        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = adapter

        ItemTouchHelper(touchCallback).attachToRecyclerView(recyclerView)
    }

    override fun setupEnter(inputCallback: InputCallback) {
        nameEnter?.addTextChangedListener(
                InputTextWatcher(nameEnter, InputDef.name, viewModel, inputCallback)
        )

        nameEnter?.setOnEditorActionListener { _, i, _ ->
            if (i != EditorInfo.IME_ACTION_NEXT) {
                rollEnter?.requestFocus()
                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }

        rollEnter?.addTextChangedListener(object : AppTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                bindEnter()
            }
        })

        val rollAdd: ImageButton? = view?.findViewById(R.id.roll_note_add_button)
        rollAdd?.setOnClickListener { viewModel.onClickAdd(simpleClick = true) }
        rollAdd?.setOnLongClickListener {
            viewModel.onClickAdd(simpleClick = false)
            return@setOnLongClickListener true
        }
    }

    /**
     *
     */

    override fun bindEdit(mode: Boolean, noteItem: NoteItem) {
        binding.keyEdit = mode
        binding.noteItem = noteItem

        bindEnter()
    }

    override fun bindNoteItem(noteItem: NoteItem) =
            binding.apply { this.noteItem = noteItem }.executePendingBindings()

    override fun bindEnter() {
        binding.enterEmpty = TextUtils.isEmpty(rollEnter?.text.toString())
        binding.executePendingBindings()
    }

    override fun bindInput(isUndoAccess: Boolean, isRedoAccess: Boolean, isSaveEnable: Boolean) =
            binding.apply {
                undoAccess = isUndoAccess
                redoAccess = isRedoAccess
                saveEnabled = isSaveEnable
            }.executePendingBindings()

    override fun tintToolbar(color: Int) = menuControl.startTint(color)

    override fun changeToolbarIcon(drawableOn: Boolean, needAnim: Boolean) =
            menuControl.setDrawable(drawableOn, needAnim)

    override fun clearEnter(): String = rollEnter?.clear() ?: ""

    override fun scrollToItem(simpleClick: Boolean, p: Int, list: MutableList<RollItem>) {
        val fastScroll = when (simpleClick) {
            true -> layoutManager.findLastVisibleItemPosition() == p - 1
            false -> layoutManager.findFirstVisibleItemPosition() == p
        }

        if (fastScroll) {
            recyclerView?.scrollToPosition(p)
            adapter.notifyItemInserted(p, list)
        } else {
            recyclerView?.smoothScrollToPosition(p)
            adapter.notifyDataSetChanged(list)
        }
    }

    override fun changeCheckToggle(state: Boolean) {
        adapter.checkToggle = state
    }

    override fun updateNoteState(noteState: NoteState) =
            adapter.apply { this.noteState = noteState }.notifyDataSetChanged()

    override fun notifyListItem(p: Int, item: RollItem) = adapter.setListItem(p, item)

    override fun notifyList(list: MutableList<RollItem>) = adapter.setList(list)

    override fun notifyDataSetChanged(list: MutableList<RollItem>) =
            adapter.apply { setList(list) }.notifyItemRangeChanged(0, list.size)

    override fun notifyItemRemoved(p: Int, list: MutableList<RollItem>) =
            adapter.notifyItemRemoved(p, list)

    override fun notifyItemMoved(from: Int, to: Int, list: MutableList<RollItem>) =
            adapter.notifyItemMoved(from, to, list)

    override fun hideKeyboard() = activity.hideKeyboard()

    override fun showRankDialog(rankCheck: BooleanArray) {
        activity.hideKeyboard()

        rankDialog.setArguments(rankCheck)
        rankDialog.show(fragmentManager, DialogDef.RANK)
    }

    override fun showColorDialog(color: Int) {
        activity.hideKeyboard()

        menuControl.setColorFrom(color)

        colorDialog.setArguments(color)
        colorDialog.show(fragmentManager, DialogDef.COLOR)
    }

    override fun showConvertDialog() {
        activity.hideKeyboard()

        convertDialog.show(fragmentManager, DialogDef.CONVERT)
    }

    companion object {
        fun getInstance(id: Long) = RollNoteFragment().apply {
            arguments = Bundle().apply { putLong(NoteData.Intent.ID, id) }
        }
    }

}