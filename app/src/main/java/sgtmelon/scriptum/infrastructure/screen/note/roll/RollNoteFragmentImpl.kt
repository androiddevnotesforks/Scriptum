package sgtmelon.scriptum.infrastructure.screen.note.roll

import android.content.Context
import android.text.InputType
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import javax.inject.Inject
import sgtmelon.extensions.removeExtraSpace
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.iconanim.callback.IconChangeCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.bindBoolTint
import sgtmelon.scriptum.cleanup.extension.createVisibleAnim
import sgtmelon.scriptum.cleanup.extension.requestSelectionFocus
import sgtmelon.scriptum.cleanup.presentation.adapter.RollAdapter
import sgtmelon.scriptum.cleanup.presentation.control.note.save.NoteSave
import sgtmelon.scriptum.cleanup.presentation.control.touch.RollTouchControl
import sgtmelon.scriptum.databinding.FragmentRollNoteBinding
import sgtmelon.scriptum.databinding.IncNotePanelContentBinding
import sgtmelon.scriptum.databinding.IncToolbarNoteBinding
import sgtmelon.scriptum.infrastructure.adapter.holder.RollReadHolder
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteFragmentImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.getItem
import sgtmelon.scriptum.infrastructure.utils.extensions.hideKeyboard
import sgtmelon.scriptum.infrastructure.utils.extensions.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf
import sgtmelon.scriptum.infrastructure.utils.extensions.note.getCheckCount
import sgtmelon.scriptum.infrastructure.utils.extensions.setEditorDoneAction
import sgtmelon.scriptum.infrastructure.utils.icons.VisibleFilterIcon
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener

/**
 * Fragment for display roll note.
 */
