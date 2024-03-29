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
import sgtmelon.extensions.emptyString
import sgtmelon.extensions.getDimen
import sgtmelon.extensions.removeExtraSpace
import sgtmelon.iconanim.callback.IconChangeCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.bindBoolTint
import sgtmelon.scriptum.databinding.FragmentRollNoteBinding
import sgtmelon.scriptum.databinding.IncNotePanelBinding
import sgtmelon.scriptum.databinding.IncToolbarNoteBinding
import sgtmelon.scriptum.domain.model.result.HistoryResult
import sgtmelon.scriptum.infrastructure.adapter.RollContentAdapter
import sgtmelon.scriptum.infrastructure.adapter.holder.RollContentHolder
import sgtmelon.scriptum.infrastructure.adapter.touch.DragAndSwipeTouchHelper
import sgtmelon.scriptum.infrastructure.animation.ShowListAnimation
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteFragment
import sgtmelon.scriptum.infrastructure.screen.note.save.NoteSave
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListScreen
import sgtmelon.scriptum.infrastructure.screen.parent.permission.PermissionViewModel
import sgtmelon.scriptum.infrastructure.utils.extensions.clearText
import sgtmelon.scriptum.infrastructure.utils.extensions.disableChangeAnimations
import sgtmelon.scriptum.infrastructure.utils.extensions.getItem
import sgtmelon.scriptum.infrastructure.utils.extensions.hideKeyboard
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.updateMargin
import sgtmelon.scriptum.infrastructure.utils.extensions.isTrue
import sgtmelon.scriptum.infrastructure.utils.extensions.note.getCheckCount
import sgtmelon.scriptum.infrastructure.utils.extensions.requestFocusWithCursor
import sgtmelon.scriptum.infrastructure.utils.extensions.setEditorDoneAction
import sgtmelon.scriptum.infrastructure.utils.extensions.setOverscrollColor
import sgtmelon.scriptum.infrastructure.utils.icons.VisibleFilterIcon
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener
import javax.inject.Inject

/**
 * Fragment for display roll note.
 */
