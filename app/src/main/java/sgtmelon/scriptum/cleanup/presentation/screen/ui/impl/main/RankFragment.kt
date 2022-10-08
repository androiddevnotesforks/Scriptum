package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.extension.animateAlpha
import sgtmelon.scriptum.cleanup.extension.hideKeyboard
import sgtmelon.scriptum.cleanup.extension.setDefaultAnimator
import sgtmelon.scriptum.cleanup.presentation.adapter.RankAdapter
import sgtmelon.scriptum.cleanup.presentation.control.touch.RankTouchControl
import sgtmelon.scriptum.cleanup.presentation.factory.DialogFactory
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ParentFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.main.IRankFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IRankViewModel
import sgtmelon.scriptum.databinding.FragmentRankBinding
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.system.delegators.SnackbarDelegator
import sgtmelon.scriptum.infrastructure.utils.inflateBinding
import sgtmelon.scriptum.infrastructure.widgets.listeners.RecyclerOverScrollListener
import sgtmelon.test.idling.getIdling

/**
 * Fragment which displays list of categories - [RankItem].
 */
class RankFragment : ParentFragment(),
    IRankFragment,
    UnbindNoteReceiver.Callback,
    SnackbarDelegator.Callback {

    //region Variables

    private var binding: FragmentRankBinding? = null

    @Inject lateinit var viewModel: IRankViewModel

    private val renameDialog by lazy { DialogFactory.Main(context, fm).getRename() }

    private val adapter by lazy {
        RankAdapter(object : IconBlockCallback {
            override fun setEnabled(isEnabled: Boolean) {
                getIdling().change(!isEnabled, IdlingTag.Anim.ICON)

                parentOpen?.isBlocked = !isEnabled
                parentOpen?.tag = if (isEnabled) OpenState.Tag.ND else OpenState.Tag.ANIM
            }
        }, object : ItemListener.ActionClick {
            override fun onItemClick(view: View, p: Int, action: () -> Unit) {
                when (view.id) {
                    R.id.rank_visible_button -> parentOpen?.attempt(OpenState.Tag.ANIM) {
                        action()
                        viewModel.onClickVisible(p)
                    }
                    R.id.rank_click_container -> parentOpen?.attempt {
                        viewModel.onShowRenameDialog(p)
                    }
                    R.id.rank_cancel_button -> parentOpen?.attempt {
                        viewModel.onClickCancel(p)
                    }
                }
            }
        })
    }
    private val layoutManager by lazy { LinearLayoutManager(context) }

    private val snackbar = SnackbarDelegator(
        R.string.snackbar_message_rank, R.string.snackbar_action_cancel, callback = this
    )

    val enterCard: View? get() = view?.findViewById(R.id.toolbar_rank_card)

    /**
     * Setup manually because after rotation lazy function will return null.
     */
    private var nameEnter: EditText? = null
    private var parentContainer: ViewGroup? = null
    private var recyclerContainer: ViewGroup? = null

    private var emptyInfoView: View? = null
    private var progressBar: View? = null
    private var recyclerView: RecyclerView? = null

    //endregion

    override val openState: OpenState? get() = parentOpen

    //region System

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflateBinding(R.layout.fragment_rank, container)

        ScriptumApplication.component.getRankBuilder().set(fragment = this).build()
            .inject(fragment = this)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Inside [savedInstanceState] saved snackbar data. */
        viewModel.onSetup(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUpdateData()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    /**
     * When user change page from this to another - inside [MainActivity] happen call of
     * func: [onPause], and manually run func: [dismissSnackbar]. It mean that [onStop]
     * will never call on navigation page change.
     */
    override fun onStop() {
        super.onStop()
        // TODO На сколько я понимаю, после поворота экрана будет восстановлен snackbar и поэтому не нужно чтобы отработал dismissResult
        snackbar.dismiss(skipDismissResult = true)
    }

    /**
     * Save snackbar data on rotation and screen turn off. Func [onSaveInstanceState] will be
     * called in both cases.
     *
     * - But on rotation case [outState] will be restored inside [onViewCreated].
     * - On turn off screen case [outState] will be restored only if activity will be
     *   recreated.
     *
     * On navigation page change this func will not be called. See [onStop] comment.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveData(outState)
    }

    //endregion

    //region Receiver functions

    override fun onReceiveUnbindNote(noteId: Long) = viewModel.onReceiveUnbindNote(noteId)

    //endregion

    override fun hideKeyboard() {
        activity?.hideKeyboard()
    }

    /**
     * Use [OpenState.attempt] and [OpenState.returnAttempt] because item adding
     * happen inside coroutine, not main thread.
     *
     * Reset of [OpenState.isBlocked] happen inside [scrollToItem].
     */
    override fun setupToolbar() {
        view?.findViewById<Toolbar>(R.id.toolbar_rank_container)?.apply {
            title = getString(R.string.title_rank)
        }

        view?.findViewById<ImageButton>(R.id.toolbar_rank_clear_button)?.apply {
            setOnClickListener { viewModel.onClickEnterCancel() }
        }

        view?.findViewById<ImageButton>(R.id.toolbar_rank_add_button)?.apply {
            setOnClickListener {
                parentOpen?.attempt { viewModel.onClickEnterAdd(simpleClick = true) }
            }
            setOnLongClickListener {
                parentOpen?.attempt { viewModel.onClickEnterAdd(simpleClick = false) }
                return@setOnLongClickListener true
            }
        }

        nameEnter = view?.findViewById(R.id.toolbar_rank_enter)
        nameEnter?.apply {
            doOnTextChanged { _, _, _, _ -> viewModel.onUpdateToolbar() }
            setOnEditorActionListener { _, i, _ ->
                val result = parentOpen?.returnAttempt { viewModel.onEditorClick(i) } ?: false

                /**
                 * If item wasn't add need clear [parentOpen].
                 */
                if (!result) parentOpen?.clear()

                return@setOnEditorActionListener result
            }
        }
    }

    override fun setupRecycler() {
        parentContainer = view?.findViewById(R.id.rank_parent_container)
        recyclerContainer = view?.findViewById(R.id.rank_recycler_container)
        emptyInfoView = view?.findViewById(R.id.rank_info_include)
        progressBar = view?.findViewById(R.id.rank_progress)

        val touchCallback = RankTouchControl(viewModel)

        adapter.dragListener = touchCallback

        recyclerView = view?.findViewById(R.id.rank_recycler)
        recyclerView?.let {
            it.setDefaultAnimator { viewModel.onItemAnimationFinished() }

            it.addOnScrollListener(RecyclerOverScrollListener())
            it.setHasFixedSize(true)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }

        ItemTouchHelper(touchCallback).attachToRecyclerView(recyclerView)
    }

    override fun setupDialog() {
        renameDialog.apply {
            onPositiveClick { viewModel.onResultRenameDialog(position, name) }
            onDismiss { parentOpen?.clear() }
        }
    }


    /**
     * For first time [recyclerView] visibility flag set inside xml file.
     */
    override fun prepareForLoad() {
        emptyInfoView?.visibility = View.GONE
        progressBar?.visibility = View.GONE
    }

    override fun showProgress() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun hideEmptyInfo() {
        emptyInfoView?.visibility = View.GONE
    }


    override fun onBindingList() {
        progressBar?.visibility = View.GONE

        if (adapter.itemCount == 0) {
            /**
             * Prevent useless calls from [RecyclerView.setDefaultAnimator].
             */
            if (emptyInfoView?.visibility == View.VISIBLE
                    && recyclerView?.visibility == View.INVISIBLE) return

            emptyInfoView?.visibility = View.VISIBLE
            recyclerView?.visibility = View.INVISIBLE

            emptyInfoView?.alpha = 0f
            emptyInfoView?.animateAlpha(isVisible = true)
        } else {
            /**
             * Prevent useless calls from [RecyclerView.setDefaultAnimator].
             */
            if (emptyInfoView?.visibility == View.GONE
                    && recyclerView?.visibility == View.VISIBLE) return

            recyclerView?.visibility = View.VISIBLE

            emptyInfoView?.animateAlpha(isVisible = false) {
                emptyInfoView?.visibility = View.GONE
            }
        }

        /**
         * If toolbar enter contains text then need update add button.
         */
        viewModel.onUpdateToolbar()
    }

    override fun onBindingToolbar(isClearEnable: Boolean, isAddEnable: Boolean) {
        binding?.apply {
            this.isClearEnable = isClearEnable
            this.isAddEnable = isAddEnable
        }?.executePendingBindings()
    }

    override fun scrollTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun showSnackbar() {
        recyclerContainer?.let { snackbar.show(it, withInsets = false) }
    }

    override fun dismissSnackbar() = snackbar.dismiss(skipDismissResult = false)


    override fun onSnackbarAction() {
        parentOpen?.attempt { viewModel.onSnackbarAction() }
    }

    override fun onSnackbarDismiss() = viewModel.onSnackbarDismiss()


    override fun getEnterText() = nameEnter?.text?.toString() ?: ""

    override fun clearEnter(): String {
        val name = nameEnter?.text?.toString() ?: ""
        nameEnter?.setText("")
        return name
    }

    override fun scrollToItem(list: MutableList<RankItem>, p: Int, simpleClick: Boolean) {
        parentOpen?.clear()

        if (list.size == 1) {
            adapter.setList(list).notifyItemInserted(0)
            onBindingList()
        } else {
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
                adapter.setList(list)
                recyclerView?.post { adapter.notifyDataSetChanged() }
            }
        }
    }

    override fun showRenameDialog(p: Int, name: String, nameList: List<String>) {
        renameDialog.setArguments(p, name, nameList)
            .safeShow(fm, DialogFactory.Main.RENAME, owner = this)
    }


    override fun setList(list: List<RankItem>) {
        adapter.setList(list)
    }

    override fun notifyList(list: List<RankItem>) = adapter.notifyList(list)

    override fun notifyItemChanged(list: List<RankItem>, p: Int) {
        adapter.setList(list).notifyItemChanged(p)
    }

    override fun notifyItemRemoved(list: List<RankItem>, p: Int) {
        adapter.setList(list).notifyItemRemoved(p)
    }

    override fun notifyItemMoved(list: List<RankItem>, from: Int, to: Int) {
        adapter.setList(list).notifyItemMoved(from, to)
    }

    override fun notifyItemInsertedScroll(list: List<RankItem>, p: Int) {
        adapter.setList(list).notifyItemInserted(p)

        val firstVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
        val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()

        /**
         *  FirstVisiblePosition can be equal [p] if:
         *  - Click on first item remove;
         *  - Click on snackbar undo.
         *
         *  Then [p] = 0 and firstVisiblePosition = 0.
         */
        if (p <= firstVisiblePosition || p > lastVisiblePosition) {
            recyclerView?.smoothScrollToPosition(p)
        }
    }

    //region Broadcast functions

    override fun sendNotifyNotesBroadcast() = delegators.broadcast.sendNotifyNotesBind()

    /**
     * Not used here.
     */
    override fun sendCancelNoteBroadcast(id: Long) = Unit

    /**
     * Not used here.
     */
    override fun sendNotifyInfoBroadcast(count: Int?) = Unit

    //endregion

}