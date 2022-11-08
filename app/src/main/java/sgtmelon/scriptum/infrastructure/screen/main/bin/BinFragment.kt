package sgtmelon.scriptum.infrastructure.screen.main.bin

import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import javax.inject.Inject
import sgtmelon.extensions.collect
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.annotation.Options
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.databinding.FragmentBinBinding
import sgtmelon.scriptum.infrastructure.adapter.NoteAdapter
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NoteClickListener
import sgtmelon.scriptum.infrastructure.animation.ShowListAnimation
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.screen.main.callback.ScrollTopCallback
import sgtmelon.scriptum.infrastructure.screen.parent.BindingFragment
import sgtmelon.scriptum.infrastructure.utils.getItem
import sgtmelon.scriptum.infrastructure.utils.tintIcon
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener

/**
 * Screen to display the list of deleted notes.
 */
class BinFragment : BindingFragment<FragmentBinBinding>(),
    ScrollTopCallback {

    override val layoutId: Int = R.layout.fragment_bin

    @Inject lateinit var viewModel: BinViewModel

    private val animation = ShowListAnimation()

    private val dialogs by lazy { DialogFactory.Main(context, fm) }
    private val optionsDialog by lazy { dialogs.getOptions() }
    private val clearBinDialog by lazy { dialogs.getClearBin() }

    private val adapter: NoteAdapter by lazy {
        NoteAdapter(object : NoteClickListener {
            override fun onNoteClick(item: NoteItem) = openNoteScreen(item)
            override fun onNoteLongClick(item: NoteItem, p: Int) = showOptionsDialog(item, p)
        })
    }

    private val itemClearBin: MenuItem?
        get() = binding?.toolbarInclude?.toolbar?.getItem(R.id.item_clear)

    //region System

    override fun inject(component: ScriptumComponent) {
        component.getBinBuilder()
            .set(owner = this)
            .build()
            .inject(fragment = this)
    }

    override fun setupView() {
        super.setupView()

        binding?.toolbarInclude?.toolbar?.apply {
            title = getString(R.string.title_bin)
            inflateMenu(R.menu.fragment_bin)
            setOnMenuItemClickListener {
                showClearBinDialog()
                return@setOnMenuItemClickListener true
            }

            itemClearBin?.tintIcon(context)
        }

        binding?.recyclerView?.let {
            it.addOnScrollListener(RecyclerOverScrollListener())
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }
    }

    override fun setupDialogs() {
        super.setupDialogs()

        clearBinDialog.apply {
            onPositiveClick { viewModel.clearRecyclerBin() }
            onDismiss { parentOpen?.clear() }
        }

        optionsDialog.apply {
            onItem { onOptionSelect(position, it) }
            onDismiss { parentOpen?.clear() }
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.showList.observe(this) {
            val binding = binding ?: return@observe

            animation.startListFade(
                it, binding.parentContainer, binding.progressBar,
                binding.recyclerView, binding.infoInclude.parentContainer
            )
        }
        viewModel.itemList.observe(this) {
            adapter.notifyList(it)
            itemClearBin?.isVisible = it.isNotEmpty()
        }
    }

    override fun onResume() {
        super.onResume()

        /**
         * Lifecycle observer not working inside viewModel when changing pages. Check out custom
         * call of this function inside parent activity (during fragment transaction).
         */
        viewModel.updateData()
    }

    //endregion

    private fun openNoteScreen(item: NoteItem) {
        val context = context ?: return

        parentOpen?.attempt { startActivity(InstanceFactory.Note[context, item]) }
    }

    private fun showOptionsDialog(item: NoteItem, p: Int) {
        parentOpen?.attempt {
            val title = item.name.ifEmpty { getString(R.string.empty_note_name) }
            val itemArray = resources.getStringArray(R.array.dialog_menu_bin)

            optionsDialog.title = title
            optionsDialog.setArguments(itemArray, p)
                .safeShow(DialogFactory.Main.OPTIONS, owner = this)
        }
    }

    private fun onOptionSelect(p: Int, which: Int) {
        when (which) {
            Options.Bin.RESTORE -> viewModel.restoreNote(p)
            Options.Bin.COPY -> viewModel.getNoteText(p).collect(owner = this) {
                delegators.clipboard.copy(it)
            }
            Options.Bin.CLEAR -> viewModel.clearNote(p)
        }
    }

    private fun showClearBinDialog() {
        parentOpen?.attempt {
            clearBinDialog.safeShow(DialogFactory.Main.CLEAR_BIN, owner = this@BinFragment)
        }
    }

    override fun scrollTop() {
        binding?.recyclerView?.smoothScrollToPosition(0)
    }
}