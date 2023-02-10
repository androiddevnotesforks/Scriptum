package sgtmelon.scriptum.infrastructure.screen.note.roll

import android.content.Context
import android.text.InputType
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import sgtmelon.scriptum.cleanup.presentation.adapter.RollAdapter
import sgtmelon.scriptum.databinding.FragmentRollNoteBinding
import sgtmelon.scriptum.databinding.IncNotePanelContentBinding
import sgtmelon.scriptum.databinding.IncToolbarNoteBinding
import sgtmelon.scriptum.domain.model.result.HistoryResult
import sgtmelon.scriptum.infrastructure.adapter.holder.RollReadHolder
import sgtmelon.scriptum.infrastructure.adapter.touch.DragAndSwipeTouchHelper
import sgtmelon.scriptum.infrastructure.animation.ShowListAnimation
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteFragmentImpl
import sgtmelon.scriptum.infrastructure.screen.note.save.NoteSave
import sgtmelon.scriptum.infrastructure.screen.parent.list.CustomListNotifyUi
import sgtmelon.scriptum.infrastructure.utils.extensions.clearText
import sgtmelon.scriptum.infrastructure.utils.extensions.getItem
import sgtmelon.scriptum.infrastructure.utils.extensions.hideKeyboard
import sgtmelon.scriptum.infrastructure.utils.extensions.isTrue
import sgtmelon.scriptum.infrastructure.utils.extensions.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf
import sgtmelon.scriptum.infrastructure.utils.extensions.note.getCheckCount
import sgtmelon.scriptum.infrastructure.utils.extensions.requestFocusWithCursor
import sgtmelon.scriptum.infrastructure.utils.extensions.setEditorDoneAction
import sgtmelon.scriptum.infrastructure.utils.icons.VisibleFilterIcon
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener

/**
 * Fragment for display roll note.
 */
class RollNoteFragmentImpl : ParentNoteFragmentImpl<NoteItem.Roll, FragmentRollNoteBinding>(),
    CustomListNotifyUi<RollItem>,
    RollNoteFragment,
    IconBlockCallback,
    DragAndSwipeTouchHelper.Callback {

    // TODO pass data for pre-binding: visible state

    // TODO FIX:
    // 1. After filling note -> save button don't change enable state (only after rotation)
    // 2. Create note - cursor not on name (it's placed on enter)

    override val layoutId: Int = R.layout.fragment_roll_note
    override val type: NoteType = NoteType.ROLL

    @Inject override lateinit var viewModel: RollNoteViewModel
    @Inject override lateinit var noteSave: NoteSave

    override val appBar: IncToolbarNoteBinding? get() = binding?.appBar
    override val panelBar: IncNotePanelContentBinding? get() = binding?.panel?.content

    private val listAnimation = ShowListAnimation()

    private var visibleIcon: IconChangeCallback? = null
    private val visibleMenuItem: MenuItem?
        get() = appBar?.content?.toolbar?.getItem(R.id.item_visible)

    override fun inject(component: ScriptumComponent) {
        component.getRollNoteBuilder()
            .set(owner = this)
            .set(fragment = this)
            .set(lifecycle)
            .set(noteSaveCallback)
            .set(connector.init)
            .build()
            .inject(fragment = this)
    }

    //region Setup functions

    override fun setupToolbar(context: Context, toolbar: Toolbar?) {
        super.setupToolbar(context, toolbar)

        toolbar?.inflateMenu(R.menu.fragment_roll_note)
        toolbar?.setOnMenuItemClickListener {
            open.attempt(withSwitch = false) { viewModel.changeVisible() }
            return@setOnMenuItemClickListener true
        }

        /** Call after menu inflating because otherwise visible icon will be null. */
        visibleIcon = VisibleFilterIcon(context, visibleMenuItem, callback = this)
    }

    override fun focusOnEnter() {
        binding?.addPanel?.rollEnter?.requestFocusWithCursor(binding)
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
                if (open.isBlocked) return@setEditorDoneAction

                val addText = getAddText()
                if (addText.isEmpty()) {
                    viewModel.save(changeMode = true)
                } else {
                    binding?.addPanel?.rollEnter?.clearText()
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
        if (viewModel.isEditMode) open.ifNotBlocked { viewModel.addItem(toBottom, text) }
    }

    override fun setupContent() {
        super.setupContent()

        binding?.recyclerView?.let {
            it.addOnScrollListener(RecyclerOverScrollListener(showFooter = false))
            it.setHasFixedSize(true)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }

        ItemTouchHelper(touchHelper).attachToRecyclerView(binding?.recyclerView)
    }

    //endregion

    override fun collectUndoRedo(result: HistoryResult) {
        when (result) {
            is HistoryResult.Name -> onHistoryName(result)
            else -> TODO()
        }
    }

    private fun getAddText(): String {
        return binding?.addPanel?.rollEnter?.text?.toString()?.removeExtraSpace() ?: ""
    }

    //region Observable staff

    override fun setupObservers() {
        super.setupObservers()

        viewModel.showList.observe(this) {
            val binding = binding ?: return@observe
            listAnimation.startFade(
                it, binding.parentContainer, binding.progressBar,
                binding.recyclerView, binding.emptyInfo.parentContainer
            )
        }
        viewModel.itemList.observe(this) { catchListUpdate(it) }
    }

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

    /**
     * Take list size from [viewModel], because there are maybe a hide state (hide done items).
     */
    override fun isContentEmpty(): Boolean = viewModel.noteItem.value?.list?.isEmpty().isTrue()

    //region Cleanup

    private val touchHelper by lazy { DragAndSwipeTouchHelper(callback = this) }
    override val adapter: RollAdapter by lazy {
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
            RollAdapter(isEdit, state, touchHelper, viewModel, readCallback) {
                focusOnEnter()
            }
        }
    }
    override val layoutManager by lazy { LinearLayoutManager(activity) }
    override val recyclerView: RecyclerView? get() = binding?.recyclerView

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

    //region Touch callback

    override fun onTouchAction(inAction: Boolean) {
        open.isBlocked = inAction
    }

    override fun onTouchGetDrag(): Boolean = viewModel.isEditMode

    override fun onTouchGetSwipe(): Boolean = viewModel.isEditMode

    override fun onTouchSwiped(position: Int) = viewModel.swipeItem(position)

    override fun onTouchMoveStarts() = hideKeyboard()

    override fun onTouchMove(from: Int, to: Int): Boolean {
        /** I know it was closed inside [onTouchMoveStarts], but it's for sure. */
        hideKeyboard()

        return viewModel.moveItem(from, to)
    }

    override fun onTouchMoveResult(from: Int, to: Int) = viewModel.moveItemResult(from, to)

    override fun onTouchClear(position: Int) = viewModel.releaseItem(position)

    //endregion

}