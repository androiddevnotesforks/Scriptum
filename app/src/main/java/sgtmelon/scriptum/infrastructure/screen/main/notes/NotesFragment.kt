package sgtmelon.scriptum.infrastructure.screen.main.notes

import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar
import javax.inject.Inject
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.annotation.Options
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.animateAlpha
import sgtmelon.scriptum.databinding.FragmentNotesBinding
import sgtmelon.scriptum.infrastructure.adapter.NoteAdapter
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NoteClickListener
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData
import sgtmelon.scriptum.infrastructure.model.key.PreferenceScreen
import sgtmelon.scriptum.infrastructure.model.state.OpenState
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.main.callback.MainFabCallback
import sgtmelon.scriptum.infrastructure.screen.main.callback.ScrollTopCallback
import sgtmelon.scriptum.infrastructure.screen.parent.ParentFragment
import sgtmelon.scriptum.infrastructure.utils.DelayJobDelegator
import sgtmelon.scriptum.infrastructure.utils.hideKeyboard
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
 * Screen to display the list of main notes.
 */
class NotesFragment : ParentFragment<FragmentNotesBinding>(),
    INotesFragment,
    ScrollTopCallback {

    override val layoutId: Int = R.layout.fragment_notes

    //region Variables

    private val fabCallback: MainFabCallback? get() = activity as? MainFabCallback

    @Inject lateinit var viewModel: NotesViewModel

    private val unbindNoteReceiver by lazy { UnbindNoteReceiver[viewModel] }

    private val dialogs by lazy { DialogFactory.Main(context, fm) }
    private val optionsDialog by lazy { dialogs.getOptions() }
    private val dateDialog by lazy { dialogs.getDate() }
    private val timeDialog by lazy { dialogs.getTime() }

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
    private var parentContainer: ViewGroup? = null
    private var emptyInfoView: View? = null
    private var progressBar: View? = null
    private var recyclerView: RecyclerView? = null

    /** Delay for showing add-note-FAB after long standstill. */
    private val fabDelayJob = DelayJobDelegator(lifecycle)

    //endregion

    //region System

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onSetup()
    }

    override fun inject(component: ScriptumComponent) {
        component.getNotesBuilder()
            .set(fragment = this)
            .build()
            .inject(fragment = this)
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
        viewModel.onUpdateData()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    //endregion

    override fun setupToolbar() {
        view?.findViewById<Toolbar>(R.id.toolbar)?.apply {
            title = getString(R.string.title_notes)
            inflateMenu(R.menu.fragment_notes)

            setOnMenuItemClickListener {
                parentOpen?.attempt {
                    startActivity(
                        when (it.itemId) {
                            R.id.item_notifications -> InstanceFactory.Notifications[context]
                            else -> InstanceFactory.Preference[context, PreferenceScreen.PREFERENCE]
                        }
                    )
                }

                return@setOnMenuItemClickListener true
            }

            activity?.let {
                menu?.findItem(R.id.item_notifications)?.tintIcon(it)
                menu?.findItem(R.id.item_preferences)?.tintIcon(it)
            }
        }
    }

    override fun setupRecycler() {
        parentContainer = view?.findViewById(R.id.notes_parent_container)
        emptyInfoView = view?.findViewById(R.id.info_include)
        progressBar = view?.findViewById(R.id.notes_progress)

        recyclerView = view?.findViewById(R.id.notes_recycler)
        recyclerView?.let {
            it.setDefaultAnimator { onBindingList() }

            it.addOnScrollListener(RecyclerOverScrollListener())
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter

            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    /** Visible only if scroll to top. */
                    val isTopScroll = dy <= 0

                    fabCallback?.changeFabVisibility(isTopScroll, withGap = true)
                    fabDelayJob.run(FAB_STANDSTILL_TIME) {
                        fabCallback?.changeFabVisibility()
                    }
                }
            })
        }
    }

    override fun setupDialog() {
        optionsDialog.apply {
            onItem {
                if (it == Options.Notes.NOTIFICATION) {
                    parentOpen?.skipClear = true
                }

                viewModel.onResultOptionsDialog(optionsDialog.position, it)
            }
            onDismiss { parentOpen?.clear() }
        }

        dateDialog.apply {
            onPositiveClick {
                parentOpen?.skipClear = true
                viewModel.onResultDateDialog(dateDialog.calendar, dateDialog.position)
            }
            onNeutralClick { viewModel.onResultDateDialogClear(dateDialog.position) }
            onDismiss { parentOpen?.clear() }
        }

        timeDialog.apply {
            onPositiveClick {
                viewModel.onResultTimeDialog(timeDialog.calendar, timeDialog.position)
            }
            onDismiss { parentOpen?.clear() }
        }
    }

    override fun setupBinding(isListHide: Boolean) {
        binding?.isListHide = isListHide
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
            parentOpen?.tag = OpenState.Tag.DIALOG

            optionsDialog.title = title
            optionsDialog.setArguments(itemArray, p)
                .safeShow(DialogFactory.Main.OPTIONS, owner = this)
        }
    }

    override fun showDateDialog(calendar: Calendar, resetVisible: Boolean, p: Int) {
        parentOpen?.attempt(OpenState.Tag.DIALOG) {
            dateDialog.setArguments(calendar, resetVisible, p)
                .safeShow(DialogFactory.Main.DATE, owner = this)
        }
    }

    override fun showTimeDialog(calendar: Calendar, dateList: List<String>, p: Int) {
        parentOpen?.attempt(OpenState.Tag.DIALOG) {
            activity?.hideKeyboard()
            timeDialog.setArguments(calendar, dateList, p)
                .safeShow(DialogFactory.Main.TIME, owner = this)
        }
    }


    override fun notifyList(list: List<NoteItem>) = adapter.notifyList(list)


    override fun getStringArray(arrayId: Int): Array<String> = resources.getStringArray(arrayId)

    //region Broadcast functions

    override fun sendSetAlarmBroadcast(id: Long, calendar: Calendar, showToast: Boolean) {
        delegators.broadcast.sendSetAlarm(id, calendar, showToast)
    }

    override fun sendCancelAlarmBroadcast(id: Long) = delegators.broadcast.sendCancelAlarm(id)

    override fun sendNotifyNotesBroadcast() = delegators.broadcast.sendNotifyNotesBind()

    override fun sendCancelNoteBroadcast(id: Long) = delegators.broadcast.sendCancelNoteBind(id)

    override fun sendNotifyInfoBroadcast(count: Int?) {
        delegators.broadcast.sendNotifyInfoBind(count)
    }

    //endregion

    override fun copyClipboard(text: String) = delegators.clipboard.copy(text)


    companion object {
        const val FAB_STANDSTILL_TIME = 2000L
    }
}