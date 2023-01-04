package sgtmelon.scriptum.infrastructure.screen.note.roll

import android.content.Context
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
import javax.inject.Inject
import sgtmelon.extensions.removeExtraSpace
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.iconanim.callback.IconChangeCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.addOnNextAction
import sgtmelon.scriptum.cleanup.extension.bindBoolTint
import sgtmelon.scriptum.cleanup.extension.createVisibleAnim
import sgtmelon.scriptum.cleanup.extension.requestFocusOnVisible
import sgtmelon.scriptum.cleanup.extension.requestSelectionFocus
import sgtmelon.scriptum.cleanup.presentation.adapter.RollAdapter
import sgtmelon.scriptum.cleanup.presentation.control.touch.RollTouchControl
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener
import sgtmelon.scriptum.data.noteHistory.HistoryAction
import sgtmelon.scriptum.data.noteHistory.NoteHistory
import sgtmelon.scriptum.databinding.FragmentRollNoteBinding
import sgtmelon.scriptum.databinding.IncNotePanelContentBinding
import sgtmelon.scriptum.databinding.IncToolbarNoteBinding
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.noteHistory.HistoryTextWatcher
import sgtmelon.scriptum.infrastructure.screen.note.parent.ParentNoteFragmentImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf
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
    override val panelBar: IncNotePanelContentBinding? get() = binding?.panel?.content

    private var visibleIcon: IconChangeCallback? = null

    // TODO pass data for pre-binding: visible state

    override fun setupToolbar(context: Context, toolbar: Toolbar?) {
        super.setupToolbar(context, toolbar)

        toolbar?.inflateMenu(R.menu.fragment_roll_note)
        toolbar?.setOnMenuItemClickListener(this)

        /** Call after menu inflating because otherwise visible icon will be null */
        visibleIcon = VisibleFilterIcon(context, visibleMenuItem, callback = this)
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

            doOnTextChanged { it, _, _, _ ->
                val isAddAvailable = it?.toString()?.removeExtraSpace()?.isNotEmpty()
                    ?: return@doOnTextChanged

                binding?.addPanel?.addButton?.apply {
                    isEnabled = isAddAvailable
                    bindBoolTint(isAddAvailable, R.attr.clAccent, R.attr.clDisable)
                }
            }

            setOnEditorActionListener { _, i, _ -> viewModel.onEditorClick(i) }
        }

        binding?.addPanel?.addButton?.apply {
            setOnClickListener { viewModel.onClickAdd(toBottom = true) }
            setOnLongClickListener {
                viewModel.onClickAdd(toBottom = false)
                return@setOnLongClickListener true
            }
        }
    }

    override fun invalidatePanelState(isEdit: Boolean) {
        super.invalidatePanelState(isEdit)

        binding?.addPanel?.parentContainer?.makeVisibleIf(isEdit)
        binding?.doneProgress?.makeVisibleIf(!isEdit)
        binding?.panel?.dividerView?.makeVisibleIf(isEdit)
    }

    //region Cleanup

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

    //    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    //        super.onViewCreated(view, savedInstanceState)
    //        viewModel.onSetup(bundle = arguments ?: savedInstanceState)
    //    }

    // TODO check how it will work with rotation end other staff
    override fun inject(component: ScriptumComponent) {
        component.getRollNoteBuilder()
            .set(fragment = this)
            .set(connector.init)
            .build()
            .inject(fragment = this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        open.attempt(withSwitch = false) { viewModel.onClickVisible() }
        return true
    }

    override fun setTouchAction(inAction: Boolean) {
        open.isBlocked = inAction
    }

    override fun setupEnter(history: NoteHistory) {
        binding?.appBar?.content?.scrollView?.requestFocusOnVisible(nameEnter)

        nameEnter?.let {
            // TODO move to parent class
            it.addTextChangedListener(HistoryTextWatcher(it, viewModel) { value, cursor ->
                history.add(HistoryAction.Name(value, cursor))
            })

            it.addOnNextAction { onFocusEnter() }
        }

        //        binding?.addPanel?.rollEnter?.apply {
        //            setRawInputType(
        //                InputType.TYPE_CLASS_TEXT
        //                        or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        //                        or InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
        //                        or InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
        //            )
        //            imeOptions = EditorInfo.IME_ACTION_DONE or EditorInfo.IME_FLAG_NO_FULLSCREEN
        //
        //            doOnTextChanged { _, _, _, _ -> onBindingEnter() }
        //            setOnEditorActionListener { _, i, _ -> viewModel.onEditorClick(i) }
        //        }
        //
        //        binding?.addPanel?.addButton?.apply {
        //            setOnClickListener { viewModel.onClickAdd(toBottom = true) }
        //            setOnLongClickListener {
        //                viewModel.onClickAdd(toBottom = false)
        //                return@setOnLongClickListener true
        //            }
        //        }
    }

    override fun setupRecycler(history: NoteHistory) {
        adapter.apply {
            this.history = history

            registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                /** Update before animation ends. */
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

    //    override fun showToolbarVisibleIcon(isShow: Boolean) {
    //        visibleMenuItem?.isVisible = isShow
    //    }


    override fun onBindingLoad() {
        binding?.apply { this.isDataLoad = true }?.executePendingBindings()
    }

    override fun onBindingInfo(isListEmpty: Boolean, isListHide: Boolean) {
        binding?.apply {
            this.isListEmpty = isListEmpty
            this.isListHide = isListHide
        }?.executePendingBindings()
    }

    override fun onBindingEdit(item: NoteItem.Roll, isEditMode: Boolean) {
        binding?.apply {
            TODO()
            //            this.item = item
            //            this.isEditMode = isEditMode
        }

        // TODO зачем тут нужно было обновлять поле ввода? мб при первом включении
        //        onBindingEnter()
    }

    override fun onBindingNote(item: NoteItem.Roll) {
        TODO()
        //        binding?.apply { this.item = item }?.executePendingBindings()
    }
    //
    //    override fun onBindingEnter() {
    ////        binding?.isEnterEmpty = getEnterText().removeExtraSpace().isEmpty()
    ////        binding?.executePendingBindings()
    //    }


    //    override fun onBindingInput(
    //        item: NoteItem.Roll,
    //        historyMove: HistoryMoveAvailable
    //    ) {
    //        binding?.apply {
    //            TODO()
    //            //            this.item = item
    //            this.historyMove = historyMove
    //        }?.executePendingBindings()
    //    }


    override fun setToolbarVisibleIcon(isVisible: Boolean, needAnim: Boolean) {
        visibleMenuItem?.title = getString(
            if (isVisible) {
                R.string.menu_roll_visible
            } else {
                R.string.menu_roll_invisible
            }
        )

        visibleIcon?.setDrawable(isVisible, needAnim)
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

    override fun updateNoteState(isEdit: Boolean, state: NoteState?) {
        adapter.isEdit = isEdit
        adapter.state = state
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

    //endregion

}