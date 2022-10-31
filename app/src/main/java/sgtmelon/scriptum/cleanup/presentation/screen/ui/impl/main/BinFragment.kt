package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ArrayRes
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.animateAlpha
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.main.IBinFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IBinViewModel
import sgtmelon.scriptum.databinding.FragmentBinBinding
import sgtmelon.scriptum.infrastructure.adapter.NoteAdapter
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NoteClickListener
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.screen.main.callback.ScrollTopCallback
import sgtmelon.scriptum.infrastructure.screen.parent.ParentFragment
import sgtmelon.scriptum.infrastructure.utils.isGone
import sgtmelon.scriptum.infrastructure.utils.isInvisible
import sgtmelon.scriptum.infrastructure.utils.isVisible
import sgtmelon.scriptum.infrastructure.utils.makeGone
import sgtmelon.scriptum.infrastructure.utils.makeInvisible
import sgtmelon.scriptum.infrastructure.utils.makeVisible
import sgtmelon.scriptum.infrastructure.utils.setDefaultAnimator
import sgtmelon.scriptum.infrastructure.utils.tintIcon
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener

/**
 * Fragment which displays list of deleted notes - [NoteItem].
 */
class BinFragment : ParentFragment<FragmentBinBinding>(),
    IBinFragment,
    ScrollTopCallback {

    override val layoutId: Int = R.layout.fragment_bin

    //region Variables

    @Inject lateinit var viewModel: IBinViewModel

    private val dialogs by lazy { DialogFactory.Main(context, fm) }

    private val optionsDialog by lazy { dialogs.getOptions() }
    private val clearBinDialog by lazy { dialogs.getClearBin() }

    private val adapter: NoteAdapter by lazy {
        NoteAdapter(object : NoteClickListener {
            override fun onNoteClick(item: NoteItem) = openNoteScreen(item)

            override fun onNoteLongClick(item: NoteItem, p: Int) {
                viewModel.onShowOptionsDialog(item, p)
            }
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

    //endregion

    //region System

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onSetup()
    }

    override fun inject(component: ScriptumComponent) {
        component.getBinBuilder()
            .set(fragment = this)
            .build()
            .inject(fragment = this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUpdateData()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    //endregion

    override fun setupToolbar() {
        toolbar = view?.findViewById(R.id.toolbar)
        toolbar?.apply {
            title = getString(R.string.title_bin)
            inflateMenu(R.menu.fragment_bin)
            setOnMenuItemClickListener {
                parentOpen?.attempt {
                    clearBinDialog.safeShow(
                        DialogFactory.Main.CLEAR_BIN,
                        owner = this@BinFragment
                    )
                }
                return@setOnMenuItemClickListener true
            }
        }

        itemClearBin = toolbar?.menu?.findItem(R.id.item_clear)
        activity?.let { itemClearBin?.tintIcon(it) }

        clearBinDialog.apply {
            onPositiveClick { viewModel.onClickClearBin() }
            onDismiss { parentOpen?.clear() }
        }
    }

    override fun setupRecycler() {
        parentContainer = view?.findViewById(R.id.bin_parent_container)
        emptyInfoView = view?.findViewById(R.id.info_include)
        progressBar = view?.findViewById(R.id.bin_progress)

        recyclerView = view?.findViewById(R.id.bin_recycler)
        recyclerView?.let {
            it.setDefaultAnimator { onBindingList() }

            it.addOnScrollListener(RecyclerOverScrollListener())
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }
    }

    override fun setupDialog() {
        optionsDialog.apply {
            onItem { viewModel.onResultOptionsDialog(optionsDialog.position, it) }
            onDismiss { parentOpen?.clear() }
        }
    }


    /**
     * For first time [recyclerView] visibility flag set inside xml file.
     */
    override fun prepareForLoad() {
        emptyInfoView?.makeGone()
        progressBar?.makeGone()
    }

    override fun showProgress() {
        progressBar?.makeVisible()
    }

    override fun hideEmptyInfo() {
        emptyInfoView?.makeGone()
    }


    override fun onBindingList() {
        progressBar?.makeGone()

        /**
         * Case without animation need for best performance, without freeze. Because changes
         * on other screens may cause [onBindingList].
         */
        if (adapter.itemCount == 0) {
            /**
             * Prevent useless calls from [RecyclerView.setDefaultAnimator].
             */
            if (emptyInfoView.isVisible() && recyclerView.isInvisible()) return

            emptyInfoView?.makeVisible()
            recyclerView?.makeInvisible()

            emptyInfoView?.alpha = 0f
            emptyInfoView?.animateAlpha(isVisible = true)
        } else {
            /**
             * Prevent useless calls from [RecyclerView.setDefaultAnimator].
             */
            if (emptyInfoView.isGone() && recyclerView.isVisible()) return

            emptyInfoView?.makeGone()
            recyclerView?.makeVisible()
        }
    }

    override fun scrollTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    override fun openNoteScreen(item: NoteItem) {
        parentOpen?.attempt { startActivity(InstanceFactory.Note[context ?: return, item]) }
    }

    override fun showOptionsDialog(title: String, itemArray: Array<String>, p: Int) {
        parentOpen?.attempt {
            optionsDialog.title = title
            optionsDialog.setArguments(itemArray, p)
                .safeShow(DialogFactory.Main.OPTIONS, owner = this)
        }
    }


    override fun notifyMenuClearBin() {
        itemClearBin?.isVisible = adapter.itemCount != 0
    }

    override fun notifyList(list: List<NoteItem>) = adapter.notifyList(list)


    override fun getStringArray(@ArrayRes arrayId: Int): Array<String> = resources.getStringArray(arrayId)


    override fun copyClipboard(text: String) = delegators.clipboard.copy(text)

}