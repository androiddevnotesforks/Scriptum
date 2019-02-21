package sgtmelon.scriptum.app.ui.main.rank

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
import sgtmelon.safedialog.library.RenameDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.RankAdapter
import sgtmelon.scriptum.app.control.touch.RankTouchControl
import sgtmelon.scriptum.app.factory.DialogFactory
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.databinding.FragmentRankBinding
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.intf.ItemIntf
import sgtmelon.scriptum.office.state.OpenState
import sgtmelon.scriptum.office.utils.AppUtils.clear
import sgtmelon.scriptum.office.utils.AppUtils.inflateBinding

class RankFragment : Fragment(), RankCallback {

    private val openState = OpenState()

    private lateinit var activity: Activity
    private lateinit var binding: FragmentRankBinding

    private val viewModel: RankViewModel by lazy {
        ViewModelProviders.of(this).get(RankViewModel::class.java)
    }
    private val adapter: RankAdapter by lazy {
        RankAdapter(activity)
    }

    private val layoutManager by lazy { LinearLayoutManager(context) }

    private val recyclerView: RecyclerView? by lazy {
        view?.findViewById<RecyclerView>(R.id.rank_recycler)
    }
    private val rankEnter: EditText? by lazy {
        view?.findViewById<EditText>(R.id.toolbar_rank_enter)
    }

    private val renameDialog: RenameDialog by lazy {
        DialogFactory.getRenameDialog(activity, fragmentManager)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        activity = context as Activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = inflater.inflateBinding(R.layout.fragment_rank, container)

        viewModel.callback = this
        lifecycle.addObserver(viewModel)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            openState.value = savedInstanceState.getBoolean(OpenState.KEY)
        }

        setupToolbar()
        setupRecycler()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(OpenState.KEY, openState.value)
    }

    private fun setupToolbar() {
        val toolbar: Toolbar? = view?.findViewById(R.id.toolbar_rank_container)
        toolbar?.title = getString(R.string.title_rank)

        val rankCancel: ImageButton? = view?.findViewById(R.id.toolbar_rank_cancel_button)
        rankCancel?.setOnClickListener { rankEnter.clear() }

        val rankAdd: ImageButton? = view?.findViewById(R.id.toolbar_rank_add_button)
        rankAdd?.setOnClickListener { viewModel.onClickAdd(simpleClick = true) }
        rankAdd?.setOnLongClickListener {
            viewModel.onClickAdd(simpleClick = false)
            return@setOnLongClickListener true
        }

        rankEnter?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                bindToolbar()
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        rankEnter?.setOnEditorActionListener { _, i, _ ->
            viewModel.onEditorClick(i, rankEnter?.text.toString().toUpperCase())
        }
    }

    private fun setupRecycler() {
        adapter.clickListener = ItemIntf.ClickListener { view, p ->
            when (view.id) {
                R.id.rank_visible_button -> viewModel.onClickVisible(p)
                R.id.rank_click_container -> viewModel.onShowRenameDialog(p)
                R.id.rank_cancel_button -> viewModel.onClickCancel(p)
            }
        }
        adapter.longClickListener = ItemIntf.LongClickListener { _, p -> viewModel.onLongClickVisible(p) }

        val touchCallback = RankTouchControl(viewModel)
        adapter.dragListener = touchCallback
        touchCallback.adapter = adapter

        recyclerView?.itemAnimator = object : DefaultItemAnimator() {
            override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) {
                bindList(adapter.itemCount)
            }
        }
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = adapter

        ItemTouchHelper(touchCallback).attachToRecyclerView(recyclerView)

        renameDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onRenameDialog(renameDialog.position, renameDialog.name)
        }
        renameDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    override fun bindList(size: Int) {
        binding.listEmpty = size == 0
        bindToolbar()
    }

    override fun bindToolbar() {
        val name = rankEnter?.text.toString().toUpperCase()

        binding.nameNotEmpty = !TextUtils.isEmpty(name)
        binding.listNotContain = !viewModel.rankRepo.listName.contains(name)
        binding.executePendingBindings()
    }

    override fun scrollTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun clearEnter(): String = rankEnter?.clear() ?: ""

    override fun addItem(simpleClick: Boolean, list: MutableList<RankItem>) { // TODO при обычном добавлении нет анимации
        val p = if (simpleClick) list.size else 0

        if (list.size == 1) {
            adapter.notifyItemInserted(p, list)
            bindList(list.size)
        } else {
            val fastScroll = when (simpleClick) {
                true -> layoutManager.findLastVisibleItemPosition() == p - 1
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

    override fun showRenameDialog(p: Int, name: String, listName: ArrayList<String>) {
        openState.tryInvoke {
            renameDialog.setArguments(p, name, listName)
            renameDialog.show(fragmentManager, DialogDef.RENAME)
        }
    }

    override fun notifyVisible(p: Int, item: RankItem) = adapter.setListItem(p, item)

    override fun notifyVisible(startAnim: BooleanArray, list: MutableList<RankItem>) {
        adapter.setList(list)
        adapter.startAnim = startAnim
        adapter.notifyDataSetChanged()
    }

    override fun notifyDataSetChanged(list: MutableList<RankItem>) =
            adapter.notifyDataSetChanged(list)

    override fun notifyItemChanged(p: Int, item: RankItem) =
            adapter.notifyItemChanged(item, p)

    override fun notifyItemRemoved(p: Int, list: MutableList<RankItem>) =
            adapter.notifyItemRemoved(p, list)

    override fun notifyItemMoved(from: Int, to: Int, list: MutableList<RankItem>) =
            adapter.notifyItemMoved(from, to, list)

}