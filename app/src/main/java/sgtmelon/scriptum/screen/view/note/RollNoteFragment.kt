package sgtmelon.scriptum.screen.view.note

import android.app.Activity
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.InputType
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
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.RollAdapter
import sgtmelon.scriptum.control.input.InputCallback
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.control.input.watcher.InputTextWatcher
import sgtmelon.scriptum.control.menu.MenuControl
import sgtmelon.scriptum.control.menu.MenuControlAnim
import sgtmelon.scriptum.control.touch.RollTouchControl
import sgtmelon.scriptum.databinding.FragmentRollNoteBinding
import sgtmelon.scriptum.factory.DialogFactory
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.InputAction
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.*
import sgtmelon.scriptum.screen.callback.note.NoteChildCallback
import sgtmelon.scriptum.screen.callback.note.roll.RollNoteCallback
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel

/**
 * Фрагмент для отображения заметки списка
 *
 * @author SerjantArbuz
 */
class RollNoteFragment : Fragment(), RollNoteCallback {

    private var binding: FragmentRollNoteBinding? = null

    private val viewModel: RollNoteViewModel by lazy {
        ViewModelProviders.of(this).get(RollNoteViewModel::class.java).apply {
            callback = this@RollNoteFragment
            parentCallback = context as NoteChildCallback
        }
    }

    private lateinit var menuControl: MenuControl

    private val adapter: RollAdapter by lazy {
        RollAdapter(ItemListener.ClickListener { _, p -> viewModel.onClickItemCheck(p) },
                ItemListener.LongClickListener { _, p -> viewModel.onLongClickItemCheck(p) },
                viewModel
        )
    }
    private val layoutManager by lazy { LinearLayoutManager(activity) }

    private var nameEnter: EditText? = null
    private var rollEnter: EditText? = null
    private var recyclerView: RecyclerView? = null

    private var parentContainer: ViewGroup? = null
    private var enterContainer: View? = null

    private var panelContainer: ViewGroup? = null

    private val openState = OpenState()
    private val rankDialog by lazy {
        DialogFactory.getRankDialog(context as Activity, fragmentManager)
    }
    private val colorDialog by lazy { DialogFactory.getColorDialog(fragmentManager) }
    private val convertDialog by lazy {
        DialogFactory.getConvertDialog(context as Activity, fragmentManager, NoteType.ROLL)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_roll_note, container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        openState.get(savedInstanceState)

        viewModel.onSetupData(arguments ?: savedInstanceState)

        parentContainer = view.findViewById(R.id.roll_note_parent_container)
        enterContainer = view.findViewById(R.id.roll_note_enter_container)

        panelContainer = view.findViewById(R.id.roll_note_content_container)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUpdateData()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply {
                openState.save(bundle = this)
                viewModel.onSaveData(bundle = this)
            })

    fun onCancelNoteBind() = viewModel.onCancelNoteBind()

    override fun setupBinding(rankEmpty: Boolean) {
        binding?.menuCallback = viewModel
        binding?.rankEmpty = rankEmpty
    }

    override fun setupToolbar(color: Int, noteState: NoteState) {
        val toolbar: Toolbar? = view?.findViewById(R.id.toolbar_note_container)
        val indicator: View? = view?.findViewById(R.id.toolbar_note_color_view)

        activity?.let {
            menuControl = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                MenuControl(it, it.window, toolbar, indicator)
            } else {
                MenuControlAnim(it, it.window, toolbar, indicator)
            }
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
        }.title = getString(R.string.dialog_title_color)

        convertDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                viewModel.onResultConvertDialog()
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }
    }

    override fun setupEnter(inputCallback: InputCallback) {
        nameEnter = view?.findViewById(R.id.toolbar_note_enter)
        view?.findViewById<View>(R.id.toolbar_note_scroll)?.requestFocusOnVisible(nameEnter)

        nameEnter?.addTextChangedListener(
                InputTextWatcher(nameEnter, InputAction.name, viewModel, inputCallback)
        )
        nameEnter?.addOnNextAction {
            rollEnter?.apply {
                requestFocus()
                setSelection(text.toString().length)
            }
        }

        rollEnter = view?.findViewById(R.id.roll_note_enter)
        rollEnter?.let {
            it.setRawInputType(InputType.TYPE_CLASS_TEXT
                    or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                    or InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
                    or InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
            )
            it.imeOptions = EditorInfo.IME_ACTION_DONE or EditorInfo.IME_FLAG_NO_FULLSCREEN

            it.addTextChangedListener(on = { bindEnter() })
            it.setOnEditorActionListener { _, i, _ -> viewModel.onEditorClick(i) }
        }

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
            dragListener = touchCallback
            this.inputCallback = inputCallback
        }

        recyclerView = view?.findViewById(R.id.roll_note_recycler)
        recyclerView?.let {
            (it.itemAnimator as? SimpleItemAnimator?)?.supportsChangeAnimations = false
            it.layoutManager = layoutManager
            it.adapter = adapter
        }

        ItemTouchHelper(touchCallback).attachToRecyclerView(recyclerView)
    }

    override fun bindEdit(editMode: Boolean, noteItem: NoteItem) {
        panelContainer?.let {
            TransitionManager.beginDelayedTransition(it,
                    AutoTransition()
                            .setOrdering(AutoTransition.ORDERING_TOGETHER)
                            .excludeChildren(R.id.roll_note_recycler, true)
                            .setDuration(100)
            )
        }

        binding?.apply {
            this.editMode = editMode
            this.noteItem = noteItem
        }

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

    override fun bindInput(inputAccess: InputControl.Access, isSaveEnabled: Boolean) {
        binding?.apply {
            this.inputAccess = inputAccess
            this.isSaveEnabled = isSaveEnabled
        }?.executePendingBindings()
    }

    override fun bindItem(noteItem: NoteItem) {
        binding?.apply { this.noteItem = noteItem }?.executePendingBindings()
    }

    override fun onPressBack() = viewModel.onPressBack()

    override fun tintToolbar(from: Int, to: Int) =
            menuControl.apply { setColorFrom(from) }.startTint(to)

    override fun tintToolbar(color: Int) = menuControl.startTint(color)

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

    override fun getEnterText() = rollEnter?.text?.toString() ?: ""

    override fun clearEnterText() {
        rollEnter?.setText("")
    }

    override fun scrollToItem(simpleClick: Boolean, p: Int, list: MutableList<RollItem>) {
        val fastScroll = with(layoutManager) {
            if (simpleClick) {
                findLastVisibleItemPosition() == p - 1
            } else {
                findFirstVisibleItemPosition() == p
            }
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

    override fun notifyItemChanged(p: Int, list: MutableList<RollItem>, cursor: Int) {
        adapter.apply {
            cursorPosition = cursor
            notifyItemChanged(p, list)
        }
    }

    override fun notifyItemRemoved(p: Int, list: MutableList<RollItem>) =
            adapter.notifyItemRemoved(p, list)

    override fun notifyItemMoved(from: Int, to: Int, list: MutableList<RollItem>) =
            adapter.notifyItemMoved(from, to, list)

    override fun hideKeyboard() {
        activity?.hideKeyboard()
    }

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