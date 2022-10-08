package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note

import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.TransitionManager
import java.util.Calendar
import javax.inject.Inject
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.iconanim.callback.IconChangeCallback
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.annotation.InputAction
import sgtmelon.scriptum.cleanup.domain.model.annotation.test.IdlingTag
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.domain.model.state.NoteState
import sgtmelon.scriptum.cleanup.domain.model.state.OpenState
import sgtmelon.scriptum.cleanup.extension.addOnNextAction
import sgtmelon.scriptum.cleanup.extension.clearSpace
import sgtmelon.scriptum.cleanup.extension.createVisibleAnim
import sgtmelon.scriptum.cleanup.extension.hideKeyboard
import sgtmelon.scriptum.cleanup.extension.inflateBinding
import sgtmelon.scriptum.cleanup.extension.requestFocusOnVisible
import sgtmelon.scriptum.cleanup.extension.requestSelectionFocus
import sgtmelon.scriptum.cleanup.extension.setFirstRunAnimation
import sgtmelon.scriptum.cleanup.presentation.adapter.RollAdapter
import sgtmelon.scriptum.cleanup.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.cleanup.presentation.control.note.input.InputControl
import sgtmelon.scriptum.cleanup.presentation.control.note.input.watcher.InputTextWatcher
import sgtmelon.scriptum.cleanup.presentation.control.toolbar.icon.NavigationIconControl
import sgtmelon.scriptum.cleanup.presentation.control.toolbar.icon.VisibleIconControl
import sgtmelon.scriptum.cleanup.presentation.control.toolbar.tint.IToolbarTintControl
import sgtmelon.scriptum.cleanup.presentation.control.toolbar.tint.ToolbarTintControl
import sgtmelon.scriptum.cleanup.presentation.control.touch.RollTouchControl
import sgtmelon.scriptum.cleanup.presentation.factory.DialogFactory
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ParentFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note.IRollNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.IRollNoteViewModel
import sgtmelon.scriptum.databinding.FragmentRollNoteBinding
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.widgets.listeners.RecyclerOverScrollListener
import sgtmelon.test.idling.addIdlingListener
import sgtmelon.test.idling.getIdling

/**
 * Fragment for display roll note.
 */
