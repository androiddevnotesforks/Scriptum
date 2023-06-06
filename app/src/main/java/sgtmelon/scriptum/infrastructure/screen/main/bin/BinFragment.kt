package sgtmelon.scriptum.infrastructure.screen.main.bin

import android.content.Context
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject
import sgtmelon.extensions.collect
import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.safedialog.dialog.OptionsDialog
import sgtmelon.safedialog.utils.DialogStorage
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.databinding.FragmentBinBinding
import sgtmelon.scriptum.infrastructure.adapter.NoteAdapter
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NoteClickListener
import sgtmelon.scriptum.infrastructure.animation.ShowListAnimation
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.screen.Screens
import sgtmelon.scriptum.infrastructure.screen.main.callback.ScrollTopCallback
import sgtmelon.scriptum.infrastructure.screen.parent.BindingFragment
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListScreen
import sgtmelon.scriptum.infrastructure.utils.extensions.getItem
import sgtmelon.scriptum.infrastructure.utils.extensions.tintIcon
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener
import sgtmelon.scriptum.infrastructure.model.key.dialog.BinDialogOptions as Options

/**
 * Screen to display the list of deleted notes.
 */
class BinFragment : BindingFragment<FragmentBinBinding>(),
    ListScreen<NoteItem>,
    ScrollTopCallback {

    // TODO bugs:
    // 1. similar glitch as in NoteFragment (with single item delete animation of info not smooth)
    //    May be skip animation?

    override val layoutId: Int = R.layout.fragment_bin

    @Inject override lateinit var viewModel: BinViewModel

    private val listAnimation = ShowListAnimation()

    private val dialogs by lazy { DialogFactory.Main(resources) }
    private val optionsDialog = DialogStorage(
        DialogFactory.Main.OPTIONS, owner = this,
        create = { dialogs.getOptions() },
        setup = { setupOptionsDialog(it) }
    )
    private val clearBinDialog = DialogStorage(
        DialogFactory.Main.CLEAR_BIN, owner = this,
        create = { dialogs.getClearBin() },
        setup = { setupClearBinDialog(it) }
    )

    override val adapter: NoteAdapter by lazy {
        NoteAdapter(object : NoteClickListener {
            override fun onNoteClick(item: NoteItem) = openNoteScreen(item)
            override fun onNoteLongClick(item: NoteItem, p: Int) = showOptionsDialog(item, p)
        })
    }
    override val layoutManager by lazy { LinearLayoutManager(context) }
    override val recyclerView: RecyclerView? get() = binding?.recyclerView

    private val itemClearBin: MenuItem?
        get() = binding?.appBar?.toolbar?.getItem(R.id.item_clear)

    //region System

    override fun inject(component: ScriptumComponent) {
        component.getBinBuilder()
            .set(owner = this)
            .build()
            .inject(fragment = this)
    }

    override fun setupView(context: Context) {
        super.setupView(context)

        binding?.appBar?.toolbar?.apply {
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
            it.setHasFixedSize(false) /** The height of all items may be not the same. */
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
    }

    override fun setupDialogs() {
        super.setupDialogs()

        optionsDialog.restore()
        clearBinDialog.restore()
    }

    private fun setupOptionsDialog(dialog: OptionsDialog): Unit = with(dialog) {
        onItem { onOptionSelect(position, it) }
        onDismiss {
            optionsDialog.release()
            parentOpen?.clear()
        }
    }

    private fun setupClearBinDialog(dialog: MessageDialog): Unit = with(dialog) {
        onPositiveClick { viewModel.clearRecyclerBin() }
        onDismiss {
            clearBinDialog.release()
            parentOpen?.clear()
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.list.show.observe(this) {
            val binding = binding ?: return@observe
            listAnimation.startFade(
                it, binding.parentContainer, binding.progressBar,
                binding.recyclerView, binding.emptyInfo.root
            )
        }
        viewModel.list.data.observe(this) {
            onListUpdate(it)
            itemClearBin?.isVisible = it.isNotEmpty()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateData()
    }

    //endregion

    private fun openNoteScreen(item: NoteItem) {
        val context = context ?: return

        parentOpen?.attempt { startActivity(Screens.Note.toExist(context, item)) }
    }

    private fun showOptionsDialog(item: NoteItem, p: Int) {
        parentOpen?.attempt {
            val title = item.name.ifEmpty { getString(R.string.empty_note_name) }
            val itemArray = resources.getStringArray(R.array.dialog_menu_bin)

            optionsDialog.show {
                this.title = title
                setArguments(itemArray, p)
            }
        }
    }

    private fun onOptionSelect(p: Int, which: Int) {
        when (Options.values().getOrNull(which) ?: return) {
            Options.RESTORE -> viewModel.restoreNote(p)
            Options.COPY -> viewModel.getNoteText(p).collect(owner = this) {
                system?.clipboard?.copy(it)
            }
            Options.CLEAR -> viewModel.clearNote(p)
        }
    }

    private fun showClearBinDialog() {
        parentOpen?.attempt { clearBinDialog.show() }
    }

    override fun scrollTop() {
        binding?.recyclerView?.smoothScrollToPosition(0)
    }
}