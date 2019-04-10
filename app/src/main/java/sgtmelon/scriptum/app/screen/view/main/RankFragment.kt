package sgtmelon.scriptum.app.screen.view.main

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.RankAdapter
import sgtmelon.scriptum.app.control.touch.RankTouchControl
import sgtmelon.scriptum.app.factory.DialogFactory
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.model.state.OpenState
import sgtmelon.scriptum.app.screen.callback.main.RankCallback
import sgtmelon.scriptum.app.screen.vm.main.RankViewModel
import sgtmelon.scriptum.app.watcher.AppTextWatcher
import sgtmelon.scriptum.databinding.FragmentRankBinding
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.AppUtils.clear
import sgtmelon.scriptum.office.utils.AppUtils.getClearText
import sgtmelon.scriptum.office.utils.AppUtils.inflateBinding

/**
 * Фрагмент для отображения списка категорий - [RankItem]
 *
 * @author SerjantArbuz
 */
class RankFragment : Fragment(), RankCallback {

    private lateinit var activity: Activity

    private var binding: FragmentRankBinding? = null

    private val viewModel: RankViewModel by lazy {
        ViewModelProviders.of(this).get(RankViewModel::class.java).apply {
            callback = this@RankFragment
        }
    }

    private val adapter by lazy { RankAdapter(activity) }
    private val layoutManager by lazy { LinearLayoutManager(activity) }

    private var recyclerView: RecyclerView? = null
    private var rankEnter: EditText? = null

    private val openState = OpenState()
    private val renameDialog by lazy { DialogFactory.getRenameDialog(fragmentManager) }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        activity = context as Activity
    }

    override fun onResume() {
        super.onResume()

        viewModel.onUpdateData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_rank, container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            openState.value = savedInstanceState.getBoolean(OpenState.KEY)
        }

        setupToolbar()
        setupRecycler()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply { putBoolean(OpenState.KEY, openState.value) })

    private fun setupToolbar() {
        view?.findViewById<Toolbar>(R.id.toolbar_rank_container)?.apply {
            title = getString(R.string.title_rank)
        }

        view?.findViewById<ImageButton>(R.id.toolbar_rank_cancel_button)?.apply {
            setOnClickListener { rankEnter.clear() }
        }

        view?.findViewById<ImageButton>(R.id.toolbar_rank_add_button)?.apply {
            setOnClickListener { viewModel.onClickAdd(simpleClick = true) }
            setOnLongClickListener {
                viewModel.onClickAdd(simpleClick = false)
                return@setOnLongClickListener true
            }
        }

        rankEnter = view?.findViewById(R.id.toolbar_rank_enter)
        rankEnter?.addTextChangedListener(object : AppTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                    bindToolbar()
        })
        rankEnter?.setOnEditorActionListener { _, i, _ ->
            viewModel.onEditorClick(i, rankEnter?.text.toString().toUpperCase())
        }
    }

    private fun setupRecycler() {
        val touchCallback = RankTouchControl(viewModel)

        adapter.apply {
            clickListener = ItemListener.ClickListener { view, p ->
                when (view.id) {
                    R.id.rank_visible_button -> viewModel.onClickVisible(p)
                    R.id.rank_click_container -> viewModel.onShowRenameDialog(p)
                    R.id.rank_cancel_button -> viewModel.onClickCancel(p)
                }
            }
            longClickListener = ItemListener.LongClickListener { _, p ->
                viewModel.onLongClickVisible(p)
            }
            dragListener = touchCallback
        }

        recyclerView = view?.findViewById(R.id.rank_recycler)
        recyclerView?.itemAnimator = object : DefaultItemAnimator() {
            override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) =
                    bindList(adapter.itemCount)
        }
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = adapter

        ItemTouchHelper(touchCallback).attachToRecyclerView(recyclerView)

        renameDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                viewModel.onRenameDialog(position, name)
            }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }
    }

    override fun bindList(size: Int) {
        binding?.listEmpty = size == 0
        bindToolbar()
    }

    override fun bindToolbar() {
        val name = rankEnter?.getClearText()?.toUpperCase() ?: ""

        binding?.apply {
            enableClear = rankEnter?.text.toString().isNotEmpty()
            enableAdd = name.isNotEmpty() && !viewModel.rankModel.nameList.contains(name)
        }?.executePendingBindings()
    }

    override fun scrollTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun clearEnter(): String = rankEnter?.clear() ?: ""

    override fun scrollToItem(simpleClick: Boolean, list: MutableList<RankItem>) {
        val p = if (simpleClick) list.size else 0

        if (list.size == 1) {
            adapter.notifyItemInserted(p, list)
            bindList(list.size)
        } else {
            val fastScroll = when (simpleClick) {
                true -> layoutManager.findLastVisibleItemPosition() == p - 2
                false -> layoutManager.findFirstVisibleItemPosition() == p
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

    override fun showRenameDialog(p: Int, name: String, listName: ArrayList<String>) =
            openState.tryInvoke {
                renameDialog.apply {
                    setArguments(p, name, listName)
                }.show(fragmentManager, DialogDef.RENAME)
            }

    override fun notifyVisible(p: Int, item: RankItem) = adapter.setListItem(p, item)

    override fun notifyVisible(startAnim: BooleanArray, list: MutableList<RankItem>) =
            adapter.apply {
                setList(list)
                this.startAnim = startAnim
            }.notifyDataSetChanged()

    override fun notifyDataSetChanged(list: MutableList<RankItem>) =
            adapter.notifyDataSetChanged(list)

    override fun notifyItemChanged(p: Int, item: RankItem) =
            adapter.notifyItemChanged(item, p)

    override fun notifyItemRemoved(p: Int, list: MutableList<RankItem>) =
            adapter.notifyItemRemoved(p, list)

    override fun notifyItemMoved(from: Int, to: Int, list: MutableList<RankItem>) =
            adapter.notifyItemMoved(from, to, list)

}