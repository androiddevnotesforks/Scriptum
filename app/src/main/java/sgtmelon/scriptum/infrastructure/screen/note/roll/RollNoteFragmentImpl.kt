package sgtmelon.scriptum.infrastructure.screen.note.roll

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar
import javax.inject.Inject
import sgtmelon.extensions.removeExtraSpace
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.iconanim.callback.IconChangeCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.annotation.InputAction
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.addOnNextAction
import sgtmelon.scriptum.cleanup.extension.createVisibleAnim
import sgtmelon.scriptum.cleanup.extension.requestFocusOnVisible
import sgtmelon.scriptum.cleanup.extension.requestSelectionFocus
import sgtmelon.scriptum.cleanup.presentation.adapter.RollAdapter
import sgtmelon.scriptum.cleanup.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.cleanup.presentation.control.note.input.InputControl
import sgtmelon.scriptum.cleanup.presentation.control.note.input.watcher.InputTextWatcher
import sgtmelon.scriptum.cleanup.presentation.control.touch.RollTouchControl
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener
import sgtmelon.scriptum.databinding.FragmentRollNoteBinding
import sgtmelon.scriptum.databinding.IncToolbarNoteBinding
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity
import sgtmelon.scriptum.infrastructure.screen.note.NoteMenu
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteFragmentImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.hideKeyboard
import sgtmelon.scriptum.infrastructure.utils.icons.VisibleFilterIcon
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener

/**
 * Fragment for display roll note.
 */
