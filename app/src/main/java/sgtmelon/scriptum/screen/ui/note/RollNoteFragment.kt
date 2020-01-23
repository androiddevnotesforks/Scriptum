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
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.transition.AutoTransition
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.RollAdapter
import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.control.bind.BindControl
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
import sgtmelon.scriptum.receiver.NoteReceiver
import sgtmelon.scriptum.screen.ui.ParentFragment
import sgtmelon.scriptum.screen.ui.callback.note.roll.IRollNoteFragment
import java.util.*

/**
 * Fragment for display roll note
 */
class RollNoteFragment : ParentFragment(), IRollNoteFragment, NoteReceiver.Callback {

    private var binding: FragmentRollNoteBinding? = null

    private val iViewModel by lazy { ViewModelFactory.Note.get(fragment = this) }

    private val iAlarmControl by lazy { AlarmControl[context] }
    private val iBindControl by lazy { BindControl[context] }
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

    private var parentContainer: ViewGroup? = null

    /**
     * Setup manually because after rotation lazy function will return null.
     */
    private var nameEnter: EditText? = null
    private var rollEnter: EditText? = null
    private var recyclerView: RecyclerView? = null
    private var rollProgress: ProgressBar? = null
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
        panelContainer = view.findViewById(R.id.note_panel_container)
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

    override fun onReceiveUnbindNote(id: Long) = iViewModel.onReceiveUnbindNote(id)

    //endregion

    override val isDialogOpen: Boolean get() = openState.value

    override fun hideKeyboard() {
        activity?.hideKeyboard()
    }


    override fun setupBinding(@Theme theme: Int) {
        binding?.apply {
            this.theme = theme
            this.menuCallback = iViewModel
        }
    }

    override fun setupToolbar(@Theme theme: Int, @Color color: Int) {
        val toolbar: Toolbar? = view?.findViewById(R.id.toolbar_note_content_container)
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

        rollEnter = view?.findViewById(R.id.roll_add_panel_enter)
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

        view?.findViewById<ImageButton>(R.id.roll_add_panel_button)?.apply {
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

    override fun setupProgress() {
        rollProgress = view?.findViewById(R.id.roll_note_progress)
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
        }

        onBindingEnter()
    }

    override fun onBingingNote(item: NoteItem) {
        binding?.apply { this.item = item }?.executePendingBindings()
    }

    override fun onBindingEnter() {
        binding?.enterEmpty = getEnterText().clearSpace().isEmpty()
        binding?.executePendingBindings()
    }

    override fun onBindingInput(item: NoteItem, inputAccess: InputControl.Access) {
        binding?.apply {
            this.item = item
            this.inputAccess = inputAccess
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

    override fun focusOnEdit(isCreate: Boolean) {
        if (isCreate) {
            view?.post { nameEnter?.requestSelectionFocus() }
        } else {
            view?.post { rollEnter?.requestSelectionFocus() }
        }
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
            adapter.setList(list).notifyItemInserted(p)
        } else {
            recyclerView?.smoothScrollToPosition(p)
            adapter.setList(list).notifyDataSetChanged()
        }
    }

    override fun changeCheckToggle(state: Boolean) {
        adapter.checkToggle = state
    }

    override fun updateNoteState(noteState: NoteState) {
        adapter.apply { this.noteState = noteState }.notifyDataSetChanged()
    }

    override fun updateProgress(progress: Int, max: Int) {
        rollProgress?.max = max

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            rollProgress?.setProgress(progress, true)
        } else {
            rollProgress?.progress = progress
        }
    }

    override fun setList(list: List<RollItem>) {
        adapter.setList(list)
    }

    override fun notifyDataSetChanged(list: List<RollItem>) {
        adapter.setList(list).notifyDataSetChanged()
    }

    override fun notifyItemChanged(list: List<RollItem>, p: Int, cursor: Int?) {
        if (cursor != null) adapter.cursorPosition = cursor

        adapter.setList(list).notifyItemChanged(p)
    }

    override fun notifyItemMoved(list: List<RollItem>, from: Int, to: Int) {
        adapter.setList(list).notifyItemMoved(from, to)
    }

    override fun notifyItemInserted(list: List<RollItem>, p: Int, cursor: Int) {
        adapter.apply { cursorPosition = cursor }.setList(list).notifyItemInserted(p)
    }

    override fun notifyItemRemoved(list: List<RollItem>, p: Int) {
        adapter.setList(list).notifyItemRemoved(p)
    }

    override fun notifyItemRemoved(p: Int) {
        adapter.notifyItemRemoved(p)
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

    override fun notifyNoteBind(item: NoteItem, rankIdVisibleList: List<Long>) {
        iBindControl.notifyNote(item, rankIdVisibleList)
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