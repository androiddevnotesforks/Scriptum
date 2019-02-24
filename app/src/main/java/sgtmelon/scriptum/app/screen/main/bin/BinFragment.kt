package sgtmelon.scriptum.app.screen.main.bin

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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
import sgtmelon.safedialog.MessageDialog
import sgtmelon.safedialog.OptionsDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.NoteAdapter
import sgtmelon.scriptum.app.factory.DialogFactory
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.databinding.FragmentBinBinding
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.intf.ItemIntf
import sgtmelon.scriptum.office.state.OpenState
import sgtmelon.scriptum.office.utils.AppUtils.inflateBinding
import sgtmelon.scriptum.office.utils.ColorUtils.tintIcon

class BinFragment : Fragment(), BinCallback {

    private val openState = OpenState()

    private lateinit var activity: Activity
    private lateinit var binding: FragmentBinBinding

    private val viewModel: BinViewModel by lazy {
        ViewModelProviders.of(this).get(BinViewModel::class.java)
    }
    private val adapter: NoteAdapter by lazy {
        NoteAdapter(activity)
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

    private fun setupToolbar() {
        toolbar?.title = getString(R.string.title_bin)
        toolbar?.inflateMenu(R.menu.fragment_bin)
        toolbar?.setOnMenuItemClickListener {
            openState.tryInvoke { clearBinDialog.show(fragmentManager, DialogDef.CLEAR_BIN) }
            return@setOnMenuItemClickListener true
        }

        itemClearBin?.tintIcon(activity)

        clearBinDialog.positiveListener =
                DialogInterface.OnClickListener { _, _ -> viewModel.onClickClearBin() }

        clearBinDialog.dismissListener =
                DialogInterface.OnDismissListener { openState.clear() }
    }

    private fun setupRecycler() {
        adapter.clickListener = ItemIntf.ClickListener { _, p -> viewModel.onClickNote(p) }
        adapter.longClickListener = ItemIntf.LongClickListener { _, p -> viewModel.onShowOptionsDialog(p) }

        recyclerView?.itemAnimator = object : DefaultItemAnimator() {
            override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) {
                bind()
            }
        }

        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = adapter

        optionsDialog.onClickListener = DialogInterface.OnClickListener { _, which ->
            viewModel.onResultOptionsDialog(optionsDialog.position, which)
        }
    }

    override fun bind() {
        binding.listEmpty = adapter.itemCount == 0
        binding.executePendingBindings()
    }

    override fun scrollTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun startNote(intent: Intent) = startActivity(intent)

    override fun showOptionsDialog(itemArray: Array<String>, p: Int) {
        optionsDialog.setArguments(itemArray, p)
        optionsDialog.show(fragmentManager, DialogDef.OPTIONS)
    }

    override fun notifyMenuClearBin() {
        itemClearBin?.isVisible = adapter.itemCount != 0
    }

    override fun notifyDataSetChanged(list: MutableList<NoteRepo>) =
            adapter.notifyDataSetChanged(list)

    override fun notifyItemRemoved(p: Int, list: MutableList<NoteRepo>) =
            adapter.notifyItemRemoved(p, list)

}