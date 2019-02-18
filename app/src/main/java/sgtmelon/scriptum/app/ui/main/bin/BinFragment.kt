package sgtmelon.scriptum.app.ui.main.bin

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
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
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.view.activity.NoteActivity
import sgtmelon.scriptum.databinding.FragmentBinBinding
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.intf.ItemIntf
import sgtmelon.scriptum.office.state.OpenState
import sgtmelon.scriptum.office.utils.AppUtils.inflateBinding
import sgtmelon.scriptum.office.utils.ColorUtils.tintIcon
import sgtmelon.scriptum.office.utils.DialogUtils.setBinArguments

class BinFragment : Fragment(),
        BinCallback,
        ItemIntf.ClickListener,
        ItemIntf.LongClickListener {

    private val openState = OpenState()

    private lateinit var activity: Activity
    private lateinit var binding: FragmentBinBinding

    private val viewModel: BinViewModel by lazy {
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
        viewModel.onLoadData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = inflater.inflateBinding(R.layout.fragment_bin, container)

        viewModel.callback = this

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

    override fun bind() {
        binding.listEmpty = adapter.itemCount == 0
        binding.executePendingBindings()
    }

    private fun setupToolbar() {
        toolbar?.title = getString(R.string.title_bin)
        toolbar?.inflateMenu(R.menu.fragment_bin)
        toolbar?.setOnMenuItemClickListener {
            openState.tryInvoke { clearBinDialog.show(fragmentManager, DialogDef.CLEAR_BIN) }
            return@setOnMenuItemClickListener true
        }

        itemClearBin?.tintIcon(activity)

        clearBinDialog.positiveListener =
                DialogInterface.OnClickListener { _, _ -> viewModel.clearBin() }

        clearBinDialog.dismissListener =
                DialogInterface.OnDismissListener { openState.clear() }
    }

    private fun setupRecycler() {
        recyclerView?.itemAnimator = object : DefaultItemAnimator() {
            override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) {
                bind()
            }
        }

        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = adapter

        optionsDialog.onClickListener = DialogInterface.OnClickListener { _, which ->
            viewModel.onClickDialog(optionsDialog.position, which)
        }
    }

    override fun notifyMenuClearBin() {
        itemClearBin?.isVisible = adapter.itemCount != 0
    }

    override fun scrollTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun notifyDataSetChanged(list: MutableList<NoteRepo>) =
            adapter.notifyDataSetChanged(list)

    override fun notifyItemRemoved(list: MutableList<NoteRepo>, p: Int) =
            adapter.notifyItemRemoved(list, p)

    override fun onItemClick(view: View, p: Int) {
        if (p == RecyclerView.NO_POSITION) return

        startActivity(NoteActivity.getIntent(activity, viewModel.getNoteItem(p).id))
    }

    override fun onItemLongClick(view: View, p: Int): Boolean {
        if (p == RecyclerView.NO_POSITION) return false

        optionsDialog.setBinArguments(activity, p)
        optionsDialog.show(fragmentManager!!, DialogDef.OPTIONS)

        return true
    }

}