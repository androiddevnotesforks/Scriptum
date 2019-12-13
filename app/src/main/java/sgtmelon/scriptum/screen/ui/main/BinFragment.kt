package sgtmelon.scriptum.screen.ui.main

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.control.clipboard.ClipboardControl
import sgtmelon.scriptum.control.clipboard.IClipboardControl
import sgtmelon.scriptum.databinding.FragmentBinBinding
import sgtmelon.scriptum.extension.createVisibleAnim
import sgtmelon.scriptum.extension.inflateBinding
import sgtmelon.scriptum.extension.initLazy
import sgtmelon.scriptum.extension.tintIcon
import sgtmelon.scriptum.factory.DialogFactory
import sgtmelon.scriptum.factory.ViewModelFactory
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.screen.ui.ParentFragment
import sgtmelon.scriptum.screen.ui.callback.main.IBinFragment
import sgtmelon.scriptum.screen.ui.callback.main.IMainActivity
import sgtmelon.scriptum.screen.ui.note.NoteActivity

/**
 * Fragment which displays list of deleted notes - [NoteItem]
 */
class BinFragment : ParentFragment(), IBinFragment {

    private val callback: IMainActivity? by lazy { context as? IMainActivity }

    private var binding: FragmentBinBinding? = null

    private val iViewModel by lazy { ViewModelFactory.getBinViewModel(fragment = this) }

    private val iClipboardControl: IClipboardControl by lazy { ClipboardControl(context) }

    private val openState get() = callback?.openState
    private val dialogFactory by lazy { DialogFactory.Main(context, fm) }

    private val optionsDialog by lazy { dialogFactory.getOptionsDialog() }
    private val clearBinDialog by lazy { dialogFactory.getClearBinDialog() }

    private val adapter: NoteAdapter by lazy {
        NoteAdapter(object : ItemListener.Click {
            override fun onItemClick(view: View, p: Int) {
                openState?.tryInvoke { iViewModel.onClickNote(p) }
            }
        }, object : ItemListener.LongClick {
            override fun onItemLongClick(view: View, p: Int) = iViewModel.onShowOptionsDialog(p)
        })
    }

    private var toolbar: Toolbar? = null
    private var itemClearBin: MenuItem? = null

    private var parentContainer: ViewGroup? = null
    private var emptyInfoView: View? = null
    private var progressBar: View? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.fragment_bin, container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iClipboardControl.initLazy()

        iViewModel.onSetup()
    }

    override fun onResume() {
        super.onResume()

        emptyInfoView?.visibility = View.GONE
        progressBar?.visibility = View.GONE

        iViewModel.onUpdateData()
    }

    override fun onDestroy() {
        super.onDestroy()
        iViewModel.onDestroy()
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
            positiveListener = DialogInterface.OnClickListener { _, _ -> iViewModel.onClickClearBin() }
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
            it.itemAnimator = object : DefaultItemAnimator() {
                override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) = onBindingList()
            }

            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }

        optionsDialog.apply {
            itemListener = DialogInterface.OnClickListener { _, which ->
                iViewModel.onResultOptionsDialog(optionsDialog.position, which)
            }
            dismissListener = DialogInterface.OnDismissListener { openState?.clear() }
        }
    }

    override fun showProgress() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun onBindingList() {
        progressBar?.visibility = View.GONE

        val isListEmpty = adapter.itemCount == 0

        /**
         * Use time equal 0
         *
         * Because you on another screen and restore item to that screen, after return you will
         * cause [onBindingList]. Zero time need for best performance, without freeze
         */
        val durationId = if (isListEmpty) R.integer.info_fade_time else R.integer.info_skip_time
        parentContainer?.createVisibleAnim(emptyInfoView, isListEmpty, durationId)

        binding?.apply { this.isListEmpty = isListEmpty }?.executePendingBindings()
    }

    override fun scrollTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun startNoteActivity(item: NoteItem) {
        val context = context ?: return

        startActivity(NoteActivity[context, item])
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


    override fun copyClipboard(text: String) = iClipboardControl.copy(text)

}