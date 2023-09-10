package sgtmelon.scriptum.infrastructure.screen.main.notes

import android.content.IntentFilter
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.extensions.collect
import sgtmelon.safedialog.dialog.OptionsDialog
import sgtmelon.safedialog.dialog.time.DateDialog
import sgtmelon.safedialog.dialog.time.TimeDialog
import sgtmelon.safedialog.utils.DialogStorage
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.databinding.FragmentNotesBinding
import sgtmelon.scriptum.infrastructure.adapter.NoteAdapter
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NoteClickListener
import sgtmelon.scriptum.infrastructure.animation.ShowListAnimation
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState
import sgtmelon.scriptum.infrastructure.receiver.screen.InfoChangeReceiver
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.Screens
import sgtmelon.scriptum.infrastructure.screen.main.callback.ScrollTopCallback
import sgtmelon.scriptum.infrastructure.screen.parent.BindingFragment
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListScreen
import sgtmelon.scriptum.infrastructure.screen.preference.PreferenceScreen
import sgtmelon.scriptum.infrastructure.utils.extensions.getItem
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.tintIcon
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerMainFabListener
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener
import java.util.Calendar
import javax.inject.Inject
import sgtmelon.scriptum.infrastructure.model.key.dialog.NotesDialogOptions as Options

/**
 * Screen to display the list of main notes.
 */
