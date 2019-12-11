package sgtmelon.scriptum.screen.ui.note

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.RollAdapter
import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.control.bind.IBindControl
import sgtmelon.scriptum.control.input.IInputControl
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.control.input.watcher.InputTextWatcher
import sgtmelon.scriptum.control.menu.MenuControl
import sgtmelon.scriptum.control.menu.MenuControlAnim
import sgtmelon.scriptum.control.touch.RollTouchControl
import sgtmelon.scriptum.databinding.FragmentRollNoteBinding
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.factory.DialogFactory
import sgtmelon.scriptum.factory.ViewModelFactory
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.InputAction
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.screen.ui.ParentFragment
import sgtmelon.scriptum.screen.ui.callback.note.roll.IRollNoteFragment
import java.util.*

/**
 * Fragment for display roll note
 */
class RollNoteFragment : ParentFragment(), IRollNoteFragment {

    private var binding: FragmentRollNoteBinding? = null

    private val iViewModel by lazy { ViewModelFactory.getRollNoteViewModel(fragment = this) }

    private val iAlarmControl by lazy { AlarmControl[context] }
    private val iBindControl: IBindControl by lazy { BindControl(context) }
    private var menuControl: MenuControl? = null

    private val openState = OpenState()
    private val dialogFactory by lazy { DialogFactory.Note(context, fm) }

    private val rankDialog by lazy { dialogFactory.getRankDialog() }
    private val colorDialog by lazy { dialogFactory.getColorDialog() }

    private val dateDialog by lazy { dialogFactory.getDateDialog() }
    private val timeDialog by lazy { dialogFactory.getTimeDialog() }
    private val convertDialog by lazy { dialogFactory.getConvertDialog(NoteType.ROLL) }

    private val adapter: RollAdapter by lazy {
        RollAdapter(iViewModel, object : ItemListener.Click {
            override fun onItemClick(view: View, p: Int) = iViewModel.onClickItemCheck(p)
        }, object : ItemListener.LongClick {
            override fun onItemLongClick(view: View, p: Int) = iViewModel.onLongClickItemCheck()
        })
    }
    private val layoutManager by lazy { LinearLayoutManager(activity) }

    private var nameEnter: EditText? = null
    private var rollEnter: EditText? = null
    private var recyclerView: RecyclerView? = null

    private var parentContainer: ViewGroup? = null
    private var panelContainer: ViewGroup? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_roll_note, container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iAlarmControl.initLazy()
        iBindControl.initLazy()
        openState.get(savedInstanceState)

        iViewModel.onSetup(bundle = arguments ?: savedInstanceState)

