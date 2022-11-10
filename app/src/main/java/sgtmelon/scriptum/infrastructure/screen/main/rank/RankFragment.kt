package sgtmelon.scriptum.infrastructure.screen.main.rank

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import javax.inject.Inject
import sgtmelon.extensions.collect
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.extension.bindBoolTint
import sgtmelon.scriptum.cleanup.presentation.control.touch.RankTouchControl
import sgtmelon.scriptum.databinding.FragmentRankBinding
import sgtmelon.scriptum.infrastructure.adapter.RankAdapter
import sgtmelon.scriptum.infrastructure.adapter.callback.click.RankClickListener
import sgtmelon.scriptum.infrastructure.animation.ShowListAnimation
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.model.state.UpdateListState
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.main.callback.ScrollTopCallback
import sgtmelon.scriptum.infrastructure.screen.parent.BindingFragment
import sgtmelon.scriptum.infrastructure.system.delegators.SnackbarDelegator
import sgtmelon.scriptum.infrastructure.utils.hideKeyboard
import sgtmelon.scriptum.infrastructure.utils.setDefaultAnimator
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerInsertScroll
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener
import sgtmelon.test.idling.getIdling

/**
 * Screen with list of categories and with ability to create them.
 */
class RankFragment : BindingFragment<FragmentRankBinding>(),
    ScrollTopCallback,
    SnackbarDelegator.Callback {

    override val layoutId: Int = R.layout.fragment_rank

    @Inject lateinit var viewModel: RankViewModel

    private val animation = ShowListAnimation()

    private val unbindNoteReceiver by lazy { UnbindNoteReceiver[viewModel] }

    private val dialogs by lazy { DialogFactory.Main(context, fm) }
    private val renameDialog by lazy { dialogs.getRename() }

    private val touchControl by lazy { RankTouchControl(viewModel) }
    private val adapter by lazy {
        RankAdapter(touchControl, object : IconBlockCallback {
            override fun setEnabled(isEnabled: Boolean) {
                getIdling().change(!isEnabled, IdlingTag.Anim.ICON)

                parentOpen?.isBlocked = !isEnabled
                parentOpen?.tag = if (isEnabled) OpenState.Tag.ND else OpenState.Tag.ANIM
            }
        }, object : RankClickListener {
            override fun onRankVisibleClick(p: Int, onAction: () -> Unit) {
                changeVisibility(p, onAction)
            }

            override fun onRankClick(p: Int) = showRenameDialog(p)
            override fun onRankCancelClick(p: Int) = removeRank(p)
        })
    }
    private val layoutManager by lazy { LinearLayoutManager(context) }

    private val snackbar = SnackbarDelegator(
        R.string.snackbar_message_rank, R.string.snackbar_action_cancel, callback = this
    )

    val enterCard: View? get() = binding?.toolbarInclude?.enterCard

    //region System

    override fun inject(component: ScriptumComponent) {
        component.getRankBuilder()
            .set(owner = this)
            .build()
            .inject(fragment = this)
    }

    override fun setupView() {
        super.setupView()

        /**
         * Use [OpenState.attempt] inside add category feature, because calculations happens
         * inside coroutine, not main thread.
         *
         * TODO Reset of [OpenState.isBlocked] happen inside [scrollToItem].
         */
        binding?.toolbarInclude?.apply {
            toolbar.title = getString(R.string.title_rank)
            clearButton.setOnClickListener { clearEnter() }
            addButton.setOnClickListener {
                parentOpen?.attempt { addFromEnter(toBottom = true) }
            }
            addButton.setOnLongClickListener {
                parentOpen?.attempt { addFromEnter(toBottom = false) }
                return@setOnLongClickListener true
            }
            rankEnter.doOnTextChanged { _, _, _, _ -> notifyToolbar() }
            rankEnter.setOnEditorActionListener { _, i, _ ->
                return@setOnEditorActionListener if (i == EditorInfo.IME_ACTION_DONE) {
                    parentOpen?.attempt { addFromEnter(toBottom = true) }
                    true
                } else {
                    false
                }
            }
        }

        notifyToolbar()

        binding?.recyclerView?.let {
            it.setDefaultAnimator(supportsChangeAnimations = false) {
                // TODO check
                //                viewModel.onItemAnimationFinished()
            }

            it.addOnScrollListener(RecyclerOverScrollListener())
            it.setHasFixedSize(true)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }

        ItemTouchHelper(touchControl).attachToRecyclerView(binding?.recyclerView)
    }

    override fun setupDialogs() {
        super.setupDialogs()

        renameDialog.apply {
            onPositiveClick {
                viewModel.renameRank(position, name).collect(owner = this) { notifyToolbar() }
            }
            onDismiss { parentOpen?.clear() }
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.showList.observe(this) {
            val binding = binding ?: return@observe

            animation.startListFade(
                it, binding.parentContainer, binding.progressBar,
                binding.recyclerView, binding.infoInclude.parentContainer
            )

            /** If toolbar enter contains any text then need update add button. */
            notifyToolbar()
        }
        viewModel.itemList.observe(this) { observeItemList(it) }
        viewModel.showSnackbar.observe(this) { if (it) showSnackbar() }
    }

    override fun registerReceivers() {
        super.registerReceivers()
        context?.registerReceiver(unbindNoteReceiver, IntentFilter(ReceiverData.Filter.RANK))
    }

    override fun unregisterReceivers() {
        super.unregisterReceivers()
        context?.unregisterReceiver(unbindNoteReceiver)
    }

    override fun onResume() {
        super.onResume()

        /**
         * Lifecycle observer not working inside viewModel when changing pages. Check out custom
         * call of this function inside parent activity (during fragment transaction).
         */
        viewModel.updateData()

        // TODO restore snackbar after returning to this page (test case: click cance -> open notes page -> open rank page -> check snackbar is visible)
        // TODO restore snackbar after app reopen (свернул-открыл)
        /** Restore our snack bar if it must be shown. */
        if (viewModel.showSnackbar.value == true && !snackbar.isDisplayed) {
            showSnackbar()
        }
    }

    override fun onStop() {
        super.onStop()

        /**
         * Need dismiss without listener for control leave screen case. We don't want to lost
         * snackbar stack inside [viewModel] during rotation/app close/ect. So, need restore
         * [snackbar] after screen reopen - [onResume].
         */
        hideSnackbar()
    }

    /**
     * Use here [UpdateListState.NotifyHard] case, because it will prevent lags during
     * undo (insert) first item. When empty info hides and list appears. Insert animation
     * and list fade in animation concurrent with each other and it's looks laggy.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun observeItemList(it: List<RankItem>) {
        when (val state = viewModel.updateList) {
            is UpdateListState.Set -> adapter.setList(it)
            is UpdateListState.Notify -> adapter.notifyList(it)
            is UpdateListState.NotifyHard -> adapter.setList(it).notifyDataSetChanged()
            is UpdateListState.Remove -> adapter.setList(it).notifyItemRemoved(state.p)
            is UpdateListState.Insert -> {
                // TODO may be here need add open.clear?
                adapter.setList(it).notifyItemInserted(state.p)
                RecyclerInsertScroll(binding?.recyclerView, layoutManager).scroll(it, state.p)
            }
        }
    }

    //endregion

    private fun notifyToolbar() {
        val (isClearEnable, isAddEnable) = viewModel.getToolbarEnable(getEnterText())

        binding?.toolbarInclude?.apply {
            clearButton.isEnabled = isClearEnable
            clearButton.bindBoolTint(isClearEnable, R.attr.clContent, R.attr.clDisable)

            addButton.isEnabled = isAddEnable
            addButton.bindBoolTint(isAddEnable, R.attr.clAccent, R.attr.clDisable)
        }
    }

    private fun getEnterText(): String = binding?.toolbarInclude?.rankEnter?.text?.toString() ?: ""

    private fun clearEnter() {
        binding?.toolbarInclude?.rankEnter?.setText("")
    }

    private fun addFromEnter(toBottom: Boolean) {
        viewModel.addRank(getEnterText(), toBottom).collect(owner = this) {
            when (it) {
                AddState.Deny -> parentOpen?.clear()
                AddState.Prepare -> {
                    activity?.hideKeyboard()
                    clearEnter()
                    dismissSnackbar()
                }
                AddState.Complete -> parentOpen?.clear()
            }
        }
    }

    private fun showSnackbar() {
        val parentContainer = binding?.recyclerContainer ?: return
        snackbar.show(parentContainer, withInsets = false)
    }

    /**
     * Call of this func happens inside parent activity during pages change.
     */
    // TODO restore snackbar after page changes
    fun hideSnackbar() {
        snackbar.dismiss(skipDismissResult = true)
    }

    private fun dismissSnackbar() = snackbar.dismiss(skipDismissResult = false)

    //region Cleanup
    //
    //    override fun hideKeyboard() {
    //        activity?.hideKeyboard()
    //    }

    //    override fun onBindingList() {
    //        binding?.progressBar?.makeGone()
    //
    //        if (adapter.itemCount == 0) {
    //            /**
    //             * Prevent useless calls from [RecyclerView.setDefaultAnimator].
    //             */
    //            if (binding?.infoInclude?.parentContainer.isVisible()
    //                && binding?.recyclerView.isInvisible()
    //            ) return
    //
    //            binding?.infoInclude?.parentContainer?.makeVisible()
    //            binding?.recyclerView?.makeInvisible()
    //
    //            binding?.infoInclude?.parentContainer?.alpha = 0f
    //            binding?.infoInclude?.parentContainer?.animateAlpha(isVisible = true)
    //        } else {
    //            /**
    //             * Prevent useless calls from [RecyclerView.setDefaultAnimator].
    //             */
    //            if (binding?.infoInclude?.parentContainer.isGone()
    //                && binding?.recyclerView.isVisible()
    //            ) return
    //
    //            binding?.recyclerView?.makeVisible()
    //
    //            binding?.infoInclude?.parentContainer?.animateAlpha(isVisible = false) {
    //                binding?.infoInclude?.parentContainer?.makeGone()
    //            }
    //        }
    //
    //        /**
    //         * If toolbar enter contains text then need update add button.
    //         */
    //        viewModel.onUpdateToolbar()
    //    }
    //
    //    override fun clearEnter(): String {
    //        val name = binding?.toolbarInclude?.rankEnter?.text?.toString() ?: ""
    //        binding?.toolbarInclude?.rankEnter?.setText("")
    //        return name
    //    }
    //
    //    override fun scrollToItem(list: List<RankItem>, p: Int, addToBottom: Boolean) {
    //        parentOpen?.clear()
    //
    //        if (list.size == 1) {
    //            adapter.setList(list).notifyItemInserted(0)
    //            onBindingList()
    //        } else {
    //            notifyItemInsertedScroll(list, p)
    //        }
    //    }
    //
    //    override fun setList(list: List<RankItem>) {
    //        adapter.setList(list)
    //    }
    //
    //    override fun notifyList(list: List<RankItem>) = adapter.notifyList(list)
    //
    //    override fun notifyItemMoved(list: List<RankItem>, from: Int, to: Int) {
    //        adapter.setList(list).notifyItemMoved(from, to)
    //    }
    //
    //    override fun notifyItemInsertedScroll(list: List<RankItem>, p: Int) {
    //        adapter.setList(list).notifyItemInserted(p)
    //        RecyclerInsertScroll(binding?.recyclerView, layoutManager).scroll(list, p)
    //    }

    //endregion

    private inline fun changeVisibility(p: Int, onAction: () -> Unit) {
        parentOpen?.attempt(OpenState.Tag.ANIM) {
            onAction()
            viewModel.changeRankVisibility(p).collect(owner = this) { updateNotesBind() }
        }
    }

    private fun showRenameDialog(p: Int) {
        parentOpen?.attempt {
            viewModel.getRenameData(p).collect(owner = this) {
                val (name, nameList) = it

                dismissSnackbar()
                renameDialog.setArguments(p, name, nameList)
                    .safeShow(DialogFactory.Main.RENAME, owner = this)
            }
        }
    }

    private fun removeRank(p: Int) {
        parentOpen?.attempt {
            viewModel.removeRank(p).collect(owner = this) { updateNotesBind() }
        }
    }

    override fun onSnackbarAction() {
        parentOpen?.attempt {
            viewModel.undoRemove().collect(owner = this) { updateNotesBind() }
        }
    }

    private fun updateNotesBind() = delegators.broadcast.sendNotifyNotesBind()

    override fun onSnackbarDismiss() = viewModel.clearUndoStack()

    override fun scrollTop() {
        binding?.recyclerView?.smoothScrollToPosition(0)
    }
}