class RollNoteFragmentImpl : ParentNoteFragmentImpl<NoteItem.Roll, FragmentRollNoteBinding>(),
    RollNoteFragment,
    IconBlockCallback,
    RollTouchControl.Callback {

    // TODO pass data for pre-binding: visible state

    override val layoutId: Int = R.layout.fragment_roll_note
    override val type: NoteType = NoteType.ROLL

    @Inject override lateinit var viewModel: RollNoteViewModel
    @Inject override lateinit var noteSave: NoteSave

    override val appBar: IncToolbarNoteBinding? get() = binding?.appBar
    override val panelBar: IncNotePanelContentBinding? get() = binding?.panel?.content

    private var visibleIcon: IconChangeCallback? = null
    private val visibleMenuItem: MenuItem?
        get() = appBar?.content?.toolbar?.getItem(R.id.item_visible)

    override fun setupToolbar(context: Context, toolbar: Toolbar?) {
        super.setupToolbar(context, toolbar)

        toolbar?.inflateMenu(R.menu.fragment_roll_note)
        toolbar?.setOnMenuItemClickListener {
            open.attempt(withSwitch = false) { viewModel.changeVisible() }
            return@setOnMenuItemClickListener true
        }

        /** Call after menu inflating because otherwise visible icon will be null */
        visibleIcon = VisibleFilterIcon(context, visibleMenuItem, callback = this)
    }

    override fun focusOnEnter() {
        binding?.addPanel?.rollEnter?.requestSelectionFocus()
    }

    override fun setupPanel() {
        super.setupPanel()

        binding?.addPanel?.rollEnter?.apply {
            setRawInputType(
                InputType.TYPE_CLASS_TEXT
                        or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                        or InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
                        or InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
            )
            imeOptions = EditorInfo.IME_ACTION_DONE or EditorInfo.IME_FLAG_NO_FULLSCREEN

            doOnTextChanged { _, _, _, _ ->
                val isAddAvailable = getAddText().isNotEmpty()

                binding?.addPanel?.addButton?.apply {
                    isEnabled = isAddAvailable
                    bindBoolTint(isAddAvailable, R.attr.clAccent, R.attr.clDisable)
                }
            }

            setEditorDoneAction {
                val addText = getAddText()
                if (addText.isEmpty()) {
                    viewModel.save(changeMode = true)
                } else {
                    binding?.addPanel?.rollEnter?.setText("")
                    addItem(toBottom = true, addText)
                }
            }
        }

        binding?.addPanel?.addButton?.apply {
            setOnClickListener { addItem(toBottom = true, getAddText()) }
            setOnLongClickListener {
                addItem(toBottom = false, getAddText())
                return@setOnLongClickListener true
            }
        }
    }

    private fun addItem(toBottom: Boolean, text: String) {
        if (viewModel.isEditMode && !isSomethingOpened) viewModel.addItem(toBottom, text)
    }

    override fun setupContent() {
        super.setupContent()

        binding?.recyclerView?.let {
            it.addOnScrollListener(RecyclerOverScrollListener(showFooter = false))
            it.setHasFixedSize(true)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }

        ItemTouchHelper(touchCallback).attachToRecyclerView(binding?.recyclerView)
    }

    //region Observable staff

    override fun observeDataReady(it: Boolean) {
        super.observeDataReady(it)
        binding?.recyclerView?.makeVisibleIf(it) { makeInvisible() }
        visibleMenuItem?.isVisible = it
        visibleMenuItem?.isEnabled = it
    }

    override fun observeEdit(previousEdit: Boolean, isEdit: Boolean) {
        super.observeEdit(previousEdit, isEdit)
        adapter.updateEdit(isEdit)
    }

    override fun observeState(previousState: NoteState, state: NoteState) {
        super.observeState(previousState, state)
        adapter.updateState(state)
    }

    override fun observeNoteItem(item: NoteItem.Roll) {
        super.observeNoteItem(item)

        binding?.doneProgress?.max = item.list.size
        binding?.doneProgress?.setProgress(item.list.getCheckCount(), true)

        val isVisible = item.isVisible
        val titleId = if (isVisible) R.string.menu_roll_visible else R.string.menu_roll_invisible
        visibleMenuItem?.title = getString(titleId)

        /** Skip animation on first icon setup. */
        visibleIcon?.setDrawable(isVisible, needAnim = visibleIcon?.isEnterIcon != null)
    }

    override fun invalidatePanelState(isEdit: Boolean) {
        super.invalidatePanelState(isEdit)

        binding?.addPanel?.parentContainer?.makeVisibleIf(isEdit)
        binding?.doneProgress?.makeVisibleIf(!isEdit)
        binding?.panel?.dividerView?.makeVisibleIf(isEdit)
    }

    //endregion

    private fun getAddText(): String {
        return binding?.addPanel?.rollEnter?.text?.toString()?.removeExtraSpace() ?: ""
    }

    //region Cleanup

    private val touchCallback by lazy { RollTouchControl(callback = this) }
    private val adapter: RollAdapter by lazy {
        val readCallback = object : RollReadHolder.Callback {
            override fun onReadCheckClick(p: Int, animTime: Long, action: () -> Unit) {
                // TODO зачем это условие? (опиши комментарием)
                if (!open.isChangeEnabled && open.tag == OpenState.Tag.ANIM) {
                    open.isChangeEnabled = true
                }

                open.attempt(OpenState.Tag.ANIM) {
                    open.block(animTime)
                    open.tag = OpenState.Tag.ANIM
                    action()
                    viewModel.changeItemCheck(p)
                }
            }
        }

        return@lazy with(connector.init) {
            RollAdapter(isEdit, state, touchCallback, viewModel, readCallback) {
                focusOnEnter()
            }
        }
    }
    private val layoutManager by lazy { LinearLayoutManager(activity) }

    // TODO check how it will work with rotation end other staff
    override fun inject(component: ScriptumComponent) {
        component.getRollNoteBuilder()
            .set(fragment = this)
            .set(lifecycle)
            .set(callback = this)
            .set(connector.init)
            .build()
            .inject(fragment = this)
    }

    override fun onBindingInfo(isListEmpty: Boolean, isListHide: Boolean) {
        binding?.apply {
            this.isListEmpty = isListEmpty
            this.isListHide = isListHide
        }?.executePendingBindings()
    }


    // TODO remove this way of animation
    override fun animateInfoVisible(isVisible: Boolean?) {
        val isListEmpty = adapter.itemCount == 0

        binding?.parentContainer?.createVisibleAnim(
            binding?.emptyInfo?.root,
            isVisible = isVisible ?: isListEmpty
        )
    }

    override fun changeName(text: String, cursor: Int) {
        appBar?.content?.nameEnter?.apply {
            requestFocus()
            setText(text)
            setSelection(cursor)
        }
    }


    override fun scrollToItem(toBottom: Boolean, p: Int, list: MutableList<RollItem>) {
        val smoothInsert = with(layoutManager) {
            if (adapter.itemCount == 0) return@with true

            return@with if (toBottom) {
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
        if (cursor != null) adapter.cursor = cursor

        adapter.setList(list).notifyItemChanged(p)
    }

    override fun notifyItemMoved(list: List<RollItem>, from: Int, to: Int) {
        adapter.setList(list).notifyItemMoved(from, to)
    }

    override fun notifyItemInserted(list: List<RollItem>, p: Int, cursor: Int?) {
        if (cursor != null) adapter.cursor = cursor

        adapter.setList(list).notifyItemInserted(p)
    }

    override fun notifyItemRemoved(list: List<RollItem>, p: Int) {
        adapter.setList(list).notifyItemRemoved(p)
    }

    //endregion

    override fun onTouchAction(inAction: Boolean) {
        open.isBlocked = inAction
    }

    override fun onTouchGetDrag(isDragAvailable: Boolean): Boolean {
        return viewModel.isEditMode && isDragAvailable
    }

    override fun onTouchGetSwipe(): Boolean = viewModel.isEditMode

    override fun onTouchDragStart() = hideKeyboard()

    /**
     * All item positions updates after call [save], because it's hard
     * to control in Edit.
     */
    override fun onTouchSwiped(p: Int) {
        TODO()
        //        val absolutePosition = getAbsolutePosition(p) ?: return
        //        val item = deprecatedNoteItem.list.removeAtOrNull(absolutePosition) ?: return
        //
        //        history.add(HistoryAction.Roll.List.Remove(absolutePosition, item))
        //        historyAvailable.postValue(history.available)
        //
        //        callback.notifyItemRemoved(getAdapterList(), p)
    }

    /**
     * All item positions updates after call [save], because it's hard
     * to control in Edit.
     */
    override fun onTouchMove(from: Int, to: Int): Boolean {
        TODO()
        //        val absoluteFrom = getAbsolutePosition(from) ?: return true
        //        val absoluteTo = getAbsolutePosition(to) ?: return true
        //
        //        deprecatedNoteItem.list.move(absoluteFrom, absoluteTo)
        //
        //        callback.notifyItemMoved(getAdapterList(), from, to)
        //        callback.hideKeyboardDepr()
        //
        //        return true
    }

    override fun onTouchClear(position: Int) {
        TODO()
        //        val absolute = getAbsolutePosition(position) ?: return
        //        callback.notifyItemChanged(getAdapterList(), absolute)
    }

    override fun onTouchMoveResult(from: Int, to: Int) {
        TODO()
        //        val absoluteFrom = getAbsolutePosition(from) ?: return
        //        val absoluteTo = getAbsolutePosition(to) ?: return
        //
        //        history.add(HistoryAction.Roll.Move(HistoryChange(absoluteFrom, absoluteTo)))
        //        historyAvailable.postValue(history.available)
    }
}