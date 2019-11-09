package sgtmelon.scriptum.screen.ui.main

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
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.RankAdapter
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.control.bind.IBindControl
import sgtmelon.scriptum.control.touch.RankTouchControl
import sgtmelon.scriptum.databinding.FragmentRankBinding
import sgtmelon.scriptum.extension.addTextChangedListener
import sgtmelon.scriptum.extension.createVisibleAnim
import sgtmelon.scriptum.extension.inflateBinding
import sgtmelon.scriptum.extension.initLazy
import sgtmelon.scriptum.factory.DialogFactory
import sgtmelon.scriptum.factory.ViewModelFactory
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.screen.ui.ParentFragment
import sgtmelon.scriptum.screen.ui.callback.main.IMainActivity
import sgtmelon.scriptum.screen.ui.callback.main.IRankFragment

/**
 * Fragment which displays list of categories - [RankEntity]
 */
class RankFragment : ParentFragment(), IRankFragment {

    private val callback: IMainActivity? by lazy { context as? IMainActivity }

    private var binding: FragmentRankBinding? = null

    private val iViewModel by lazy { ViewModelFactory.getRankViewModel(fragment = this) }

    private val iBindControl: IBindControl by lazy { BindControl(context) }

    private val openState get() = callback?.openState
    private val renameDialog by lazy { DialogFactory.Main(context, fm).getRenameDialog() }

    private val visibleAnimTime: Long? by lazy {
        context?.resources?.getInteger(R.integer.visible_anim_time)?.toLong()
    }

    private val adapter by lazy {
        RankAdapter(object: ItemListener.ActionClick {
            override fun onItemClick(view: View, p: Int, action: () -> Unit) {
                openState?.tryInvoke {
                    when (view.id) {
                        R.id.rank_visible_button -> {
                            openState?.block(visibleAnimTime)

                            action()
                            iViewModel.onClickVisible(p)
                        }
                        R.id.rank_click_container -> iViewModel.onShowRenameDialog(p)
                        R.id.rank_cancel_button -> iViewModel.onClickCancel(p)
                    }
                }
            }
        }, object : ItemListener.LongClick {
            override fun onItemLongClick(view: View, p: Int) {
                openState?.tryInvoke {
                    openState?.block(visibleAnimTime)
                    iViewModel.onLongClickVisible(p)
                }
            }
        })
    }
    private val layoutManager by lazy { LinearLayoutManager(context) }

    val enterCard: View? get() = view?.findViewById(R.id.toolbar_rank_card)

    private var nameEnter: EditText? = null
    private var parentContainer: ViewGroup? = null
    private var emptyInfoView: View? = null

    private var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_rank, container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iBindControl.initLazy()

        iViewModel.onSetup()
    }

    override fun onResume() {
        super.onResume()
        iViewModel.onUpdateData()
    }

    override fun onDestroy() {
        super.onDestroy()
        iViewModel.onDestroy()
    }


    override fun setupToolbar() {
        view?.findViewById<Toolbar>(R.id.toolbar_rank_container)?.apply {
            title = getString(R.string.title_rank)
        }

        view?.findViewById<ImageButton>(R.id.toolbar_rank_clear_button)?.apply {
            setOnClickListener { iViewModel.onClickEnterCancel() }
        }

        view?.findViewById<ImageButton>(R.id.toolbar_rank_add_button)?.apply {
            setOnClickListener {
                openState?.tryInvoke {  iViewModel.onClickEnterAdd(simpleClick = true) }
            }
            setOnLongClickListener {
                openState?.tryInvoke { iViewModel.onClickEnterAdd(simpleClick = false) }
                return@setOnLongClickListener true
            }
        }

        nameEnter = view?.findViewById(R.id.toolbar_rank_enter)
        nameEnter?.apply {
            addTextChangedListener(on = { iViewModel.onUpdateToolbar() })
            setOnEditorActionListener { _, i, _ ->
                openState?.tryReturnInvoke { iViewModel.onEditorClick(i) } ?: false
            }
        }
    }

    override fun setupRecycler() {
        parentContainer = view?.findViewById(R.id.rank_parent_container)
        emptyInfoView = view?.findViewById(R.id.rank_info_include)

        val touchCallback = RankTouchControl(iViewModel)

        adapter.dragListener = touchCallback

        recyclerView = view?.findViewById(R.id.rank_recycler)
        recyclerView?.let {
            it.itemAnimator = object : DefaultItemAnimator() {
                override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) {
                    bindList()
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
                iViewModel.onRenameDialog(position, name)
            }
            dismissListener = DialogInterface.OnDismissListener { openState?.clear() }
        }
    }

    override fun bindList() {
        val isListEmpty = adapter.itemCount == 0

        parentContainer?.createVisibleAnim(emptyInfoView, isListEmpty)

        binding?.isListEmpty = isListEmpty

        iViewModel.onUpdateToolbar()
    }

    override fun bindToolbar(isClearEnable: Boolean, isAddEnable: Boolean) {
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

    override fun scrollToItem(simpleClick: Boolean, p: Int, list: MutableList<RankItem>) {
        openState?.clear()

        if (list.size == 1) {
            adapter.notifyItemInserted(0, list)
            bindList()
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
                adapter.notifyItemInserted(p, list)
            } else {
                recyclerView?.smoothScrollToPosition(p)
                adapter.notifyDataSetChanged(list)
            }
        }
    }

    override fun showRenameDialog(p: Int, name: String, nameList: List<String>) {
        renameDialog.setArguments(p, name, nameList).show(fm, DialogFactory.Main.RENAME)
    }


    override fun notifyVisible(p: Int, item: RankItem) = adapter.setListItem(p, item)

    override fun notifyVisible(startAnim: BooleanArray, list: MutableList<RankItem>) {
        adapter.apply {
            setList(list)
            this.startAnim = startAnim
        }.notifyDataSetChanged()
    }

    override fun notifyDataSetChanged(list: MutableList<RankItem>) {
        adapter.notifyDataSetChanged(list)
    }

    override fun notifyItemChanged(p: Int, item: RankItem) = adapter.notifyItemChanged(item, p)

    override fun notifyItemRemoved(p: Int, list: MutableList<RankItem>) {
        adapter.notifyItemRemoved(p, list)
    }

    override fun notifyItemMoved(from: Int, to: Int, list: MutableList<RankItem>) {
        adapter.notifyItemMoved(from, to, list)
    }


    override fun notifyNoteBind(noteModel: NoteModel, rankIdVisibleList: List<Long>) {
        iBindControl.notifyNote(noteModel, rankIdVisibleList)
    }

}