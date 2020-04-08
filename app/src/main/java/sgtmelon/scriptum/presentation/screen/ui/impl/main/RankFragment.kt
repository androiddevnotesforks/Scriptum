package sgtmelon.scriptum.presentation.screen.ui.impl.main

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.iconanim.IconBlockCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.FragmentRankBinding
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RankItem
import sgtmelon.scriptum.domain.model.state.OpenState
import sgtmelon.scriptum.extension.addTextChangedListener
import sgtmelon.scriptum.extension.createVisibleAnim
import sgtmelon.scriptum.extension.inflateBinding
import sgtmelon.scriptum.extension.initLazy
import sgtmelon.scriptum.presentation.adapter.RankAdapter
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.control.touch.RankTouchControl
import sgtmelon.scriptum.presentation.factory.DialogFactory
import sgtmelon.scriptum.presentation.listener.ItemListener
import sgtmelon.scriptum.presentation.receiver.MainReceiver
import sgtmelon.scriptum.presentation.screen.ui.ParentFragment
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IMainActivity
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IRankFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IRankViewModel
import javax.inject.Inject

/**
 * Fragment which displays list of categories - [RankItem].
 */
class RankFragment : ParentFragment(), IRankFragment, MainReceiver.BindCallback {

    private val callback: IMainActivity? by lazy { context as? IMainActivity }

    private var binding: FragmentRankBinding? = null

    @Inject internal lateinit var viewModel: IRankViewModel

    private val bindControl by lazy { BindControl[context] }

    override val openState get() = callback?.openState
    private val renameDialog by lazy { DialogFactory.Main(context, fm).getRenameDialog() }

    private val adapter by lazy {
        RankAdapter(object : IconBlockCallback {
            override fun setEnabled(enabled: Boolean) {
                openState?.value = !enabled
                openState?.tag = if (enabled) OpenState.Tag.ND else OpenState.Tag.ANIM
            }
        }, object: ItemListener.ActionClick {
            override fun onItemClick(view: View, p: Int, action: () -> Unit) {
                when (view.id) {
                    R.id.rank_visible_button -> openState?.tryInvoke(OpenState.Tag.ANIM) {
                        action()
                        viewModel.onClickVisible(p)
                    }
                    R.id.rank_click_container -> openState?.tryInvoke {
                        viewModel.onShowRenameDialog(p)
                    }
                    R.id.rank_cancel_button -> openState?.tryInvoke {
                        viewModel.onClickCancel(p)
                    }
                }
            }
        }, object : ItemListener.LongClick {
            override fun onItemLongClick(view: View, p: Int) {
                openState?.tryInvoke { viewModel.onLongClickVisible(p) }
            }
        })
    }
    private val layoutManager by lazy { LinearLayoutManager(context) }

    val enterCard: View? get() = view?.findViewById(R.id.toolbar_rank_card)

    /**
     * Setup manually because after rotation lazy function will return null.
     */
    private var nameEnter: EditText? = null
    private var parentContainer: ViewGroup? = null

    private var emptyInfoView: View? = null
    private var progressBar: View? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_rank, container)

        ScriptumApplication.component.getRankBuilder().set(fragment = this).build()
                .inject(fragment = this)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindControl.initLazy()

        viewModel.onSetup()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUpdateData()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    //region Receiver functions

    override fun onReceiveUnbindNote(id: Long) = viewModel.onReceiveUnbindNote(id)

    //endregion

    /**
     * Use [OpenState.tryInvoke] and [OpenState.tryReturnInvoke] because item adding
     * happen inside coroutine, not main thread.
     *
     * Reset of [OpenState.value] happen inside [scrollToItem].
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
                openState?.tryInvoke {  viewModel.onClickEnterAdd(simpleClick = true) }
            }
            setOnLongClickListener {
                openState?.tryInvoke { viewModel.onClickEnterAdd(simpleClick = false) }
                return@setOnLongClickListener true
            }
        }

        nameEnter = view?.findViewById(R.id.toolbar_rank_enter)
        nameEnter?.apply {
            addTextChangedListener(on = { viewModel.onUpdateToolbar() })
            setOnEditorActionListener { _, i, _ ->
                val result = openState?.tryReturnInvoke { viewModel.onEditorClick(i) } ?: false

                /**
                 * If item wasn't add need clear [openState].
                 */
                if (!result) openState?.clear()

                return@setOnEditorActionListener result
            }
        }
    }

    override fun setupRecycler() {
        parentContainer = view?.findViewById(R.id.rank_parent_container)
        emptyInfoView = view?.findViewById(R.id.rank_info_include)
        progressBar = view?.findViewById(R.id.rank_progress)

        val touchCallback = RankTouchControl(viewModel)

        adapter.dragListener = touchCallback

        recyclerView = view?.findViewById(R.id.rank_recycler)
        recyclerView?.let {
            it.itemAnimator = object : DefaultItemAnimator() {
                override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) {
                    onBindingList()
                    openState?.clear()
                }
            }

            it.setHasFixedSize(true)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }

        ItemTouchHelper(touchCallback).attachToRecyclerView(recyclerView)

        renameDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                viewModel.onResultRenameDialog(position, name)
            }
            dismissListener = DialogInterface.OnDismissListener { openState?.clear() }
        }
    }


    override fun beforeLoad() {
        emptyInfoView?.visibility = View.GONE
        progressBar?.visibility = View.GONE
    }

    override fun showProgress() {
        progressBar?.visibility = View.VISIBLE
    }


    override fun onBindingList() {
        progressBar?.visibility = View.GONE

        val isListEmpty = adapter.itemCount == 0

        parentContainer?.createVisibleAnim(emptyInfoView, isListEmpty)

        binding?.isListEmpty = isListEmpty

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

    override fun getEnterText() = nameEnter?.text?.toString() ?: ""

    override fun clearEnter(): String {
        val name = nameEnter?.text?.toString() ?: ""
        nameEnter?.setText("")
        return name
    }

    override fun scrollToItem(list: MutableList<RankItem>, p: Int, simpleClick: Boolean) {
        openState?.clear()

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
                adapter.setList(list).notifyDataSetChanged()
            }
        }
    }

    override fun showRenameDialog(p: Int, name: String, nameList: List<String>) {
        renameDialog.setArguments(p, name, nameList).show(fm, DialogFactory.Main.RENAME)
    }


    override fun setList(list: List<RankItem>) {
        adapter.setList(list)
    }

    override fun notifyList(list: List<RankItem>) = adapter.notifyList(list)

    override fun notifyDataSetChanged(list: List<RankItem>, startAnim: BooleanArray) {
        adapter.setList(list).apply { this.startAnim = startAnim }.notifyDataSetChanged()

        /**
         * If don't have animation, when [OpenState.value] set in [IconBlockCallback]
         * will not call. That code prevent this bug.
         */
        if (!startAnim.contains(true)) {
            openState?.clear()
        }
    }

    override fun notifyItemChanged(list: List<RankItem>, p: Int) {
        adapter.setList(list).notifyItemChanged(p)
    }

    override fun notifyItemRemoved(list: List<RankItem>, p: Int) {
        adapter.setList(list).notifyItemRemoved(p)
    }

    override fun notifyItemMoved(list: List<RankItem>, from: Int, to: Int) {
        adapter.setList(list).notifyItemMoved(from, to)
    }

    override fun notifyNoteBind(item: NoteItem, rankIdVisibleList: List<Long>) {
        bindControl.notifyNote(item, rankIdVisibleList)
    }

}