package sgtmelon.scriptum.presentation.screen.ui.impl.note

import android.content.DialogInterface
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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.transition.AutoTransition
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sgtmelon.iconanim.IconBlockCallback
import sgtmelon.iconanim.IconChangeCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.FragmentRollNoteBinding
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.InputAction
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.data.NoteData
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.domain.model.state.OpenState
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.presentation.adapter.RollAdapter
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.control.note.input.InputControl
import sgtmelon.scriptum.presentation.control.note.input.watcher.InputTextWatcher
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.control.toolbar.icon.NavigationIconControl
import sgtmelon.scriptum.presentation.control.toolbar.icon.NavigationIconControlAnim
import sgtmelon.scriptum.presentation.control.toolbar.icon.VisibleIconControl
import sgtmelon.scriptum.presentation.control.toolbar.icon.VisibleIconControlAnim
import sgtmelon.scriptum.presentation.control.toolbar.tint.IToolbarTintControl
import sgtmelon.scriptum.presentation.control.toolbar.tint.ToolbarTintControl
import sgtmelon.scriptum.presentation.control.touch.RollTouchControl
import sgtmelon.scriptum.presentation.factory.DialogFactory
import sgtmelon.scriptum.presentation.listener.ItemListener
import sgtmelon.scriptum.presentation.receiver.NoteReceiver
import sgtmelon.scriptum.presentation.screen.ui.ParentFragment
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.note.roll.IRollNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.note.IRollNoteViewModel
import java.util.*
import javax.inject.Inject

/**
 * Fragment for display roll note.
 */
class RollNoteFragment : ParentFragment(), IRollNoteFragment,
        Toolbar.OnMenuItemClickListener,
        NoteReceiver.Callback,
        IconBlockCallback {

    private var binding: FragmentRollNoteBinding? = null

    @Inject internal lateinit var viewModel: IRollNoteViewModel

    private val alarmControl by lazy { AlarmControl[context] }
    private val bindControl by lazy { BindControl[context] }

    private var toolbarTintControl: IToolbarTintControl? = null
    private var navigationIconControl: IconChangeCallback? = null
    private var visibleIconControl: IconChangeCallback? = null

    override val openState = OpenState()
    private val dialogFactory by lazy { DialogFactory.Note(context, fm) }

    private val rankDialog by lazy { dialogFactory.getRankDialog() }
    private val colorDialog by lazy { dialogFactory.getColorDialog() }

    private val dateDialog by lazy { dialogFactory.getDateDialog() }
    private val timeDialog by lazy { dialogFactory.getTimeDialog() }
    private val convertDialog by lazy { dialogFactory.getConvertDialog(NoteType.ROLL) }

    private val adapter: RollAdapter by lazy {
        RollAdapter(viewModel, object : ItemListener.ActionClick {
            override fun onItemClick(view: View, p: Int, action: () -> Unit) {
                action()
                viewModel.onClickItemCheck(p)
            }
        }, object : ItemListener.LongClick {
            override fun onItemLongClick(view: View, p: Int) {
                viewModel.onLongClickItemCheck()
            }
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

    private var visibleMenuItem: MenuItem? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_roll_note, container)

        ScriptumApplication.component.getRollNoteBuilder().set(fragment = this).build()
                .inject(fragment = this)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alarmControl.initLazy()
        bindControl.initLazy()

        openState.get(savedInstanceState)

        viewModel.onSetup(bundle = arguments ?: savedInstanceState)

        parentContainer = view.findViewById(R.id.roll_note_parent_container)
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

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        viewModel.onClickVisible()
        return true
    }

    //region Callback functions

    override fun onReceiveUnbindNote(id: Long) = viewModel.onReceiveUnbindNote(id)

    override fun setEnabled(enabled: Boolean) {
        openState.value = !enabled
    }

    //endregion

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

        toolbar?.inflateMenu(R.menu.fragment_roll_note)
        visibleMenuItem = toolbar?.menu?.findItem(R.id.item_visible)

        activity?.let {
            toolbarTintControl = ToolbarTintControl(it, it.window, toolbar, indicator, theme, color)

            navigationIconControl = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                NavigationIconControl(it, toolbar)
            } else {
                NavigationIconControlAnim(it, toolbar, blockCallback = this)
            }

            visibleIconControl = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                VisibleIconControl(it, visibleMenuItem)
            } else {
                VisibleIconControlAnim(it, visibleMenuItem, blockCallback = this)
            }
        }

        toolbar?.setNavigationOnClickListener { viewModel.onClickBackArrow() }
        toolbar?.setOnMenuItemClickListener(this)
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

    override fun setupRecycler(iInputControl: IInputControl) {
        val touchCallback = RollTouchControl(viewModel)

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

    override fun showToolbarVisibleIcon(isShow: Boolean) {
        visibleMenuItem?.isVisible = isShow
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

    override fun setToolbarVisibleIcon(isVisible: Boolean, needAnim: Boolean) {
        visibleMenuItem?.title = getString(if (isVisible) {
            R.string.menu_roll_visible
        } else {
            R.string.menu_roll_invisible
        })

        visibleIconControl?.setDrawable(isVisible, needAnim)
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
        operator fun get(id: Long, @Color color: Int) = RollNoteFragment().apply {
            arguments = Bundle().apply {
                putLong(NoteData.Intent.ID, id)
                putInt(NoteData.Intent.COLOR, color)
            }
        }
    }

}