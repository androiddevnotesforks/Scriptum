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
import sgtmelon.scriptum.office.intf.MenuIntf
import sgtmelon.scriptum.office.state.OpenState
import sgtmelon.scriptum.office.utils.ColorUtils

class BinFragment : Fragment(),
        ItemIntf.ClickListener,
        ItemIntf.LongClickListener,
        MenuIntf.Dialog.DeleteMenuClick {

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

    private lateinit var optionsDialog: OptionsDialog
    private lateinit var clearBinDialog: MessageDialog

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

        optionsDialog = DialogFactory.getOptionsDialog(fragmentManager)
        clearBinDialog = DialogFactory.getClearBinDialog(activity, fragmentManager)

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

        ColorUtils.tintMenuIcon(activity, itemClearBin)

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

        optionsDialog.onClickListener = DialogInterface.OnClickListener { _, i ->
            val p = optionsDialog.position
            when (i) {
                OptionsDef.Bin.restore -> onMenuRestoreClick(p)
                OptionsDef.Bin.copy -> onMenuCopyClick(p)
                OptionsDef.Bin.clear -> onMenuClearClick(p)
            }
        }
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

        startActivity(NoteActivity.getIntent(activity, vm.getId(p)))
    }

    override fun onItemLongClick(view: View, p: Int): Boolean {
        if (p == RecyclerView.NO_POSITION) return false

        optionsDialog.setArguments(resources.getStringArray(R.array.dialog_menu_bin), p)
        optionsDialog.show(fragmentManager!!, DialogDef.OPTIONS)

        return true
    }

    override fun onMenuRestoreClick(p: Int) {
        adapter.setList(vm.onMenuRestore(p))
        adapter.notifyItemRemoved(p)

        itemClearBin?.isVisible = adapter.itemCount != 0
    }

    override fun onMenuCopyClick(p: Int) = vm.onMenuCopy(p)

    override fun onMenuClearClick(p: Int) {
        adapter.setList(vm.onMenuClear(p))
        adapter.notifyItemRemoved(p)

        itemClearBin?.isVisible = adapter.itemCount != 0
    }

}