class RollNoteFragment : ParentFragment(),
    IRollNoteFragment,
    Toolbar.OnMenuItemClickListener,
    UnbindNoteReceiver.Callback,
    IconBlockCallback {

    private var binding: FragmentRollNoteBinding? = null

    @Inject lateinit var viewModel: IRollNoteViewModel

    private var toolbarTintControl: IToolbarTintControl? = null
    private var navigationIconControl: IconChangeCallback? = null
    private var visibleIconControl: IconChangeCallback? = null

    private val dialogs by lazy { DialogFactory.Note(context, fm) }

    private val rankDialog by lazy { dialogs.getRankDialog() }
    private val colorDialog by lazy { dialogs.getColorDialog() }

    private val dateDialog by lazy { dialogs.getDateDialog() }
    private val timeDialog by lazy { dialogs.getTimeDialog() }
    private val convertDialog by lazy { dialogs.getConvertDialog(NoteType.ROLL) }

    private val animTime by lazy {
        context?.resources?.getInteger(R.integer.icon_animation_time)?.toLong() ?: 0L
    }

    private val adapter: RollAdapter by lazy {
        RollAdapter(viewModel, object : ItemListener.ActionClick {
            override fun onItemClick(view: View, p: Int, action: () -> Unit) {
                if (!open.isChangeEnabled && open.tag == OpenState.Tag.ANIM) {
                    open.isChangeEnabled = true
                }

                open.attempt(OpenState.Tag.ANIM) {
                    open.block(animTime)
                    open.tag = OpenState.Tag.ANIM

                    action()
                    viewModel.onClickItemCheck(p)
                }
            }
        })
    }
    private val layoutManager by lazy { LinearLayoutManager(activity) }

    private var parentContainer: ViewGroup? = null
    private var panelContainer: ViewGroup? = null
    private var emptyInfoView: View? = null

    /**
     * Setup manually because after rotation lazy function will return null.
     */
    private var nameEnter: EditText? = null
    private var rollEnter: EditText? = null
    private var recyclerView: RecyclerView? = null
    private var rollProgress: ProgressBar? = null

    private var visibleMenuItem: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflateBinding(R.layout.fragment_roll_note, container)

        ScriptumApplication.component.getRollNoteBuilder().set(fragment = this).build()
            .inject(fragment = this)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView(view)
        viewModel.onSetup(bundle = arguments ?: savedInstanceState)
    }

    private fun setupView(view: View) {
        parentContainer = view.findViewById(R.id.roll_note_parent_container)
        panelContainer = view.findViewById(R.id.note_panel_container)
        emptyInfoView = view.findViewById(R.id.roll_note_info_include)
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

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        open.attempt(withSwitch = false) { viewModel.onClickVisible() }
        return true
    }

    //region Callback functions

    override fun onReceiveUnbindNote(noteId: Long) = viewModel.onReceiveUnbindNote(noteId)

    override fun setEnabled(isEnabled: Boolean) {
        getIdling().change(!isEnabled, IdlingTag.Anim.ICON)
        open.isBlocked = !isEnabled
    }

    //endregion

    override val isDialogOpen: Boolean get() = open.isBlocked

    override fun setTouchAction(inAction: Boolean) {
        open.isBlocked = inAction
    }

    override fun hideKeyboard() {
        activity?.hideKeyboard()
    }


    override fun setupBinding() {
        binding?.apply { this.menuCallback = viewModel }
    }

    override fun setupToolbar(color: Color) {
        val toolbar: Toolbar? = view?.findViewById(R.id.toolbar_note_content_container)
        val indicator: View? = view?.findViewById(R.id.toolbar_note_color_view)

        toolbar?.inflateMenu(R.menu.fragment_roll_note)
        visibleMenuItem = toolbar?.menu?.findItem(R.id.item_visible)

        activity?.let {
            toolbarTintControl = ToolbarTintControl(it, it.window, toolbar, indicator, color)
            navigationIconControl = NavigationIconControl(it, toolbar, callback = this)
            visibleIconControl = VisibleIconControl(it, visibleMenuItem, callback = this)
        }

        toolbar?.setNavigationOnClickListener { viewModel.onClickBackArrow() }
        toolbar?.setOnMenuItemClickListener(this)
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

            it.addOnNextAction { onFocusEnter() }
        }

        rollEnter = view?.findViewById(R.id.roll_add_panel_enter)
        rollEnter?.apply {
            setRawInputType(
                InputType.TYPE_CLASS_TEXT
                        or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                        or InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
                        or InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
            )
            imeOptions = EditorInfo.IME_ACTION_DONE or EditorInfo.IME_FLAG_NO_FULLSCREEN

            doOnTextChanged { _, _, _, _ -> onBindingEnter() }
            setOnEditorActionListener { _, i, _ -> viewModel.onEditorClick(i) }
        }

        view?.findViewById<ImageButton>(R.id.roll_add_panel_button)?.apply {
            setOnClickListener { viewModel.onClickAdd(simpleClick = true) }
            setOnLongClickListener {
                viewModel.onClickAdd(simpleClick = false)
                return@setOnLongClickListener true
            }
        }
    }

    override fun setupRecycler(inputControl: IInputControl, isFirstRun: Boolean) {
        val touchCallback = RollTouchControl(viewModel)

        adapter.apply {
            this.dragListener = touchCallback
            this.inputControl = inputControl

            registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                /**
                 * Update before animation ends.
                 */
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    viewModel.onUpdateInfo()
                }
            })
        }

        recyclerView = view?.findViewById(R.id.roll_note_recycler)
        recyclerView?.let {
            it.setFirstRunAnimation(
                isFirstRun, R.anim.layout_item_roll, supportsChangeAnimations = false
            ) { viewModel.onUpdateInfo() }

            it.addOnScrollListener(RecyclerOverScrollListener(showFooter = false))
            it.setHasFixedSize(true)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }

        ItemTouchHelper(touchCallback).attachToRecyclerView(recyclerView)
    }

    override fun setupProgress() {
        rollProgress = view?.findViewById(R.id.roll_note_progress)
    }

    override fun showToolbarVisibleIcon(isShow: Boolean) {
        visibleMenuItem?.isVisible = isShow
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

    override fun onBindingInfo(isListEmpty: Boolean, isListHide: Boolean) {
        binding?.apply {
            this.isListEmpty = isListEmpty
            this.isListHide = isListHide
        }?.executePendingBindings()
    }

    override fun onBindingEdit(item: NoteItem.Roll, isEditMode: Boolean) {
        panelContainer?.let {
            val time = resources.getInteger(R.integer.note_change_time).toLong()
            TransitionManager.beginDelayedTransition(it, Fade().setDuration(time))
        }

        binding?.apply {
            this.item = item
            this.isEditMode = isEditMode
        }

        onBindingEnter()
    }

    override fun onBindingNote(item: NoteItem.Roll) {
        binding?.apply { this.item = item }?.executePendingBindings()
    }

    override fun onBindingEnter() {
        binding?.isEnterEmpty = getEnterText().clearSpace().isEmpty()
        binding?.executePendingBindings()
    }


    override fun onBindingInput(item: NoteItem.Roll, inputAccess: InputControl.Access) {
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

    override fun setToolbarVisibleIcon(isVisible: Boolean, needAnim: Boolean) {
        visibleMenuItem?.title = getString(if (isVisible) {
            R.string.menu_roll_visible
        } else {
            R.string.menu_roll_invisible
        })

        visibleIconControl?.setDrawable(isVisible, needAnim)
    }


    override fun animateInfoVisible(isVisible: Boolean?) {
        val isListEmpty = adapter.itemCount == 0

        parentContainer?.createVisibleAnim(emptyInfoView, isVisible = isVisible ?: isListEmpty)
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
        val smoothInsert = with(layoutManager) {
            if (adapter.itemCount == 0) return@with true

            return@with if (simpleClick) {
                findLastVisibleItemPosition() == p - 1
            } else {
                findFirstVisibleItemPosition() == p
            }
        }

        if (smoothInsert) {
            recyclerView?.scrollToPosition(p)
            adapter.setList(list).notifyItemInserted(p)
        } else {
            recyclerView?.smoothScrollToPosition(p)
            adapter.setList(list)
            recyclerView?.post { adapter.notifyDataSetChanged() }
        }
    }

    override fun updateNoteState(noteState: NoteState) {
        adapter.noteState = noteState
        recyclerView?.post { adapter.notifyDataSetChanged() }
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
        adapter.setList(list)
        recyclerView?.post { adapter.notifyDataSetChanged() }
    }

    override fun notifyDataRangeChanged(list: List<RollItem>) {
        adapter.setList(list).notifyItemRangeChanged(0, list.size)
    }

    override fun notifyItemChanged(list: List<RollItem>, p: Int, cursor: Int?) {
        if (cursor != null) adapter.cursorPosition = cursor

        adapter.setList(list).notifyItemChanged(p)
    }

    override fun notifyItemMoved(list: List<RollItem>, from: Int, to: Int) {
        adapter.setList(list).notifyItemMoved(from, to)
    }

    override fun notifyItemInserted(list: List<RollItem>, p: Int, cursor: Int?) {
        if (cursor != null) adapter.cursorPosition = cursor

        adapter.setList(list).notifyItemInserted(p)
    }

    override fun notifyItemRemoved(list: List<RollItem>, p: Int) {
        adapter.setList(list).notifyItemRemoved(p)
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
        operator fun get(id: Long, color: Color) = RollNoteFragment().apply {
            arguments = Bundle().apply {
                putLong(Note.Intent.ID, id)
                putInt(Note.Intent.COLOR, color.ordinal)
            }
        }
    }
}