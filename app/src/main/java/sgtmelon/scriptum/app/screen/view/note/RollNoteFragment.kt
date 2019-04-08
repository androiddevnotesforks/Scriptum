package sgtmelon.scriptum.app.screen.view.note

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
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
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.RollAdapter
import sgtmelon.scriptum.app.control.input.InputCallback
import sgtmelon.scriptum.app.control.menu.MenuControl
import sgtmelon.scriptum.app.control.menu.MenuControlAnim
import sgtmelon.scriptum.app.control.touch.RollTouchControl
import sgtmelon.scriptum.app.factory.DialogFactory
import sgtmelon.scriptum.app.model.data.NoteData
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.model.key.InputAction
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.model.state.NoteState
import sgtmelon.scriptum.app.model.state.OpenState
import sgtmelon.scriptum.app.screen.callback.note.NoteCallback
import sgtmelon.scriptum.app.screen.callback.note.roll.RollNoteCallback
import sgtmelon.scriptum.app.screen.vm.note.RollNoteViewModel
import sgtmelon.scriptum.app.watcher.AppTextWatcher
import sgtmelon.scriptum.app.watcher.InputTextWatcher
import sgtmelon.scriptum.databinding.FragmentRollNoteBinding
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.AppUtils.clear
import sgtmelon.scriptum.office.utils.AppUtils.hideKeyboard
import sgtmelon.scriptum.office.utils.AppUtils.inflateBinding

/**
 * Фрагмент для отображения заметки списка
 *
 * @author SerjantArbuz
 */
class RollNoteFragment : Fragment(), RollNoteCallback {

    private lateinit var activity: Activity
    private lateinit var parentCallback: NoteCallback

    private var binding: FragmentRollNoteBinding? = null

    private val viewModel: RollNoteViewModel by lazy {
        ViewModelProviders.of(this).get(RollNoteViewModel::class.java).apply {
            callback = this@RollNoteFragment
            noteCallback = parentCallback
        }
    }

    private lateinit var menuControl: MenuControl

    private val adapter: RollAdapter by lazy { RollAdapter(activity) }
    private val layoutManager by lazy { LinearLayoutManager(activity) }

    private var nameEnter: EditText? = null
    private var rollEnter: EditText? = null
    private var recyclerView: RecyclerView? = null

    private val openState = OpenState()
    private val rankDialog by lazy { DialogFactory.getRankDialog(activity, fragmentManager) }
    private val colorDialog by lazy { DialogFactory.getColorDialog(fragmentManager) }
    private val convertDialog by lazy {
        DialogFactory.getConvertDialog(activity, fragmentManager, NoteType.ROLL)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        activity = context as Activity
        parentCallback = context as NoteCallback
    }

    override fun onResume() {
        super.onResume()

        viewModel.onUpdateData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_roll_note, container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            openState.value = savedInstanceState.getBoolean(OpenState.KEY)
        }

        viewModel.setupData(arguments ?: savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply {
                putBoolean(OpenState.KEY, openState.value)
                viewModel.saveData(bundle = this)
            })

    override fun setupBinding(rankEmpty: Boolean) {
        binding?.menuCallback = viewModel
        binding?.rankEmpty = rankEmpty
    }

    override fun setupToolbar(color: Int, noteState: NoteState) {
        val toolbar: Toolbar? = view?.findViewById(R.id.toolbar_note_container)
        val indicator: View? = view?.findViewById(R.id.toolbar_note_color_view)

        menuControl = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            MenuControl(activity, activity.window, toolbar, indicator)
        } else {
            MenuControlAnim(activity, activity.window, toolbar, indicator)
        }

        menuControl.apply {
            setColor(color)
            setDrawable(drawableOn = noteState.isEdit && !noteState.isCreate, needAnim = false)
        }

