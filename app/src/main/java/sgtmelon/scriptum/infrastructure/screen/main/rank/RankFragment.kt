package sgtmelon.scriptum.infrastructure.screen.main.rank

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject
import sgtmelon.extensions.collect
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.extension.animateAlpha
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
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.main.callback.ScrollTopCallback
import sgtmelon.scriptum.infrastructure.screen.main.rank.state.UpdateListState
import sgtmelon.scriptum.infrastructure.screen.parent.BindingFragment
import sgtmelon.scriptum.infrastructure.system.delegators.SnackbarDelegator
import sgtmelon.scriptum.infrastructure.utils.hideKeyboard
import sgtmelon.scriptum.infrastructure.utils.isGone
import sgtmelon.scriptum.infrastructure.utils.isInvisible
import sgtmelon.scriptum.infrastructure.utils.isVisible
import sgtmelon.scriptum.infrastructure.utils.makeGone
import sgtmelon.scriptum.infrastructure.utils.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.makeVisible
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
         * Use [OpenState.attempt] and [OpenState.returnAttempt] inside add category feature
         * because calculations happens inside coroutine, not main thread.
         *
         * Reset of [OpenState.isBlocked] happen inside [scrollToItem].
         */
        binding?.toolbarInclude?.apply {
            toolbar.title = getString(R.string.title_rank)
            clearButton.setOnClickListener { viewModel.onClickEnterCancel() }
            addButton.setOnClickListener {
                parentOpen?.attempt { viewModel.onClickEnterAdd(addToBottom = true) }
            }
            addButton.setOnLongClickListener {
                parentOpen?.attempt { viewModel.onClickEnterAdd(addToBottom = false) }
                return@setOnLongClickListener true
            }
            rankEnter.doOnTextChanged { _, _, _, _ -> viewModel.onUpdateToolbar() }
            rankEnter.setOnEditorActionListener { _, i, _ ->
                val result = parentOpen?.returnAttempt {
                    viewModel.onEditorClick(i)
                } ?: false

                /**
                 * If item wasn't add need clear [parentOpen].
                 */
                if (!result) parentOpen?.clear()

                return@setOnEditorActionListener result
            }
        }

        binding?.recyclerView?.let {
            it.setDefaultAnimator(supportsChangeAnimations = false) {
                viewModel.onItemAnimationFinished()
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
            onPositiveClick { viewModel.onResultRenameDialog(position, name) }
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
        }
        viewModel.itemList.observe(this) { observeItemList(it) }
        viewModel.showSnackbar.observe(this) { if (it) showSnackbar() }
        TODO()
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
                adapter.setList(it).notifyItemInserted(state.p)
                RecyclerInsertScroll(binding?.recyclerView, layoutManager).scroll(it, state.p)
            }
        }
    }

    //endregion

    private fun showSnackbar() {
        val parentContainer = binding?.recyclerContainer ?: return
        snackbar.show(parentContainer, withInsets = false)
    }

    // TODO restore snackbar after page changes
    /**
     * Call of this func happens inside parent activity during pages change.
     */
    fun hideSnackbar() {
        snackbar.dismiss(skipDismissResult = true)
    }

    private fun dismissSnackbar() = snackbar.dismiss(skipDismissResult = false)

    //region Cleanup

    //region Variables

    //    val enterCard: View? get() = view?.findViewById(R.id.toolbar_rank_card)

    //    /**
    //     * Setup manually because after rotation lazy function will return null.
    //     */
    //    private var nameEnter: EditText? = null
    //    private var parentContainer: ViewGroup? = null
    //    private var recyclerContainer: ViewGroup? = null

    //    private var emptyInfoView: View? = null
    //    private var progressBar: View? = null
    //    private var recyclerView: RecyclerView? = null

    //endregion

    //region System

    //    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    //        super.onViewCreated(view, savedInstanceState)
    //
    //        /** Inside [savedInstanceState] saved snackbar data. */
    //        viewModel.onSetup(savedInstanceState)
    //    }

    //    override fun onResume() {
    //        super.onResume()
    //        viewModel.onUpdateData()
    //    }
    //
    //    override fun onDestroy() {
    //        super.onDestroy()
    //        viewModel.onDestroy()
    //    }

    //    override fun onStop() {
    //        super.onStop()
    //
    //        /**
    //         * Need dismiss without listener for control leave screen case. We don't want to lost
    //         * snackbar stack inside [viewModel] during rotation/app close/ect. So, need restore
    //         * [snackbar] after screen reopen - [onResume].
    //         */
    //        snackbar.dismiss(skipDismissResult = true)
    //    }

    //    /**
    //     * Save snackbar data on rotation and screen turn off. Func [onSaveInstanceState] will be
    //     * called in both cases.
    //     *
    //     * - But on rotation case [outState] will be restored inside [onViewCreated].
    //     * - On turn off screen case [outState] will be restored only if activity will be
    //     *   recreated.
    //     *
    //     * On navigation page change this func will not be called. See [onStop] comment.
    //     */
    //    override fun onSaveInstanceState(outState: Bundle) {
    //        super.onSaveInstanceState(outState)
    //        viewModel.onSaveData(outState)
    //    }

    //endregion

    override fun hideKeyboard() {
        activity?.hideKeyboard()
    }

    //    /**
    //     * Use [OpenState.attempt] and [OpenState.returnAttempt] because item adding
    //     * happen inside coroutine, not main thread.
    //     *
    //     * Reset of [OpenState.isBlocked] happen inside [scrollToItem].
    //     */
    //    override fun setupToolbar() {
    //        binding?.toolbarInclude?.toolbar?.title = getString(R.string.title_rank)
    //        binding?.toolbarInclude?.clearButton?.setOnClickListener {
    //            viewModel.onClickEnterCancel()
    //        }
    //        binding?.toolbarInclude?.addButton?.apply {
    //            setOnClickListener {
    //                parentOpen?.attempt { viewModel.onClickEnterAdd(addToBottom = true) }
    //            }
    //            setOnLongClickListener {
    //                parentOpen?.attempt { viewModel.onClickEnterAdd(addToBottom = false) }
    //                return@setOnLongClickListener true
    //            }
    //        }
    //        binding?.toolbarInclude?.rankEnter?.apply {
    //            doOnTextChanged { _, _, _, _ -> viewModel.onUpdateToolbar() }
    //            setOnEditorActionListener { _, i, _ ->
    //                val result = parentOpen?.returnAttempt { viewModel.onEditorClick(i) } ?: false
    //
    //                /**
    //                 * If item wasn't add need clear [parentOpen].
    //                 */
    //                if (!result) parentOpen?.clear()
    //
    //                return@setOnEditorActionListener result
    //            }
    //        }
    //
    //        //        view?.findViewById<Toolbar>(R.id.toolbar)?.apply {
    //        //            title = getString(R.string.title_rank)
    //        //        }
    //
    //        //        view?.findViewById<ImageButton>(R.id.clear_button)?.apply {
    //        //            setOnClickListener { viewModel.onClickEnterCancel() }
    //        //        }
    //
    //        //        view?.findViewById<ImageButton>(R.id.add_button)?.apply {
    //        //            setOnClickListener {
    //        //                parentOpen?.attempt { viewModel.onClickEnterAdd(addToBottom = true) }
    //        //            }
    //        //            setOnLongClickListener {
    //        //                parentOpen?.attempt { viewModel.onClickEnterAdd(addToBottom = false) }
    //        //                return@setOnLongClickListener true
    //        //            }
    //        //        }
    //
    //        //        nameEnter = view?.findViewById(R.id.rank_enter)
    //    }

    //    override fun setupRecycler() {
    //        //        parentContainer = view?.findViewById(R.id.parent_container)
    //        //        recyclerContainer = view?.findViewById(R.id.recycler_container)
    //        //        emptyInfoView = view?.findViewById(R.id.info_include)
    //        //        progressBar = view?.findViewById(R.id.progress_bar)
    //
    //        //        recyclerView = view?.findViewById(R.id.recycler_view)
    //        binding?.recyclerView?.let {
    //            it.setDefaultAnimator(supportsChangeAnimations = false) {
    //                viewModel.onItemAnimationFinished()
    //            }
    //
    //            it.addOnScrollListener(RecyclerOverScrollListener())
    //            it.setHasFixedSize(true)
    //            it.layoutManager = layoutManager
    //            it.adapter = adapter
    //        }
    //
    //        ItemTouchHelper(touchControl).attachToRecyclerView(binding?.recyclerView)
    //    }

    //    override fun setupDialog() {
    //        renameDialog.apply {
    //            onPositiveClick { viewModel.onResultRenameDialog(position, name) }
    //            onDismiss { parentOpen?.clear() }
    //        }
    //    }
    //
    //
    //    /**
    //     * For first time [recyclerView] visibility flag set inside xml file.
    //     */
    //    override fun prepareForLoad() {
    //        binding?.infoInclude?.parentContainer?.makeGone()
    //        binding?.progressBar?.makeGone()
    //    }
    //
    //    override fun showProgress() {
    //        binding?.progressBar?.makeVisible()
    //    }
    //
    //    override fun hideEmptyInfo() {
    //        binding?.infoInclude?.parentContainer?.makeGone()
    //    }


    override fun onBindingList() {
        binding?.progressBar?.makeGone()

        if (adapter.itemCount == 0) {
            /**
             * Prevent useless calls from [RecyclerView.setDefaultAnimator].
             */
            if (binding?.infoInclude?.parentContainer.isVisible()
                && binding?.recyclerView.isInvisible()
            ) return

            binding?.infoInclude?.parentContainer?.makeVisible()
            binding?.recyclerView?.makeInvisible()

            binding?.infoInclude?.parentContainer?.alpha = 0f
            binding?.infoInclude?.parentContainer?.animateAlpha(isVisible = true)
        } else {
            /**
             * Prevent useless calls from [RecyclerView.setDefaultAnimator].
             */
            if (binding?.infoInclude?.parentContainer.isGone()
                && binding?.recyclerView.isVisible()
            ) return

            binding?.recyclerView?.makeVisible()

            binding?.infoInclude?.parentContainer?.animateAlpha(isVisible = false) {
                binding?.infoInclude?.parentContainer?.makeGone()
            }
        }

        /**
         * If toolbar enter contains text then need update add button.
         */
        viewModel.onUpdateToolbar()
    }

    override fun onBindingToolbar(isClearEnable: Boolean, isAddEnable: Boolean) {
        binding?.toolbarInclude?.apply {
            clearButton.isEnabled = isClearEnable
            clearButton.bindBoolTint(isClearEnable, R.attr.clContent, R.attr.clDisable)

            addButton.isEnabled = isAddEnable
            addButton.bindBoolTint(isAddEnable, R.attr.clAccent, R.attr.clDisable)
        }

        //        binding?.apply {
        //            this.isClearEnable = isClearEnable
        //            this.isAddEnable = isAddEnable
        //        }?.executePendingBindings()
    }

    //
    //    override fun showSnackbar() {
    //        binding?.recyclerContainer?.let { snackbar.show(it, withInsets = false) }
    //    }
    //
    //    override fun dismissSnackbar() = snackbar.dismiss(skipDismissResult = false)

    override fun getEnterText() = binding?.toolbarInclude?.rankEnter?.text?.toString() ?: ""

    override fun clearEnter(): String {
        val name = binding?.toolbarInclude?.rankEnter?.text?.toString() ?: ""
        binding?.toolbarInclude?.rankEnter?.setText("")
        return name
    }

    override fun scrollToItem(list: List<RankItem>, p: Int, addToBottom: Boolean) {
        parentOpen?.clear()

        if (list.size == 1) {
            adapter.setList(list).notifyItemInserted(0)
            onBindingList()
        } else {
            notifyItemInsertedScroll(list, p)
        }
    }

    //    override fun showRenameDialog(p: Int, name: String, nameList: List<String>) {
    //        renameDialog.setArguments(p, name, nameList)
    //            .safeShow(DialogFactory.Main.RENAME, owner = this)
    //    }


    override fun setList(list: List<RankItem>) {
        adapter.setList(list)
    }

    override fun notifyList(list: List<RankItem>) = adapter.notifyList(list)

    override fun notifyItemMoved(list: List<RankItem>, from: Int, to: Int) {
        adapter.setList(list).notifyItemMoved(from, to)
    }

    // TODO finish
    override fun notifyItemInsertedScroll(list: List<RankItem>, p: Int) {
        adapter.setList(list).notifyItemInserted(p)
        RecyclerInsertScroll(binding?.recyclerView, layoutManager).scroll(list, p)
    }

    //    override fun sendNotifyNotesBroadcast() = delegators.broadcast.sendNotifyNotesBind()

    //    /**
    //     * Not used here.
    //     */
    //    override fun sendCancelNoteBroadcast(id: Long) = Unit
    //
    //    /**
    //     * Not used here.
    //     */
    //    override fun sendNotifyInfoBroadcast(count: Int?) = Unit

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