class RollNoteFragment : ParentNoteFragment<NoteItem.Roll, FragmentRollNoteBinding>(),
    ListScreen<RollItem>,
    DragAndSwipeTouchHelper.Callback {

    override val layoutId: Int = R.layout.fragment_roll_note
    override val type: NoteType = NoteType.ROLL

    @Inject override lateinit var viewModel: RollNoteViewModel
    @Inject override lateinit var permissionViewModel: PermissionViewModel
    @Inject override lateinit var noteSave: NoteSave

    override val appBar: IncToolbarNoteBinding? get() = binding?.appBar
    override val panelBar: IncNotePanelBinding? get() = binding?.panel

    private val animation by lazy { RollNoteAnimation(viewModel.isEditMode) }
    private val listAnimation = ShowListAnimation()

    private val touchHelper = DragAndSwipeTouchHelper(callback = this)
    override val adapter: RollContentAdapter by lazy {
        val readCallback = object : RollContentHolder.ReadCallback {
            override fun onRollChangeCheck(p: Int, animTime: Long, action: () -> Unit) {
                /** This 'if' needed for fast click on many checkBoxes at one time. */
                if (!open.isChangeEnabled && open.tag == OpenState.Tag.ANIM) {
                    open.isChangeEnabled = true
                }

                /** Tag needed here to protect from cross click: check box and visible icon */
                open.attempt(OpenState.Tag.ANIM) {
                    open.block(animTime)
                    open.tag = OpenState.Tag.ANIM

                    action()
                    viewModel.changeItemCheck(p)
                }
            }
        }

        return@lazy with(connector.init) {
            RollContentAdapter(isEdit, state, readCallback, viewModel, touchHelper) {
                focusOnEnter()
            }
        }
    }
    override val layoutManager by lazy { LinearLayoutManager(activity) }
    override val recyclerView: RecyclerView? get() = binding?.recyclerView

    private var visibleIcon: IconChangeCallback? = null
    private val visibleMenuItem: MenuItem?
        get() = appBar?.content?.toolbar?.getItem(R.id.item_visible)

    override fun inject(component: ScriptumComponent) {
        component.getRollNoteBuilder()
            .set(owner = this)
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

        panelBar?.dividerView?.apply {
            /** Setup small horizontal spaces for edit mode, in read - divider will be hided. */
            val margin = resources.getDimen(R.dimen.layout_8dp)
            updateMargin(left = margin, right = margin)
        }

        /** Invalidate during setup needed in rotation case - enter field may contain text. */
        invalidateAddButton()

        /** Needed if text field will become more than 2 lines (change size). */
        binding?.addPanel?.container?.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            animation.updateContainerMargin(binding)
        }

        binding?.addPanel?.rollEnter?.apply {
            setRawInputType(
                InputType.TYPE_CLASS_TEXT
                        or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                        or InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
                        or InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
            )
            imeOptions = EditorInfo.IME_ACTION_DONE or EditorInfo.IME_FLAG_NO_FULLSCREEN

            doOnTextChanged { _, _, _, _ -> invalidateAddButton() }

            setEditorDoneAction {
                if (open.isBlocked) return@setEditorDoneAction

                val addText = getAddText()
                if (addText.isEmpty()) {
                    viewModel.save(changeMode = true)
                } else {
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

    private fun invalidateAddButton() {
        val isAddAvailable = getAddText().isNotEmpty()

        binding?.addPanel?.addButton?.apply {
            isEnabled = isAddAvailable
            bindBoolTint(isAddAvailable, R.attr.clAccent, R.attr.clContentThird)
        }
    }

    private fun addItem(toBottom: Boolean, text: String) {
        if (viewModel.isReadMode) return

        open.ifNotBlocked {
            binding?.addPanel?.rollEnter?.clearText()
            viewModel.addItem(toBottom, text)
        }
    }

    override fun setupContent() {
        super.setupContent()

        binding?.recyclerView?.let {
            it.disableChangeAnimations()
            it.addOnScrollListener(RecyclerOverScrollListener())
            it.setHasFixedSize(false) /** The height of all items may be not the same. */
            it.layoutManager = layoutManager
            it.adapter = adapter
        }

        ItemTouchHelper(touchHelper).attachToRecyclerView(binding?.recyclerView)
    }

    //endregion

    override fun collectUndoRedo(result: HistoryResult) {
        when (result) {
            is HistoryResult.Name -> onHistoryName(result)
            is HistoryResult.Roll.Enter -> adapter.setCursor(result.cursor)
            is HistoryResult.Roll.Add -> adapter.setCursor(result.item.text.length)
            else -> return /** Other cases would not be updated in UI. */
        }
    }

    private fun getAddText(): String {
        return binding?.addPanel?.rollEnter?.text?.toString()?.removeExtraSpace() ?: emptyString()
    }

    //region Observable staff

    override fun setupObservers() {
        super.setupObservers()

        viewModel.list.show.observe(this) {
            val binding = binding ?: return@observe

            invalidateEmptyInfo()
            listAnimation.start(
                it, binding.parentContainer, binding.progressBar,
                binding.recyclerView, binding.emptyInfo.root
            )
        }
        viewModel.list.data.observe(this) { onListUpdate(it) }
    }

    private fun invalidateEmptyInfo() {
        val item = viewModel.noteItem.value ?: return

        val isListEmpty = item.list.isEmpty()
        /**
         * May simply get count from [adapter] (not from [RollNoteViewModel.list]), because
         * it will be before animation - it's mean all items already passed into [adapter].
         */
        val isListHide = !item.isVisible && !isListEmpty && adapter.itemCount == 0

        when {
            isListEmpty -> R.string.info_roll_empty_title to R.string.info_roll_empty_details
            isListHide -> R.string.info_roll_hide_title to R.string.info_roll_hide_details
            else -> null
        }?.also {
            binding?.emptyInfo?.titleText?.setText(it.first)
            binding?.emptyInfo?.detailsText?.setText(it.second)
        }
    }

    override fun observeNoteItem(item: NoteItem.Roll) {
        super.observeNoteItem(item)

        binding?.doneProgress?.setProgressSmooth(item.list.size, item.list.getCheckCount())

        val isVisible = item.isVisible
        visibleMenuItem?.setTitle(
            if (isVisible) R.string.menu_roll_visible else R.string.menu_roll_invisible
        )

        /** Skip animation on first icon setup. */
        visibleIcon?.setDrawable(isVisible, needAnim = visibleIcon?.isEnterIcon != null)
    }

    override fun observeState(previousState: NoteState, state: NoteState) {
        super.observeState(previousState, state)
        adapter.updateState(state)
    }

    override fun observeEdit(previousEdit: Boolean, isEdit: Boolean) {
        super.observeEdit(previousEdit, isEdit)
        adapter.updateEdit(isEdit)
    }

    override fun observeColor(color: Color) {
        super.observeColor(color)
        binding?.recyclerView?.setOverscrollColor(color)
    }

    override fun invalidatePanelState(isEdit: Boolean) {
        super.invalidatePanelState(isEdit)

        animation.startAddPanelChange(binding, isEdit)
    }

    //endregion

    /** Take list size from [viewModel], because there are maybe a hide state (hide done items). */
    override fun isContentEmpty(): Boolean = viewModel.noteItem.value?.list.isNullOrEmpty().isTrue()

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

        viewModel.moveItem(from, to)

        return true
    }

    override fun onTouchMoveResult(from: Int, to: Int) = viewModel.moveItemResult(from, to)

    //endregion

}