        toolbar?.setNavigationOnClickListener { viewModel.onClickBackArrow() }
    }

    override fun setupDialog(rankNameList: List<String>) {
        rankDialog.apply {
            name = rankNameList
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                viewModel.onResultRankDialog(rankDialog.check)
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }

        colorDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                viewModel.onResultColorDialog(colorDialog.check)
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }.title = activity.getString(R.string.dialog_title_color)

        convertDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                viewModel.onResultConvertDialog()
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }
    }

    override fun setupEnter(inputCallback: InputCallback) {
        nameEnter = view?.findViewById(R.id.toolbar_note_enter)
        nameEnter?.addTextChangedListener(
                InputTextWatcher(nameEnter, InputAction.name, viewModel, inputCallback)
        )

        nameEnter?.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_NEXT) {
                rollEnter?.requestFocus()
                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }

        rollEnter = view?.findViewById(R.id.roll_note_enter)
        rollEnter?.addTextChangedListener(object : AppTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                    bindEnter()
        })

        view?.findViewById<ImageButton>(R.id.roll_note_add_button)?.apply {
            setOnClickListener { viewModel.onClickAdd(simpleClick = true) }
            setOnLongClickListener {
                viewModel.onClickAdd(simpleClick = false)
                return@setOnLongClickListener true
            }
        }
    }

    override fun setupRecycler(inputCallback: InputCallback) {
        val touchCallback = RollTouchControl(viewModel)

        adapter.apply {
            clickListener = ItemListener.ClickListener { _, p -> viewModel.onClickItemCheck(p) }
            dragListener = touchCallback
            this.inputCallback = inputCallback
            textChangeCallback = viewModel
        }

        recyclerView = view?.findViewById(R.id.roll_note_recycler)
        (recyclerView?.itemAnimator as? SimpleItemAnimator?)?.supportsChangeAnimations = false

        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = adapter

        ItemTouchHelper(touchCallback).attachToRecyclerView(recyclerView)
    }

    override fun bindEdit(mode: Boolean, noteItem: NoteItem) {
        binding?.keyEdit = mode
        binding?.noteItem = noteItem

        bindEnter()
    }

    override fun bindNoteItem(noteItem: NoteItem) {
        binding?.noteItem = noteItem
        binding?.executePendingBindings()
    }

    override fun bindEnter() {
        binding?.enterEmpty = rollEnter?.text.toString().isEmpty() == true
        binding?.executePendingBindings()
    }

    override fun bindInput(isUndoAccess: Boolean, isRedoAccess: Boolean, isSaveEnable: Boolean) {
        binding?.apply {
            undoAccess = isUndoAccess
            redoAccess = isRedoAccess
            saveEnabled = isSaveEnable
        }?.executePendingBindings()
    }

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

    override fun notifyItemInserted(p: Int, cursor: Int, list: MutableList<RollItem>) {
        adapter.apply {
            cursorPosition = cursor
            notifyItemInserted(p, list)
        }
    }

    override fun notifyItemChanged(p: Int, cursor: Int, list: MutableList<RollItem>) {
        adapter.apply {
            cursorPosition = cursor
            notifyItemChanged(p, list)
        }
    }

    override fun notifyItemRemoved(p: Int, list: MutableList<RollItem>) =
            adapter.notifyItemRemoved(p, list)

    override fun notifyItemMoved(from: Int, to: Int, list: MutableList<RollItem>) =
            adapter.notifyItemMoved(from, to, list)

    override fun hideKeyboard() = activity.hideKeyboard()

    override fun showRankDialog(rankCheck: BooleanArray) = openState.tryInvoke {
        hideKeyboard()
        rankDialog.apply { setArguments(rankCheck) }.show(fragmentManager, DialogDef.RANK)
    }

    override fun showColorDialog(color: Int) = openState.tryInvoke {
        menuControl.setColorFrom(color)

        hideKeyboard()
        colorDialog.apply { setArguments(color) }.show(fragmentManager, DialogDef.COLOR)
    }

    override fun showConvertDialog() = openState.tryInvoke {
        hideKeyboard()
        convertDialog.show(fragmentManager, DialogDef.CONVERT)
    }

    companion object {
        fun getInstance(id: Long) = RollNoteFragment().apply {
            arguments = Bundle().apply { putLong(NoteData.Intent.ID, id) }
        }
    }

}