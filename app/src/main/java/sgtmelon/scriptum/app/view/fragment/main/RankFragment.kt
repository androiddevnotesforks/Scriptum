package sgtmelon.scriptum.app.view.fragment.main

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.safedialog.library.RenameDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.RankAdapter
import sgtmelon.scriptum.app.control.RankTouchControl
import sgtmelon.scriptum.app.factory.DialogFactory
import sgtmelon.scriptum.app.vm.fragment.RankViewModel
import sgtmelon.scriptum.databinding.FragmentRankBinding
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.annot.def.IntentDef
import sgtmelon.scriptum.office.intf.ItemIntf
import sgtmelon.scriptum.office.state.OpenState
import java.util.*

class RankFragment : Fragment(),
        View.OnClickListener,
        View.OnLongClickListener,
        ItemIntf.ClickListener,
        ItemIntf.LongClickListener {

    private val openState = OpenState()

    private lateinit var binding: FragmentRankBinding

    private val vm: RankViewModel by lazy {
        ViewModelProviders.of(this).get(RankViewModel::class.java)
    }
    private val adapter: RankAdapter by lazy {
        RankAdapter(context!!, clickListener = this, longClickListener = this)
    }

    private val layoutManager by lazy { LinearLayoutManager(context) }

    private val recyclerView by lazy { view!!.findViewById<RecyclerView>(R.id.rank_recycler) }
    private val rankEnter by lazy { view!!.findViewById<EditText>(R.id.toolbar_rank_enter) }

    private lateinit var renameDialog: RenameDialog

    override fun onResume() {
        super.onResume()

        updateAdapter()
        bind()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rank, container, false)
        vm.loadData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renameDialog = DialogFactory.getRenameDialog(context!!, fragmentManager)

        if (savedInstanceState != null) {
            openState.isOpen = savedInstanceState.getBoolean(IntentDef.STATE_OPEN)
        }

        setupToolbar(view)
        setupRecycler()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(IntentDef.STATE_OPEN, openState.isOpen)
    }

    private fun bind(listSize: Int) {
        binding.listEmpty = listSize == 0
        bind()
    }

    private fun bind() {
        val name = rankEnter.text.toString().toUpperCase()

        binding.nameNotEmpty = !TextUtils.isEmpty(name)
        binding.listNotContain = !vm.rankRepo.listName.contains(name)
        binding.executePendingBindings()
    }

    private fun setupToolbar(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar_rank_container)
        toolbar.title = getString(R.string.title_rank)

        val rankCancel = view.findViewById<ImageButton>(R.id.toolbar_rank_cancel_button)
        val rankAdd = view.findViewById<ImageButton>(R.id.toolbar_rank_add_button)

        rankEnter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                bind()
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        rankEnter.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                val name = rankEnter.text.toString().toUpperCase()

                if (!TextUtils.isEmpty(name) && !vm.rankRepo.listName.contains(name)) {
                    onClick(rankAdd)
                    return@setOnEditorActionListener true
                } else {
                    return@setOnEditorActionListener false
                }
            } else {
                return@setOnEditorActionListener false
            }
        }

        rankCancel.setOnClickListener(this)
        rankAdd.setOnClickListener(this)
        rankAdd.setOnLongClickListener(this)
    }

    private fun clearEnter(): String {
        val name = rankEnter.text.toString()
        rankEnter.setText("")
        return name
    }

    private fun setupRecycler() {
        val touchCallback = RankTouchControl(vm)
        adapter.dragListener = touchCallback
        touchCallback.adapter = adapter

        recyclerView.itemAnimator = object : DefaultItemAnimator() {
            override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) {
                bind(adapter.itemCount)
            }
        }

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(touchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        renameDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            val p = renameDialog.position
            val rankItem = vm.onDialogRename(p, renameDialog.name)

            bind()

            adapter.setListItem(p, rankItem)
            adapter.notifyItemChanged(p)
        }
        renameDialog.dismissListener = DialogInterface.OnDismissListener { openState.isOpen = false }
    }

    private fun updateAdapter() {
        adapter.setList(vm.loadData().listRank)
        adapter.notifyDataSetChanged()

        bind(adapter.itemCount)
    }

    fun scrollTop() = recyclerView.smoothScrollToPosition(0)

    override fun onClick(v: View) {
        when (v.id) {
            R.id.toolbar_rank_cancel_button -> clearEnter()
            R.id.toolbar_rank_add_button -> {
                adapter.setList(vm.onAddEnd(clearEnter()))

                val size = adapter.itemCount
                val p = size - 1

                if (size == 1) {
                    bind(size)
                    adapter.notifyItemInserted(p)
                } else {
                    if (layoutManager.findLastVisibleItemPosition() == p - 1) {
                        recyclerView.scrollToPosition(p)
                        adapter.notifyItemInserted(p)
                    } else {
                        recyclerView.smoothScrollToPosition(p)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onLongClick(view: View): Boolean {
        adapter.setList(vm.onAddStart(clearEnter()))

        val size = adapter.itemCount
        val p = 0

        if (size == 1) {
            bind(size)
            adapter.notifyItemInserted(p)
        } else {
            if (layoutManager.findFirstVisibleItemPosition() == p) {
                recyclerView.scrollToPosition(p)
                adapter.notifyItemInserted(p)
            } else {
                recyclerView.smoothScrollToPosition(p)
                adapter.notifyDataSetChanged()
            }
        }
        return true
    }

    override fun onItemClick(view: View, p: Int) {
        if (p == RecyclerView.NO_POSITION) return

        when (view.id) {
            R.id.rank_visible_button -> adapter.setListItem(p, vm.onUpdateVisible(p))
            R.id.rank_click_container -> if (!openState.isOpen) {
                openState.isOpen = true

                val rankRepo = vm.rankRepo
                val rankItem = rankRepo.listRank[p]

                renameDialog.setArguments(p, rankItem.name, ArrayList(rankRepo.listName))
                renameDialog.show(fragmentManager, DialogDef.RENAME)
            }
            R.id.rank_cancel_button -> {
                adapter.setList(vm.onCancel(p))
                adapter.notifyItemRemoved(p)
            }
        }
    }

    override fun onItemLongClick(view: View, p: Int): Boolean {
        if (p == RecyclerView.NO_POSITION) return false

        val listRank = vm.rankRepo.listRank

        val startAnim = adapter.startAnim
        val clickVisible = listRank[p].isVisible

        for (i in listRank.indices) {
            if (i == p) continue

            val rankItem = listRank[i]

            val isVisible = rankItem.isVisible
            if (clickVisible == isVisible) {
                rankItem.isVisible = !isVisible
                startAnim[i] = true
            }
        }

        adapter.setList(listRank)
        adapter.notifyDataSetChanged()

        vm.onUpdateVisible(listRank)

        return true
    }

}