package sgtmelon.scriptum.app.view.fragment.main

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.safedialog.library.MessageDialog
import sgtmelon.safedialog.library.OptionsDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.NoteAdapter
import sgtmelon.scriptum.app.factory.DialogFactory
import sgtmelon.scriptum.app.view.activity.NoteActivity
import sgtmelon.scriptum.app.vm.fragment.BinViewModel
import sgtmelon.scriptum.databinding.FragmentBinBinding
import sgtmelon.scriptum.office.annot.def.BinDef
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.annot.def.IntentDef
import sgtmelon.scriptum.office.annot.def.OptionsDef
import sgtmelon.scriptum.office.intf.ItemIntf
import sgtmelon.scriptum.office.state.OpenState
import sgtmelon.scriptum.office.utils.AppUtils.notifyItemRemoved
import sgtmelon.scriptum.office.utils.ColorUtils.tintIcon
import sgtmelon.scriptum.office.utils.DialogUtils.setBinArguments
import sgtmelon.scriptum.office.utils.HelpUtils.copyToClipboard

class BinFragment : Fragment(),
        ItemIntf.ClickListener,
        ItemIntf.LongClickListener,
        DialogInterface.OnClickListener {

    private val openSt = OpenState()

    private lateinit var activity: Activity
    private lateinit var binding: FragmentBinBinding

    private val vm: BinViewModel by lazy {
        ViewModelProviders.of(this).get(BinViewModel::class.java)
    }
    private val adapter: NoteAdapter by lazy {
        NoteAdapter(activity, clickListener = this, longClickListener = this)
    }

    private val toolbar: Toolbar? by lazy {
        view?.findViewById<Toolbar>(R.id.toolbar_container)
    }
    private val itemClearBin: MenuItem? by lazy {
        toolbar?.menu?.findItem(R.id.item_clear)
    }
    private val recyclerView: RecyclerView? by lazy {
        view?.findViewById<RecyclerView>(R.id.bin_recycler)
    }

    private val optionsDialog: OptionsDialog by lazy {
        DialogFactory.getOptionsDialog(fragmentManager)
    }
    private val clearBinDialog: MessageDialog by lazy {
        DialogFactory.getClearBinDialog(activity, fragmentManager)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        activity = context as Activity
    }

    override fun onResume() {
        super.onResume()
        updateAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_bin, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            openSt.isOpen = savedInstanceState.getBoolean(IntentDef.STATE_OPEN)
        }

        setupToolbar()
        setupRecycler()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IntentDef.STATE_OPEN, openSt.isOpen)
    }

    private fun bind() {
        binding.listEmpty = adapter.itemCount == 0
        binding.executePendingBindings()
    }

    private fun setupToolbar() {
        toolbar?.title = getString(R.string.title_bin)
        toolbar?.inflateMenu(R.menu.fragment_bin)
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_clear -> {
                    if (!openSt.isOpen) {
                        openSt.isOpen = true
                        clearBinDialog.show(fragmentManager, DialogDef.CLEAR_BIN)
                    }
                    true
                }
                else -> false
            }
        }

        itemClearBin.tintIcon(activity)

        clearBinDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            adapter.setList(vm.onClear())
            adapter.notifyDataSetChanged()

            itemClearBin?.isVisible = false
            bind()
        }

        clearBinDialog.dismissListener = DialogInterface.OnDismissListener { openSt.isOpen = false }
    }

    private fun setupRecycler() {
        recyclerView?.itemAnimator = object : DefaultItemAnimator() {
            override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) {
                bind()
            }
        }

        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = adapter

        optionsDialog.onClickListener = this
    }

    private fun updateAdapter() {
        adapter.setList(vm.loadData(BinDef.`in`))
        adapter.notifyDataSetChanged()

        itemClearBin?.isVisible = adapter.itemCount != 0
        bind()
    }

    fun scrollTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun onItemClick(view: View, p: Int) {
        if (p == RecyclerView.NO_POSITION) return

        startActivity(NoteActivity.getIntent(activity, vm.getNoteItem(p).id))
    }

    override fun onItemLongClick(view: View, p: Int): Boolean {
        if (p == RecyclerView.NO_POSITION) return false

        optionsDialog.setBinArguments(activity, p)
        optionsDialog.show(fragmentManager!!, DialogDef.OPTIONS)

        return true
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val p = optionsDialog.position

        when (which) {
            OptionsDef.Bin.restore -> {
                itemClearBin?.isVisible = adapter.itemCount != 0
                adapter.notifyItemRemoved(vm.onMenuRestore(p), p)
            }
            OptionsDef.Bin.copy -> activity.copyToClipboard(vm.getNoteItem(p))
            OptionsDef.Bin.clear -> {
                itemClearBin?.isVisible = adapter.itemCount != 0
                adapter.notifyItemRemoved(vm.onMenuClear(p), p)
            }
        }
    }

}