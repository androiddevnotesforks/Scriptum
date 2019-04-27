package sgtmelon.scriptum.screen.view.main

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
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.databinding.FragmentBinBinding
import sgtmelon.scriptum.factory.DialogFactory
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.AppUtils.inflateBinding
import sgtmelon.scriptum.office.utils.ColorUtils.tintIcon
import sgtmelon.scriptum.screen.callback.main.BinCallback
import sgtmelon.scriptum.screen.view.main.RankFragment.Companion.createVisibleAnim
import sgtmelon.scriptum.screen.vm.main.BinViewModel

/**
 * Фрагмент для отображения списка удалённых заметок - [NoteItem]
 *
 * @author SerjantArbuz
 */
class BinFragment : Fragment(), BinCallback {

    // TODO double open note - поправить

    private lateinit var activity: Activity

    private var binding: FragmentBinBinding? = null

    private val viewModel: BinViewModel by lazy {
        ViewModelProviders.of(this).get(BinViewModel::class.java).apply {
            callback = this@BinFragment
        }
    }

    private val adapter by lazy {
        NoteAdapter(activity,
                ItemListener.ClickListener { _, p -> viewModel.onClickNote(p) },
                ItemListener.LongClickListener { _, p -> viewModel.onShowOptionsDialog(p) }
        )
    }

    private var toolbar: Toolbar? = null
    private var itemClearBin: MenuItem? = null

    private var parentContainer: ViewGroup? = null
    private var emptyInfoView: View? = null
    private var recyclerView: RecyclerView? = null

    private val openState = OpenState()
    private val optionsDialog by lazy { DialogFactory.getOptionsDialog(fragmentManager) }
    private val clearBinDialog by lazy { DialogFactory.getClearBinDialog(activity, fragmentManager) }

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
        binding = inflater.inflateBinding(R.layout.fragment_bin, container)
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
        toolbar = view?.findViewById(R.id.toolbar_container)
        toolbar?.apply {
            title = getString(R.string.title_bin)
            inflateMenu(R.menu.fragment_bin)
            setOnMenuItemClickListener {
                openState.tryInvoke { clearBinDialog.show(fragmentManager, DialogDef.CLEAR_BIN) }
                return@setOnMenuItemClickListener true
            }
        }

        itemClearBin = toolbar?.menu?.findItem(R.id.item_clear)
        itemClearBin?.tintIcon(activity)

        clearBinDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ -> viewModel.onClickClearBin() }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }
    }

    private fun setupRecycler() {
        parentContainer = view?.findViewById(R.id.bin_parent_container)
        emptyInfoView = view?.findViewById(R.id.bin_info_include)

        recyclerView = view?.findViewById(R.id.bin_recycler)
        recyclerView?.itemAnimator = object : DefaultItemAnimator() {
            override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) = bind()
        }

        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = adapter

        optionsDialog.onClickListener = DialogInterface.OnClickListener { _, which ->
            viewModel.onResultOptionsDialog(optionsDialog.position, which)
        }
    }

    override fun bind() {
        val empty = adapter.itemCount == 0

        parentContainer?.createVisibleAnim(empty, emptyInfoView, if (!empty) 0 else 200)

        binding?.apply { listEmpty = empty }?.executePendingBindings()
    }

    override fun scrollTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun startNote(intent: Intent) = startActivity(intent)

    override fun showOptionsDialog(itemArray: Array<String>, p: Int) = optionsDialog.apply {
        setArguments(itemArray, p)
    }.show(fragmentManager, DialogDef.OPTIONS)

    override fun notifyMenuClearBin() {
        itemClearBin?.isVisible = adapter.itemCount != 0
    }

    override fun notifyDataSetChanged(list: MutableList<NoteModel>) =
            adapter.notifyDataSetChanged(list)

    override fun notifyItemRemoved(p: Int, list: MutableList<NoteModel>) =
            adapter.notifyItemRemoved(p, list)

}