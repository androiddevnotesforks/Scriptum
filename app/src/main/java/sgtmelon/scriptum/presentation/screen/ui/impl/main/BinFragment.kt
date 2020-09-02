package sgtmelon.scriptum.presentation.screen.ui.impl.main

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ArrayRes
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.FragmentBinBinding
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.presentation.adapter.NoteAdapter
import sgtmelon.scriptum.presentation.control.system.ClipboardControl
import sgtmelon.scriptum.presentation.control.system.callback.IClipboardControl
import sgtmelon.scriptum.presentation.factory.DialogFactory
import sgtmelon.scriptum.presentation.listener.ItemListener
import sgtmelon.scriptum.presentation.screen.ui.ParentFragment
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IBinFragment
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IMainActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IBinViewModel
import javax.inject.Inject

/**
 * Fragment which displays list of deleted notes - [NoteItem].
 */
class BinFragment : ParentFragment(), IBinFragment {

    private val callback: IMainActivity? by lazy { context as? IMainActivity }

    private var binding: FragmentBinBinding? = null

    @Inject internal lateinit var viewModel: IBinViewModel

    private val clipboardControl: IClipboardControl by lazy { ClipboardControl(context) }

    private val openState get() = callback?.openState
    private val dialogFactory by lazy { DialogFactory.Main(context, fm) }

    private val optionsDialog by lazy { dialogFactory.getOptionsDialog() }
    private val clearBinDialog by lazy { dialogFactory.getClearBinDialog() }

    private val adapter: NoteAdapter by lazy {
        NoteAdapter(object : ItemListener.Click {
            override fun onItemClick(view: View, p: Int) {
                openState?.tryInvoke { viewModel.onClickNote(p) }
            }
        }, object : ItemListener.LongClick {
            override fun onItemLongClick(view: View, p: Int) = viewModel.onShowOptionsDialog(p)
        })
    }

    /**
     * Setup manually because after rotation lazy function will return null.
     */
    private var toolbar: Toolbar? = null
    private var itemClearBin: MenuItem? = null

    private var parentContainer: ViewGroup? = null
    private var emptyInfoView: View? = null
    private var progressBar: View? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_bin, container)

        ScriptumApplication.component.getBinBuilder().set(fragment = this).build()
                .inject(fragment = this)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clipboardControl.initLazy()

        viewModel.onSetup()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUpdateData()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }


    override fun setupToolbar() {
        toolbar = view?.findViewById(R.id.toolbar_container)
        toolbar?.apply {
            title = getString(R.string.title_bin)
            inflateMenu(R.menu.fragment_bin)
            setOnMenuItemClickListener {
                openState?.tryInvoke { clearBinDialog.show(fm, DialogFactory.Main.CLEAR_BIN) }
                return@setOnMenuItemClickListener true
            }
        }

        itemClearBin = toolbar?.menu?.findItem(R.id.item_clear)
        activity?.let { itemClearBin?.tintIcon(it) }

        clearBinDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ -> viewModel.onClickClearBin() }
            dismissListener = DialogInterface.OnDismissListener { openState?.clear() }
        }
    }

    override fun setupRecycler(@Theme theme: Int) {
        parentContainer = view?.findViewById(R.id.bin_parent_container)
        emptyInfoView = view?.findViewById(R.id.bin_info_include)
        progressBar = view?.findViewById(R.id.bin_progress)


        adapter.theme = theme

        recyclerView = view?.findViewById(R.id.bin_recycler)
        recyclerView?.let {
            it.setDefaultAnimator { onBindingList() }
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }

        optionsDialog.apply {
            itemListener = DialogInterface.OnClickListener { _, which ->
                viewModel.onResultOptionsDialog(optionsDialog.position, which)
            }
            dismissListener = DialogInterface.OnDismissListener { openState?.clear() }
        }
    }


    override fun beforeLoad() {
        emptyInfoView?.visibility = View.GONE
        progressBar?.visibility = View.GONE
    }

    override fun showProgress() {
        progressBar?.visibility = View.VISIBLE
    }


    override fun onBindingList() {
        progressBar?.visibility = View.GONE

        /**
         * Case without animation need for best performance, without freeze. Because changes
         * on other screens may cause [onBindingList].
         */
        if (adapter.itemCount == 0) {
            emptyInfoView?.visibility = View.VISIBLE
            recyclerView?.visibility = View.INVISIBLE

            emptyInfoView?.alpha = 0f
            emptyInfoView?.animateAlpha(isVisible = true)
        } else {
            emptyInfoView?.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE
        }
    }

    override fun scrollTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun openNoteScreen(item: NoteItem) {
        startActivity(NoteActivity[context ?: return, item])
    }

    override fun showOptionsDialog(itemArray: Array<String>, p: Int) {
        openState?.tryInvoke {
            optionsDialog.setArguments(itemArray, p).show(fm, DialogFactory.Main.OPTIONS)
        }
    }


    override fun notifyMenuClearBin() {
        itemClearBin?.isVisible = adapter.itemCount != 0
    }

    override fun notifyList(list: List<NoteItem>) = adapter.notifyList(list)

    override fun notifyDataSetChanged(list: List<NoteItem>) {
        adapter.setList(list).notifyDataSetChanged()
    }

    override fun notifyItemRemoved(list: List<NoteItem>, p: Int) {
        adapter.setList(list).notifyItemRemoved(p)
    }


    override fun getStringArray(@ArrayRes arrayId: Int): Array<String> = resources.getStringArray(arrayId)


    override fun copyClipboard(text: String) = clipboardControl.copy(text)

}