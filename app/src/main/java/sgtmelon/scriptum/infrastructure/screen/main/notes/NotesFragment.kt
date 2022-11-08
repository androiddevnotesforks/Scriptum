package sgtmelon.scriptum.infrastructure.screen.main.notes

import android.content.IntentFilter
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.Calendar
import javax.inject.Inject
import sgtmelon.extensions.collect
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.annotation.Options
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.databinding.FragmentNotesBinding
import sgtmelon.scriptum.infrastructure.adapter.NoteAdapter
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NoteClickListener
import sgtmelon.scriptum.infrastructure.animation.ShowListAnimation
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData
import sgtmelon.scriptum.infrastructure.model.key.PreferenceScreen
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.main.callback.ScrollTopCallback
import sgtmelon.scriptum.infrastructure.screen.parent.BindingFragment
import sgtmelon.scriptum.infrastructure.utils.tintIcon
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerMainFabListener
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener

/**
 * Screen to display the list of main notes.
 */
class NotesFragment : BindingFragment<FragmentNotesBinding>(),
    Toolbar.OnMenuItemClickListener,
    ScrollTopCallback {

    override val layoutId: Int = R.layout.fragment_notes

    @Inject lateinit var viewModel: NotesViewModel

    private val animation = ShowListAnimation()

    private val unbindNoteReceiver by lazy { UnbindNoteReceiver[viewModel] }

    private val dialogs by lazy { DialogFactory.Main(context, fm) }
    private val optionsDialog by lazy { dialogs.getOptions() }
    private val dateDialog by lazy { dialogs.getDate() }
    private val timeDialog by lazy { dialogs.getTime() }

    private val adapter: NoteAdapter by lazy {
        NoteAdapter(object : NoteClickListener {
            override fun onNoteClick(item: NoteItem) = openNoteScreen(item)
            override fun onNoteLongClick(item: NoteItem, p: Int) = showOptionsDialog(item, p)
        })
    }

    //region System

    override fun inject(component: ScriptumComponent) {
        component.getNotesBuilder()
            .set(owner = this)
            .build()
            .inject(fragment = this)
    }

    override fun setupView() {
        super.setupView()

        binding?.toolbarInclude?.toolbar?.apply {
            title = getString(R.string.title_notes)
            inflateMenu(R.menu.fragment_notes)
            setOnMenuItemClickListener(this@NotesFragment)

            menu?.findItem(R.id.item_notifications)?.tintIcon(context)
            menu?.findItem(R.id.item_preferences)?.tintIcon(context)
        }

        binding?.recyclerView?.let {
            it.addOnScrollListener(RecyclerOverScrollListener())
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter

            val fabCallback = activity as? RecyclerMainFabListener.Callback
            it.addOnScrollListener(RecyclerMainFabListener(lifecycle, fabCallback))
        }
    }

    override fun setupDialogs() {
        super.setupDialogs()

        optionsDialog.apply {
            onItem { onOptionSelect(position, it) }
            onDismiss { parentOpen?.clear() }
        }

        dateDialog.apply {
            onPositiveClick {
                parentOpen?.skipClear = true
                viewModel.getOccupiedDateList().collect(owner = this) {
                    showTimeDialog(calendar, it, position)
                }
            }
            onNeutralClick {
                viewModel.deleteNoteNotification(position).collect(owner = this) {
                    delegators.broadcast.sendCancelAlarm(it)
                    delegators.broadcast.sendNotifyInfoBind()
                }
            }
            onDismiss { parentOpen?.clear() }
        }

        timeDialog.apply {
            onPositiveClick {
                viewModel.setNoteNotification(calendar, position).collect(owner = this) {
                    val (item, calendar) = it
                    delegators.broadcast.sendSetAlarm(item, calendar)
                    delegators.broadcast.sendNotifyInfoBind()
                }
            }
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
        viewModel.isListHide.observe(this) { observeListHide(it) }
        viewModel.itemList.observe(this) { adapter.notifyList(it) }
    }

    override fun registerReceivers() {
        super.registerReceivers()
        context?.registerReceiver(unbindNoteReceiver, IntentFilter(ReceiverData.Filter.NOTES))
    }

    override fun unregisterReceivers() {
        super.unregisterReceivers()
        context?.unregisterReceiver(unbindNoteReceiver)
    }

    override fun onResume() {
        super.onResume()

        /**
         * Lifecycle observer not working inside viewModel when changing pages. Check out custom
         * call of this function inside parent activity (during fragment transaction).
         */
        viewModel.updateData()
    }

    private fun observeListHide(isListHide: Boolean) {
        val titleId = if (isListHide) {
            R.string.info_notes_hide_title
        } else {
            R.string.info_notes_empty_title
        }
        binding?.infoInclude?.titleText?.setText(titleId)

        val subtitleId = if (isListHide) {
            R.string.info_notes_hide_details
        } else {
            R.string.info_notes_empty_details
        }
        binding?.infoInclude?.detailsText?.setText(subtitleId)
    }

    //endregion

    private fun openNoteScreen(item: NoteItem) {
        val context = context ?: return

        parentOpen?.attempt { startActivity(InstanceFactory.Note[context, item]) }
    }

    private fun showOptionsDialog(item: NoteItem, p: Int) {
        parentOpen?.attempt {
            parentOpen?.tag = OpenState.Tag.DIALOG

            val (title, itemArray) = getOptionsDialogData(item)

            optionsDialog.title = title
            optionsDialog.setArguments(itemArray, p)
                .safeShow(DialogFactory.Main.OPTIONS, owner = this)
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

        itemArray[Options.Notes.NOTIFICATION] = if (item.haveAlarm()) {
            getString(R.string.dialog_menu_notification_update)
        } else {
            getString(R.string.dialog_menu_notification_set)
        }

        itemArray[Options.Notes.BIND] = if (item.isStatus) {
            getString(R.string.dialog_menu_status_unbind)
        } else {
            getString(R.string.dialog_menu_status_bind)
        }

        return title to itemArray
    }

    private fun onOptionSelect(p: Int, which: Int) {
        when (which) {
            Options.Notes.NOTIFICATION -> {
                parentOpen?.skipClear = true
                viewModel.getNoteNotification(p).collect(owner = this) {
                    val (calendar, haveAlarm) = it
                    showDateDialog(calendar, haveAlarm, p)
                }
            }
            Options.Notes.BIND -> viewModel.updateNoteBind(p).collect(owner = this) {
                delegators.broadcast.sendNotifyNotesBind()
            }
            Options.Notes.CONVERT -> viewModel.convertNote(p).collect(owner = this) {
                delegators.broadcast.sendNotifyNotesBind()
            }
            Options.Notes.COPY -> viewModel.getNoteText(p).collect(owner = this) {
                delegators.clipboard.copy(it)
            }
            Options.Notes.DELETE -> viewModel.deleteNote(p).collect(owner = this) {
                delegators.broadcast.sendCancelAlarm(it)
                delegators.broadcast.sendCancelNoteBind(it)
                delegators.broadcast.sendNotifyInfoBind()
            }
        }
    }

    private fun showDateDialog(calendar: Calendar, resetVisible: Boolean, p: Int) {
        parentOpen?.attempt(OpenState.Tag.DIALOG) {
            dateDialog.setArguments(calendar, resetVisible, p)
                .safeShow(DialogFactory.Main.DATE, owner = this)
        }
    }

    private fun showTimeDialog(calendar: Calendar, dateList: List<String>, p: Int) {
        parentOpen?.attempt(OpenState.Tag.DIALOG) {
            timeDialog.setArguments(calendar, dateList, p)
                .safeShow(DialogFactory.Main.TIME, owner = this)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        val context = context ?: return false

        parentOpen?.attempt {
            val intent = when (item?.itemId) {
                R.id.item_notifications -> InstanceFactory.Notifications[context]
                R.id.item_preferences -> InstanceFactory.Preference[context, PreferenceScreen.MENU]
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