class NotesFragment : BindingFragment<FragmentNotesBinding>(),
    ListScreen<NoteItem>,
    Toolbar.OnMenuItemClickListener,
    ScrollTopCallback {

    override val layoutId: Int = R.layout.fragment_notes

    @Inject override lateinit var viewModel: NotesViewModel

    private val listAnimation = ShowListAnimation()

    private val unbindNoteReceiver by lazy { UnbindNoteReceiver[viewModel] }
    private val infoChangeReceiver by lazy { InfoChangeReceiver[viewModel] }

    private val dialogs by lazy { DialogFactory.Main(resources) }
    private val optionsDialog = DialogStorage(
        DialogFactory.Main.OPTIONS, owner = this,
        create = { dialogs.getOptions() },
        setup = { setupOptionsDialog(it) }
    )
    private val dateDialog = DialogStorage(
        DialogFactory.Main.DATE, owner = this,
        create = { dialogs.getDate() },
        setup = { setupDateDialog(it) }
    )
    private val timeDialog = DialogStorage(
        DialogFactory.Main.TIME, owner = this,
        create = { dialogs.getTime() },
        setup = { setupTimeDialog(it) }
    )

    override val adapter: NoteAdapter by lazy {
        NoteAdapter(object : NoteClickListener {
            override fun onNoteClick(item: NoteItem) = openNoteScreen(item)
            override fun onNoteLongClick(item: NoteItem, p: Int) = showOptionsDialog(item, p)
        })
    }
    override val layoutManager by lazy { LinearLayoutManager(context) }
    override val recyclerView: RecyclerView? get() = binding?.recyclerView

    //region System

    override fun inject(component: ScriptumComponent) {
        component.getNotesBuilder()
            .set(owner = this)
            .build()
            .inject(fragment = this)
    }

    override fun setupView() {
        super.setupView()

        binding?.appBar?.toolbar?.apply {
            title = getString(R.string.title_notes)
            inflateMenu(R.menu.fragment_notes)
            setOnMenuItemClickListener(this@NotesFragment)

            getItem(R.id.item_notifications).tintIcon(context)
            getItem(R.id.item_preferences).tintIcon(context)
        }

        binding?.recyclerView?.let {
            it.addOnScrollListener(RecyclerOverScrollListener())
            it.setHasFixedSize(false) /** The height of all items may be not the same. */
            it.layoutManager = layoutManager
            it.adapter = adapter

            val fabCallback = activity as? RecyclerMainFabListener.Callback
            it.addOnScrollListener(RecyclerMainFabListener(lifecycle, fabCallback))
        }
    }

    override fun setupDialogs() {
        super.setupDialogs()

        optionsDialog.restore()
        dateDialog.restore()
        timeDialog.restore()
    }

    private fun setupOptionsDialog(dialog: OptionsDialog): Unit = with(dialog) {
        onItem { onOptionSelect(position, it) }
        onDismiss {
            optionsDialog.release()
            parentOpen?.clear()
        }
    }

    private fun setupDateDialog(dialog: DateDialog): Unit = with(dialog) {
        onPositiveClick {
            parentOpen?.skipClear = true
            viewModel.notificationsDateList.collect(owner = this@NotesFragment) {
                showTimeDialog(calendar, it, position)
            }
        }
        onNeutralClick {
            viewModel.deleteNoteNotification(position).collect(owner = this@NotesFragment) {
                system?.broadcast?.sendCancelAlarm(it)
                system?.broadcast?.sendNotifyInfoBind()
            }
        }
        onDismiss {
            dateDialog.release()
            parentOpen?.clear()
        }
    }

    private fun setupTimeDialog(dialog: TimeDialog): Unit = with(dialog) {
        onPositiveClick {
            viewModel.setNoteNotification(calendar, position).collect(owner = this@NotesFragment) {
                val (item, calendar) = it
                system?.broadcast?.sendSetAlarm(item, calendar)
                system?.broadcast?.sendNotifyInfoBind()
            }
        }
        onDismiss {
            timeDialog.release()
            parentOpen?.clear()
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.list.show.observe(this) {
            val binding = binding ?: return@observe
            listAnimation.start(
                it, binding.parentContainer, binding.progressBar,
                binding.recyclerView, binding.emptyInfo.root
            )
        }
        viewModel.list.data.observe(this) { onListUpdate(it) }
        viewModel.isListHide.observe(this) { observeListHide(it) }
    }

    override fun registerReceivers() {
        super.registerReceivers()
        context?.registerReceiver(unbindNoteReceiver, IntentFilter(ReceiverData.Filter.NOTES))
        context?.registerReceiver(infoChangeReceiver, IntentFilter(ReceiverData.Filter.NOTES))
    }

    override fun unregisterReceivers() {
        super.unregisterReceivers()
        context?.unregisterReceiver(unbindNoteReceiver)
        context?.unregisterReceiver(infoChangeReceiver)
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateData()
    }

    private fun observeListHide(isListHide: Boolean) {
        val titleId = if (isListHide) {
            R.string.info_notes_hide_title
        } else {
            R.string.info_notes_empty_title
        }
        binding?.emptyInfo?.titleText?.setText(titleId)

        val subtitleId = if (isListHide) {
            R.string.info_notes_hide_details
        } else {
            R.string.info_notes_empty_details
        }
        binding?.emptyInfo?.detailsText?.setText(subtitleId)
    }

    //endregion

    private fun openNoteScreen(item: NoteItem) {
        val context = context ?: return

        parentOpen?.attempt { startActivity(Screens.Note.toExist(context, item)) }
    }

    private fun showOptionsDialog(item: NoteItem, p: Int) {
        parentOpen?.attempt {
            parentOpen?.tag = OpenState.Tag.DIALOG

            val (title, itemArray) = getOptionsDialogData(item)

            optionsDialog.show {
                this.title = title
                setArguments(itemArray, p)
            }
        }
    }

    private fun getOptionsDialogData(item: NoteItem): Pair<String, Array<String>> {
        val title = item.name.ifEmpty { getString(R.string.empty_note_name) }

        val itemArray: Array<String> = resources.getStringArray(
            when (item) {
                is NoteItem.Text -> R.array.dialog_menu_text
                is NoteItem.Roll -> R.array.dialog_menu_roll
            }
        )

        itemArray[Options.NOTIFICATION.ordinal] = if (item.haveAlarm) {
            getString(R.string.dialog_menu_notification_update)
        } else {
            getString(R.string.dialog_menu_notification_set)
        }

        itemArray[Options.BIND.ordinal] = if (item.isStatus) {
            getString(R.string.dialog_menu_status_unbind)
        } else {
            getString(R.string.dialog_menu_status_bind)
        }

        return title to itemArray
    }

    private fun onOptionSelect(p: Int, which: Int) {
        when (Options.values().getOrNull(which) ?: return) {
            Options.NOTIFICATION -> {
                parentOpen?.skipClear = true
                viewModel.getNoteNotification(p).collect(owner = this) {
                    val (calendar, haveAlarm) = it
                    showDateDialog(calendar, haveAlarm, p)
                }
            }
            Options.BIND -> viewModel.updateNoteBind(p).collect(owner = this) {
                system?.broadcast?.sendNotifyNotesBind()
            }
            Options.CONVERT -> viewModel.convertNote(p).collect(owner = this) {
                system?.broadcast?.sendNotifyNotesBind()
            }
            Options.COPY -> viewModel.getNoteText(p).collect(owner = this) {
                system?.clipboard?.copy(it)
            }
            Options.DELETE -> viewModel.deleteNote(p).collect(owner = this) {
                /** We cant surely say BIN page will display a list. */
                system?.broadcast?.sendInfoChangeUi(ShowListState.List, ReceiverData.Filter.BIN)

                system?.broadcast?.sendCancelAlarm(it)
                system?.broadcast?.sendCancelNoteBind(it)
                system?.broadcast?.sendNotifyInfoBind()
            }
        }
    }

    private fun showDateDialog(calendar: Calendar, resetVisible: Boolean, p: Int) {
        parentOpen?.attempt(OpenState.Tag.DIALOG) {
            dateDialog.show { setArguments(calendar, resetVisible, p) }
        }
    }

    private fun showTimeDialog(calendar: Calendar, dateList: List<String>, p: Int) {
        parentOpen?.attempt(OpenState.Tag.DIALOG) {
            timeDialog.show { setArguments(calendar, dateList, p) }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        val context = context ?: return false

        parentOpen?.attempt {
            val intent = when (item?.itemId) {
                R.id.item_notifications -> Screens.toNotifications(context)
                R.id.item_preferences -> Screens.toPreference(context, PreferenceScreen.MENU)
                else -> return false
            }

            startActivity(intent)
        }

        return true
    }

    override fun scrollTop() {
        binding?.recyclerView?.smoothScrollToPosition(0)
    }
}