class RollNoteFragmentImpl : ParentNoteFragmentImpl<NoteItem.Roll, FragmentRollNoteBinding>(),
    RollNoteFragment,
    Toolbar.OnMenuItemClickListener,
    IconBlockCallback {

    override val layoutId: Int = R.layout.fragment_roll_note
    override val type: NoteType = NoteType.ROLL

    @Inject override lateinit var viewModel: RollNoteViewModel

    override val appBar: IncToolbarNoteBinding? get() = binding?.appBar

    // TODO plan

    override fun setupBinding(callback: NoteMenu) {
        binding?.menuCallback = callback
    }

    override fun setupToolbar(context: Context, toolbar: Toolbar?, colorIndicator: View?) {
        super.setupToolbar(context, toolbar, colorIndicator)

        toolbar?.inflateMenu(R.menu.fragment_roll_note)
        toolbar?.setOnMenuItemClickListener(this)

        /** Call after menu inflating because otherwise visible icon will be null */
        visibleIconControl = VisibleFilterIcon(context, visibleMenuItem, callback = this)
    }

    //region Cleanup

    private var visibleIconControl: IconChangeCallback? = null

    private val animTime by lazy {
        context?.resources?.getInteger(R.integer.icon_animation_time)?.toLong() ?: 0L
    }

    private val touchCallback by lazy { RollTouchControl(viewModel) }

    private val adapter: RollAdapter by lazy {
        RollAdapter(touchCallback, viewModel, object : ItemListener.ActionClick {
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

    private val nameEnter: EditText? get() = appBar?.content?.nameEnter

    private val visibleMenuItem: MenuItem?
        get() = appBar?.content?.toolbar?.menu?.findItem(R.id.item_visible)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onSetup(bundle = arguments ?: savedInstanceState)
    }

    // TODO not save way to finish activity (view model is lateinit value)
    override fun inject(component: ScriptumComponent) {
        val bundleProvider = (activity as? NoteActivity)?.bundleProvider
        val (isEdit, noteState) = bundleProvider?.state ?: return run { activity?.finish() }
        val (id, _, color) = bundleProvider.data

        if (id == null || color == null) {
            activity?.finish()
            return
        }

        component.getRollNoteBuilder()
            .set(fragment = this)
            .set(isEdit)
            .set(noteState)
            .set(id)
            .set(color)
            .build()
            .inject(fragment = this)
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

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        open.attempt(withSwitch = false) { viewModel.onClickVisible() }
        return true
    }

    override val isDialogOpen: Boolean get() = open.isBlocked

    override fun setTouchAction(inAction: Boolean) {
        open.isBlocked = inAction
    }

    override fun hideKeyboard() {
        activity?.hideKeyboard()
    }

    override fun setupEnter(inputControl: IInputControl) {
        binding?.appBar?.content?.scrollView?.requestFocusOnVisible(nameEnter)

        nameEnter?.let {
            it.addTextChangedListener(
                InputTextWatcher(it, InputAction.NAME, viewModel, inputControl)
            )

            it.addOnNextAction { onFocusEnter() }
        }

        binding?.addPanel?.rollEnter?.apply {
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

        binding?.addPanel?.addButton?.apply {
            setOnClickListener { viewModel.onClickAdd(simpleClick = true) }
            setOnLongClickListener {
                viewModel.onClickAdd(simpleClick = false)
                return@setOnLongClickListener true
            }
        }
    }

    override fun setupRecycler(inputControl: IInputControl) {
        adapter.apply {
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

        binding?.recyclerView?.let {
            it.addOnScrollListener(RecyclerOverScrollListener(showFooter = false))
            it.setHasFixedSize(true)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }

        ItemTouchHelper(touchCallback).attachToRecyclerView(binding?.recyclerView)
    }

    override fun showToolbarVisibleIcon(isShow: Boolean) {
        visibleMenuItem?.isVisible = isShow
    }


    override fun onBindingLoad(isRankEmpty: Boolean) {
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
        binding?.isEnterEmpty = getEnterText().removeExtraSpace().isEmpty()
        binding?.executePendingBindings()
    }


    override fun onBindingInput(item: NoteItem.Roll, inputAccess: InputControl.Access) {
        binding?.apply {
            this.item = item
            this.inputAccess = inputAccess
        }?.executePendingBindings()
    }


    override fun onPressBack() = viewModel.onPressBack()

    override fun setToolbarVisibleIcon(isVisible: Boolean, needAnim: Boolean) {
        visibleMenuItem?.title = getString(if (isVisible) {
            R.string.menu_roll_visible
        } else {
            R.string.menu_roll_invisible
        })

        visibleIconControl?.setDrawable(isVisible, needAnim)
    }


    // TODO remove this way of animation
    override fun animateInfoVisible(isVisible: Boolean?) {
        val isListEmpty = adapter.itemCount == 0

        binding?.parentContainer?.createVisibleAnim(
            binding?.emptyInfo?.root,
            isVisible = isVisible ?: isListEmpty
        )
    }

    override fun focusOnEdit(isCreate: Boolean) {
        view?.post {
            if (isCreate) {
                nameEnter?.requestSelectionFocus()
            } else {
                binding?.addPanel?.rollEnter?.requestSelectionFocus()
            }
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
        binding?.addPanel?.rollEnter?.apply {
            requestFocus()
            setSelection(text.toString().length)
        }
    }

    override fun getEnterText() = binding?.addPanel?.rollEnter?.text?.toString() ?: ""

    override fun clearEnterText() {
        binding?.addPanel?.rollEnter?.setText("")
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
            binding?.recyclerView?.scrollToPosition(p)
            adapter.setList(list).notifyItemInserted(p)
        } else {
            binding?.recyclerView?.smoothScrollToPosition(p)
            adapter.setList(list)
            binding?.recyclerView?.post { adapter.notifyDataSetChanged() }
        }
    }

    override fun updateNoteState(isEdit: Boolean, noteState: NoteState?) {
        adapter.isEdit = isEdit
        adapter.noteState = noteState
        binding?.recyclerView?.post { adapter.notifyDataSetChanged() }
    }

    override fun updateProgress(progress: Int, max: Int) {
        binding?.doneProgress?.max = max
        binding?.doneProgress?.setProgress(progress, true)
    }

    override fun setList(list: List<RollItem>) {
        adapter.setList(list)
    }

    override fun notifyDataSetChanged(list: List<RollItem>) {
        adapter.setList(list)
        binding?.recyclerView?.post { adapter.notifyDataSetChanged() }
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


    override fun showSaveToast(isSuccess: Boolean) {
        val text = if (isSuccess) R.string.toast_note_save_done else R.string.toast_note_save_error
        system.toast.show(context, text)
    }

    override fun finish() {
        activity?.finish()
    }

    //region Broadcast functions

    override fun sendSetAlarmBroadcast(id: Long, calendar: Calendar, showToast: Boolean) {
        system.broadcast.sendSetAlarm(id, calendar, showToast)
    }

    override fun sendCancelAlarmBroadcast(id: Long) = system.broadcast.sendCancelAlarm(id)

    override fun sendNotifyNotesBroadcast() = system.broadcast.sendNotifyNotesBind()

    override fun sendCancelNoteBroadcast(id: Long) = system.broadcast.sendCancelNoteBind(id)

    override fun sendNotifyInfoBroadcast(count: Int?) {
        system.broadcast.sendNotifyInfoBind(count)
    }

    //endregion

    //endregion

}