        parentContainer = view.findViewById(R.id.roll_note_parent_container)
        panelContainer = view.findViewById(R.id.roll_note_content_container)
    }

    override fun onResume() {
        super.onResume()
        iViewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        iViewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        iViewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            openState.save(bundle = this)
            iViewModel.onSaveData(bundle = this)
        })
    }

    //region Receiver functions

    fun onCancelNoteBind() = iViewModel.onCancelNoteBind()

    //endregion

    override fun setupBinding(@Theme theme: Int) {
        binding?.apply {
            currentTheme = theme
            menuCallback = iViewModel
            this.rankEmpty = rankEmpty
        }
    }

    override fun setupToolbar(@Theme theme: Int, @Color color: Int) {
        val toolbar: Toolbar? = view?.findViewById(R.id.toolbar_note_container)
        val indicator: View? = view?.findViewById(R.id.toolbar_note_color_view)

        activity?.let {
            menuControl = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                MenuControl(theme, it, it.window, toolbar, indicator)
            } else {
                MenuControlAnim(theme, it, it.window, toolbar, indicator)
            }
        }

        menuControl?.setColor(color)?.setDrawable(drawableOn = false, needAnim = false)

        toolbar?.setNavigationOnClickListener { iViewModel.onClickBackArrow() }
    }

    override fun setupDialog(rankNameArray: Array<String>) {
        rankDialog.apply {
            itemArray = rankNameArray
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                iViewModel.onResultRankDialog(check = rankDialog.check - 1)
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }

        colorDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                iViewModel.onResultColorDialog(colorDialog.check)
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }

        dateDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                openState.skipClear = true
                iViewModel.onResultDateDialog(dateDialog.calendar)
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
            neutralListener = DialogInterface.OnClickListener { _, _ ->
                iViewModel.onResultDateDialogClear()
            }
        }

        timeDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                iViewModel.onResultTimeDialog(timeDialog.calendar)
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

    override fun setupEnter(iInputControl: IInputControl) {
        nameEnter = view?.findViewById(R.id.toolbar_note_enter)
        view?.findViewById<View>(R.id.toolbar_note_scroll)?.requestFocusOnVisible(nameEnter)

        nameEnter?.let {
            it.addTextChangedListener(
                    InputTextWatcher(it, InputAction.NAME, iViewModel, iInputControl)
            )

            it.addOnNextAction { onFocusEnter() }
        }

        rollEnter = view?.findViewById(R.id.roll_note_enter)
        rollEnter?.apply {
            setRawInputType(InputType.TYPE_CLASS_TEXT
                    or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                    or InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
                    or InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
            )
            imeOptions = EditorInfo.IME_ACTION_DONE or EditorInfo.IME_FLAG_NO_FULLSCREEN

            addTextChangedListener(on = { onBindingEnter() })
            setOnEditorActionListener { _, i, _ -> iViewModel.onEditorClick(i) }
        }

        view?.findViewById<ImageButton>(R.id.roll_note_add_button)?.apply {
            setOnClickListener { iViewModel.onClickAdd(simpleClick = true) }
            setOnLongClickListener {
                iViewModel.onClickAdd(simpleClick = false)
                return@setOnLongClickListener true
            }
        }
    }

    override fun setupRecycler(iInputControl: IInputControl) {
        val touchCallback = RollTouchControl(iViewModel)

        adapter.apply {
            dragListener = touchCallback
            this.iInputControl = iInputControl
        }

        recyclerView = view?.findViewById(R.id.roll_note_recycler)
        recyclerView?.let {
            (it.itemAnimator as? SimpleItemAnimator?)?.supportsChangeAnimations = false

            it.setHasFixedSize(true)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }

        ItemTouchHelper(touchCallback).attachToRecyclerView(recyclerView)
    }


    override fun onBindingLoad(rankEmpty: Boolean) {
        binding?.apply {
            this.dataLoad = true
            this.rankEmpty = rankEmpty
        }?.executePendingBindings()
    }

    override fun onBindingEdit(editMode: Boolean, noteItem: NoteItem) {
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

        onBindingEnter()
    }

    override fun onBingingNote(noteItem: NoteItem) {
        binding?.apply { this.noteItem = noteItem }?.executePendingBindings()
    }

    override fun onBindingEnter() {
        binding?.enterEmpty = getEnterText().clearSpace().isEmpty()
        binding?.executePendingBindings()
    }

    override fun onBindingInput(inputAccess: InputControl.Access, noteItem: NoteItem) {
        binding?.apply {
            this.inputAccess = inputAccess
            this.noteItem = noteItem
        }?.executePendingBindings()
    }


    override fun onPressBack() = iViewModel.onPressBack()

    override fun tintToolbar(from: Int, to: Int) {
        menuControl?.apply { setColorFrom(from) }?.startTint(to)
    }

    override fun tintToolbar(@Color color: Int) {
        menuControl?.startTint(color)
    }

    override fun changeToolbarIcon(drawableOn: Boolean, needAnim: Boolean) {
        menuControl?.setDrawable(drawableOn, needAnim)
    }

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

    override fun onFocusEnter() {
        rollEnter?.apply {
            requestFocus()
            setSelection(text.toString().length)
        }
    }

    override fun getEnterText() = rollEnter?.text?.toString() ?: ""

    override fun clearEnterText() {
        rollEnter?.setText("")
    }

    override fun scrollToItem(simpleClick: Boolean, p: Int, list: MutableList<RollItem>) {
        val fastScroll = with(layoutManager) {
            return@with if (simpleClick) {
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

    override fun updateNoteState(noteState: NoteState) {
        adapter.apply { this.noteState = noteState }.notifyDataSetChanged()
    }

    override fun notifyListItem(p: Int, rollItem: RollItem) = adapter.setListItem(p, rollItem)

    override fun notifyList(list: MutableList<RollItem>) = adapter.setList(list)

    override fun notifyDataSetChanged(list: MutableList<RollItem>) {
        adapter.apply { setList(list) }.notifyItemRangeChanged(0, list.size)
    }

    override fun notifyItemInserted(p: Int, cursor: Int, list: MutableList<RollItem>) {
        adapter.apply { cursorPosition = cursor }.notifyItemInserted(p, list)
    }

    override fun notifyItemChanged(p: Int, cursor: Int, list: MutableList<RollItem>) {
        adapter.apply { cursorPosition = cursor }.notifyItemChanged(p, list)
    }

    override fun notifyItemRemoved(p: Int, list: MutableList<RollItem>) {
        adapter.notifyItemRemoved(p, list)
    }

    override fun notifyItemMoved(from: Int, to: Int, list: MutableList<RollItem>) {
        adapter.notifyItemMoved(from, to, list)
    }

    override fun hideKeyboard() {
        activity?.hideKeyboard()
    }

    override fun showRankDialog(check: Int) = openState.tryInvoke {
        hideKeyboard()
        rankDialog.setArguments(check).show(fm, DialogFactory.Note.RANK)
    }

    override fun showColorDialog(@Color color: Int, @Theme theme: Int) = openState.tryInvoke {
        menuControl?.setColorFrom(color)

        hideKeyboard()
        colorDialog.setArguments(color, theme).show(fm, DialogFactory.Note.COLOR)
    }

    override fun showDateDialog(calendar: Calendar, resetVisible: Boolean) = openState.tryInvoke {
        openState.tag = OpenState.TAG_DATE_TIME

        hideKeyboard()
        dateDialog.setArguments(calendar, resetVisible).show(fm, DialogFactory.Note.DATE)
    }

    override fun showTimeDialog(calendar: Calendar, dateList: List<String>) {
        openState.tryInvoke(OpenState.TAG_DATE_TIME) {
            hideKeyboard()
            timeDialog.setArguments(calendar, dateList).show(fm, DialogFactory.Note.TIME)
        }
    }

    override fun showConvertDialog() = openState.tryInvoke {
        hideKeyboard()
        convertDialog.show(fm, DialogFactory.Note.CONVERT)
    }


    override fun setAlarm(calendar: Calendar, id: Long) {
        iAlarmControl.set(calendar, id)
    }

    override fun cancelAlarm(id: Long) = iAlarmControl.cancel(id)

    override fun notifyNoteBind(noteItem: NoteItem, rankIdVisibleList: List<Long>) {
        iBindControl.notifyNote(noteItem, rankIdVisibleList)
    }

    override fun cancelNoteBind(id: Int) = iBindControl.cancelNote(id)

    override fun notifyInfoBind(count: Int) = iBindControl.notifyInfo(count)

    companion object {
        operator fun get(id: Long, @Color color: Int) = RollNoteFragment().apply {
            arguments = Bundle().apply {
                putLong(NoteData.Intent.ID, id)
                putInt(NoteData.Intent.COLOR, color)
            }
        }
    }

}