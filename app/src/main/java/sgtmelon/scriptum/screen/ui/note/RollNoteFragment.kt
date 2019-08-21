package sgtmelon.scriptum.screen.ui.note

import android.app.Activity
import android.app.PendingIntent
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
import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.control.alarm.callback.IAlarmControl
import sgtmelon.scriptum.control.input.InputCallback
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.control.input.watcher.InputTextWatcher
import sgtmelon.scriptum.control.menu.MenuControl
import sgtmelon.scriptum.control.menu.MenuControlAnim
import sgtmelon.scriptum.control.touch.RollTouchControl
import sgtmelon.scriptum.databinding.FragmentRollNoteBinding
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.factory.DialogFactory
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.InputAction
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.room.entity.RollEntity
import sgtmelon.scriptum.screen.ui.callback.note.INoteChild
import sgtmelon.scriptum.screen.ui.callback.note.roll.IRollNoteFragment
import sgtmelon.scriptum.screen.vm.callback.note.IRollNoteViewModel
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel
import java.util.*

/**
 * Фрагмент для отображения заметки списка
 *
 * @author SerjantArbuz
 */
class RollNoteFragment : Fragment(), IRollNoteFragment {

    private var binding: FragmentRollNoteBinding? = null

    private val iViewModel: IRollNoteViewModel by lazy {
        ViewModelProviders.of(this).get(RollNoteViewModel::class.java).apply {
            callback = this@RollNoteFragment
            parentCallback = context as? INoteChild
        }
    }

    private val iAlarmControl: IAlarmControl by lazy { AlarmControl(context) }
    private var menuControl: MenuControl? = null

    private val openState = OpenState()
    private val rankDialog by lazy {
        DialogFactory.Note.getRankDialog(context as Activity, fragmentManager)
    }
    private val colorDialog by lazy {
        DialogFactory.Note.getColorDialog(context as Activity, fragmentManager)
    }

    private val dateDialog by lazy { DialogFactory.Note.getDateDialog(fragmentManager) }
    private val timeDialog by lazy { DialogFactory.Note.getTimeDialog(fragmentManager) }
    private val convertDialog by lazy {
        DialogFactory.Note.getConvertDialog(context as Activity, fragmentManager, NoteType.ROLL)
    }

    private val adapter: RollAdapter by lazy {
        RollAdapter(iViewModel,
                ItemListener.Click { _, p -> iViewModel.onClickItemCheck(p) },
                ItemListener.LongClick { _, _ -> iViewModel.onLongClickItemCheck() }
        )
    }
    private val layoutManager by lazy { LinearLayoutManager(activity) }

    private var nameEnter: EditText? = null
    private var rollEnter: EditText? = null
    private var recyclerView: RecyclerView? = null

    private var parentContainer: ViewGroup? = null
    private var enterContainer: View? = null

    private var panelContainer: ViewGroup? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_roll_note, container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        openState.get(savedInstanceState)

        iViewModel.onSetup(arguments ?: savedInstanceState)

        parentContainer = view.findViewById(R.id.roll_note_parent_container)
        enterContainer = view.findViewById(R.id.roll_note_enter_container)

