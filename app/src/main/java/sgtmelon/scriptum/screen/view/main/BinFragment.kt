package sgtmelon.scriptum.screen.view.main

import android.app.Activity
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
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.databinding.FragmentBinBinding
import sgtmelon.scriptum.extension.createVisibleAnim
import sgtmelon.scriptum.extension.inflateBinding
import sgtmelon.scriptum.extension.tintIcon
import sgtmelon.scriptum.factory.DialogFactory
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.screen.callback.main.BinCallback
import sgtmelon.scriptum.screen.vm.main.BinViewModel

/**
 * Фрагмент для отображения списка удалённых заметок - [NoteItem]
 *
 * @author SerjantArbuz
 */
class BinFragment : Fragment(), BinCallback {

    private var binding: FragmentBinBinding? = null

    private val viewModel: BinViewModel by lazy {
        ViewModelProviders.of(this).get(BinViewModel::class.java).apply {
            callback = this@BinFragment
        }
    }

    private val openState = OpenState()
    private val optionsDialog by lazy { DialogFactory.Main.getOptionsDialog(fragmentManager) }
    private val clearBinDialog by lazy {
        DialogFactory.Main.getClearBinDialog(activity as Activity, fragmentManager)
    }

    private lateinit var adapter: NoteAdapter

    private var toolbar: Toolbar? = null
    private var itemClearBin: MenuItem? = null

    private var parentContainer: ViewGroup? = null
    private var emptyInfoView: View? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_bin, container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        openState.get(savedInstanceState)
        viewModel.onSetup()
    }

    override fun onResume() {
        super.onResume()

        openState.clear()
        viewModel.onUpdateData()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply { openState.save(bundle = this) })

    override fun setupToolbar() {
        toolbar = view?.findViewById(R.id.toolbar_container)
        toolbar?.apply {
            title = getString(R.string.title_bin)
            inflateMenu(R.menu.fragment_bin)
            setOnMenuItemClickListener {
                fragmentManager?.let {
                    openState.tryInvoke { clearBinDialog.show(it, DialogFactory.Main.CLEAR_BIN) }
                }
                return@setOnMenuItemClickListener true
            }
        }

        itemClearBin = toolbar?.menu?.findItem(R.id.item_clear)
        activity?.let { itemClearBin?.tintIcon(it) }

        clearBinDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ -> viewModel.onClickClearBin() }
            dismissListener = DialogInterface.OnDismissListener { openState.clear() }
        }
    }

    override fun setupRecycler(@Theme theme: Int) {
        parentContainer = view?.findViewById(R.id.bin_parent_container)
        emptyInfoView = view?.findViewById(R.id.bin_info_include)

        adapter = NoteAdapter(theme,
                ItemListener.Click { _, p -> openState.tryInvoke { viewModel.onClickNote(p) } },
                ItemListener.LongClick { _, p -> viewModel.onShowOptionsDialog(p) }
        )

        recyclerView = view?.findViewById(R.id.bin_recycler)
        recyclerView?.let {
            it.itemAnimator = object : DefaultItemAnimator() {
                override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) = bind()
            }

            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }

        optionsDialog.onClickListener = DialogInterface.OnClickListener { _, which ->
            viewModel.onResultOptionsDialog(optionsDialog.position, which)
        }
    }

    override fun bind() {
        val empty = adapter.itemCount == 0

        parentContainer?.createVisibleAnim(emptyInfoView, empty, if (!empty) 0 else 200)

        binding?.apply { isListEmpty = empty }?.executePendingBindings()
    }

    override fun scrollTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun showOptionsDialog(itemArray: Array<String>, p: Int) {
        fragmentManager?.let {
            optionsDialog.apply { setArguments(itemArray, p) }.show(it, DialogFactory.Main.OPTIONS)
        }
    }

    override fun notifyMenuClearBin() {
        itemClearBin?.isVisible = adapter.itemCount != 0
    }

    override fun notifyDataSetChanged(list: MutableList<NoteModel>) =
            adapter.notifyDataSetChanged(list)

    override fun notifyItemRemoved(p: Int, list: MutableList<NoteModel>) =
            adapter.notifyItemRemoved(p, list)

}