        panelContainer = view.findViewById(R.id.roll_note_content_container)
    }

    override fun onResume() {
        super.onResume()
        iViewModel.onUpdateData()
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

        menuControl?.setColor(color)?.setDrawable(
                drawableOn = noteState.isEdit && !noteState.isCreate, needAnim = false
        )

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

    override fun setupEnter(inputCallback: InputCallback) {
        nameEnter = view?.findViewById(R.id.toolbar_note_enter)
        view?.findViewById<View>(R.id.toolbar_note_scroll)?.requestFocusOnVisible(nameEnter)

        nameEnter?.let {
            it.addTextChangedListener(
                    InputTextWatcher(nameEnter, InputAction.NAME, iViewModel, inputCallback)
            )
            it.addOnNextAction {
                rollEnter?.apply {
                    requestFocus()
                    setSelection(text.toString().length)
                }
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
            it.setOnEditorActionListener { _, i, _ -> iViewModel.onEditorClick(i) }
        }

        view?.findViewById<ImageButton>(R.id.roll_note_add_button)?.apply {
            setOnClickListener { iViewModel.onClickAdd(simpleClick = true) }
            setOnLongClickListener {
                iViewModel.onClickAdd(simpleClick = false)
                return@setOnLongClickListener true
            }
        }
    }

    override fun setupRecycler(inputCallback: InputCallback) {
        val touchCallback = RollTouchControl(iViewModel)

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

    override fun bindEdit(editMode: Boolean, noteModel: NoteModel) {
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
            this.noteModel = noteModel
        }

        bindEnter()
    }

    override fun bindNote(noteModel: NoteModel) {
        binding?.apply { this.noteModel = noteModel }?.executePendingBindings()
    }

    override fun bindEnter() {
        binding?.enterEmpty = rollEnter?.text.toString().isEmpty() == true
        binding?.executePendingBindings()
    }

    override fun bindInput(inputAccess: InputControl.Access, noteModel: NoteModel) {
        binding?.apply {
            this.inputAccess = inputAccess
            this.noteModel = noteModel
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

    override fun getEnterText() = rollEnter?.text?.toString() ?: ""

    override fun clearEnterText() {
        rollEnter?.setText("")
    }

    override fun scrollToItem(simpleClick: Boolean, p: Int, list: MutableList<RollEntity>) {
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

    override fun notifyListItem(p: Int, rollEntity: RollEntity) = adapter.setListItem(p, rollEntity)

    override fun notifyList(list: MutableList<RollEntity>) = adapter.setList(list)

    override fun notifyDataSetChanged(list: MutableList<RollEntity>) =
            adapter.apply { setList(list) }.notifyItemRangeChanged(0, list.size)

    override fun notifyItemInserted(p: Int, cursor: Int, list: MutableList<RollEntity>) =
            adapter.apply { cursorPosition = cursor }.notifyItemInserted(p, list)

    override fun notifyItemChanged(p: Int, list: MutableList<RollEntity>, cursor: Int) =
            adapter.apply { cursorPosition = cursor }.notifyItemChanged(p, list)

    override fun notifyItemRemoved(p: Int, list: MutableList<RollEntity>) =
            adapter.notifyItemRemoved(p, list)

    override fun notifyItemMoved(from: Int, to: Int, list: MutableList<RollEntity>) =
            adapter.notifyItemMoved(from, to, list)

    override fun hideKeyboard() {
        activity?.hideKeyboard()
    }

    override fun showRankDialog(check: Int) = openState.tryInvoke {
        hideKeyboard()
        fragmentManager?.let {
            rankDialog.setArguments(check).show(it, DialogFactory.Note.RANK)
        }
    }

    override fun showColorDialog(color: Int) = openState.tryInvoke {
        menuControl?.setColorFrom(color)

        hideKeyboard()
        fragmentManager?.let {
            colorDialog.setArguments(color).show(it, DialogFactory.Note.COLOR)
        }
    }

    override fun showDateDialog(calendar: Calendar, resetVisible: Boolean) = openState.tryInvoke {
        hideKeyboard()
        fragmentManager?.let {
            dateDialog.setArguments(calendar, resetVisible).show(it, DialogFactory.Note.DATE)
        }
    }

    override fun showTimeDialog(calendar: Calendar, dateList: List<String>) = openState.tryInvoke({
        clear()
    }) {
        hideKeyboard()
        fragmentManager?.let {
            timeDialog.setArguments(calendar, dateList).show(it, DialogFactory.Note.TIME)
        }
    }

    override fun showConvertDialog() = openState.tryInvoke {
        hideKeyboard()
        fragmentManager?.let { convertDialog.show(it, DialogFactory.Note.CONVERT) }
    }


    override fun setAlarm(calendar: Calendar, intent: PendingIntent) =
            iAlarmControl.set(calendar, intent)

    override fun cancelAlarm(intent: PendingIntent) = iAlarmControl.cancel(intent)


    companion object {
        fun getInstance(id: Long) = RollNoteFragment().apply {
            arguments = Bundle().apply { putLong(NoteData.Intent.ID, id) }
        }
    }

}