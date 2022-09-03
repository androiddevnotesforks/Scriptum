package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar
import javax.inject.Inject
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.annotation.Options
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.key.PreferenceScreen
import sgtmelon.scriptum.cleanup.domain.model.state.OpenState
import sgtmelon.scriptum.cleanup.extension.animateAlpha
import sgtmelon.scriptum.cleanup.extension.hideKeyboard
import sgtmelon.scriptum.cleanup.extension.inflateBinding
import sgtmelon.scriptum.cleanup.extension.initLazy
import sgtmelon.scriptum.cleanup.extension.setDefaultAnimator
import sgtmelon.scriptum.cleanup.extension.tintIcon
import sgtmelon.scriptum.cleanup.presentation.adapter.NoteAdapter
import sgtmelon.scriptum.cleanup.presentation.control.broadcast.BroadcastControl
import sgtmelon.scriptum.cleanup.presentation.control.system.ClipboardControl
import sgtmelon.scriptum.cleanup.presentation.factory.DialogFactory
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener
import sgtmelon.scriptum.cleanup.presentation.receiver.screen.MainScreenReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ParentFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.main.IMainActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.main.INotesFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.PreferenceActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.INotesViewModel
import sgtmelon.scriptum.databinding.FragmentNotesBinding
import sgtmelon.scriptum.infrastructure.widgets.RecyclerOverScrollListener

/**
 * Fragment which displays list of notes - [NoteItem].
 */
class NotesFragment : ParentFragment(),
    INotesFragment,
    MainScreenReceiver.BindCallback,
    MainScreenReceiver.AlarmCallback {

    //region Variables

    private val callback: IMainActivity? by lazy { context as? IMainActivity }

    private var binding: FragmentNotesBinding? = null

    @Inject internal lateinit var viewModel: INotesViewModel

    private val broadcastControl by lazy { BroadcastControl[context] }
    private val clipboardControl by lazy { ClipboardControl[context, toastControl] }

    private val openState get() = callback?.openState
    private val dialogFactory by lazy { DialogFactory.Main(context, fm) }

    private val optionsDialog by lazy { dialogFactory.getOptionsDialog() }
    private val dateDialog by lazy { dialogFactory.getDateDialog() }
    private val timeDialog by lazy { dialogFactory.getTimeDialog() }

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
    private var parentContainer: ViewGroup? = null
    private var emptyInfoView: View? = null
    private var progressBar: View? = null
    private var recyclerView: RecyclerView? = null

    /**
     * Handler for show addFab after long standstill.
     */
    private val fabHandler = Handler()

    //endregion

    //region System

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflateBinding(R.layout.fragment_notes, container)

        ScriptumApplication.component.getNotesBuilder().set(fragment = this).build()
            .inject(fragment = this)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        broadcastControl.initLazy()
        clipboardControl.initLazy()

        viewModel.onSetup()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUpdateData()
    }

    override fun onPause() {
        super.onPause()
        fabHandler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    //endregion

    //region Receiver functions

    override fun onReceiveUnbindNote(id: Long) = viewModel.onReceiveUnbindNote(id)

    override fun onReceiveUpdateAlarm(id: Long) = viewModel.onReceiveUpdateAlarm(id)

    //endregion

    override fun setupToolbar() {
        view?.findViewById<Toolbar>(R.id.toolbar_container)?.apply {
            title = getString(R.string.title_notes)
            inflateMenu(R.menu.fragment_notes)

            setOnMenuItemClickListener {
                openState?.tryInvoke {
                    startActivity(when (it.itemId) {
                        R.id.item_notification -> NotificationActivity[context]
                        else -> PreferenceActivity[context, PreferenceScreen.PREFERENCE]
                    })
                }

                return@setOnMenuItemClickListener true
            }

            activity?.let {
                menu?.findItem(R.id.item_notification)?.tintIcon(it)
                menu?.findItem(R.id.item_preference)?.tintIcon(it)
            }
        }
    }

    override fun setupRecycler() {
        parentContainer = view?.findViewById(R.id.notes_parent_container)
        emptyInfoView = view?.findViewById(R.id.notes_info_include)
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
                    /**
                     * Visible only if scroll to top.
                     */
                    val isTopScroll = dy <= 0

                    callback?.onFabStateChange(isTopScroll)

                    fabHandler.removeCallbacksAndMessages(null)
                    fabHandler.postDelayed({
                        callback?.onFabStateChange(state = true)
                    }, FAB_STANDSTILL_TIME)
                }
            })
        }
    }

    override fun setupDialog() {
        optionsDialog.apply {
            itemListener = DialogInterface.OnClickListener { _, which ->
                if (which == Options.Notes.NOTIFICATION) openState?.skipClear = true

                viewModel.onResultOptionsDialog(optionsDialog.position, which)
            }
            onDismiss { openState?.clear() }
        }

        dateDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                openState?.skipClear = true
                viewModel.onResultDateDialog(dateDialog.calendar, dateDialog.position)
            }
            neutralListener = DialogInterface.OnClickListener { _, _ ->
                viewModel.onResultDateDialogClear(dateDialog.position)
            }
            onDismiss { openState?.clear() }
        }

        timeDialog.apply {
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                viewModel.onResultTimeDialog(timeDialog.calendar, timeDialog.position)
            }
            onDismiss { openState?.clear() }
        }
    }

    override fun setupBinding(isListHide: Boolean) {
        binding?.isListHide = isListHide
    }


    /**
     * For first time [recyclerView] visibility flag set inside xml file.
     */
    override fun prepareForLoad() {
        emptyInfoView?.visibility = View.GONE
        progressBar?.visibility = View.GONE
    }

    override fun showProgress() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun hideEmptyInfo() {
        emptyInfoView?.visibility = View.GONE
    }


    override fun onBindingList() {
        progressBar?.visibility = View.GONE

        /**
         * Case without animation need for best performance, without freeze. Because changes
         * on other screens may cause [onBindingList].
         */
        if (adapter.itemCount == 0) {
            /**
             * Prevent useless calls from [RecyclerView.setDefaultAnimator].
             */
            if (emptyInfoView?.visibility == View.VISIBLE
                    && recyclerView?.visibility == View.INVISIBLE) return

            emptyInfoView?.visibility = View.VISIBLE
            recyclerView?.visibility = View.INVISIBLE

            emptyInfoView?.alpha = 0f
            emptyInfoView?.animateAlpha(isVisible = true)
        } else {
            /**
             * Prevent useless calls from [RecyclerView.setDefaultAnimator].
             */
            if (emptyInfoView?.visibility == View.GONE
                    && recyclerView?.visibility == View.VISIBLE) return

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


    override fun showOptionsDialog(title: String, itemArray: Array<String>, p: Int) {
        openState?.tryInvoke {
            openState?.tag = OpenState.Tag.DIALOG

            optionsDialog.title = title
            optionsDialog.setArguments(itemArray, p)
                .safeShow(fm, DialogFactory.Main.OPTIONS, owner = this)
        }
    }

    override fun showDateDialog(calendar: Calendar, resetVisible: Boolean, p: Int) {
        openState?.tryInvoke(OpenState.Tag.DIALOG) {
            dateDialog.setArguments(calendar, resetVisible, p)
                .safeShow(fm, DialogFactory.Main.DATE, owner = this)
        }
    }

    override fun showTimeDialog(calendar: Calendar, dateList: List<String>, p: Int) {
        openState?.tryInvoke(OpenState.Tag.DIALOG) {
            activity?.hideKeyboard()
            timeDialog.setArguments(calendar, dateList, p)
                .safeShow(fm, DialogFactory.Main.TIME, owner = this)
        }
    }


    override fun notifyList(list: List<NoteItem>) = adapter.notifyList(list)

    override fun notifyItemChanged(list: List<NoteItem>, p: Int) {
        adapter.setList(list).notifyItemChanged(p)
    }

    override fun notifyItemRemoved(list: List<NoteItem>, p: Int) {
        adapter.setList(list).notifyItemRemoved(p)
    }


    override fun getStringArray(arrayId: Int): Array<String> = resources.getStringArray(arrayId)

    //region Broadcast functions

    override fun sendSetAlarmBroadcast(id: Long, calendar: Calendar, showToast: Boolean) {
        broadcastControl.sendSetAlarm(id, calendar, showToast)
    }

    override fun sendCancelAlarmBroadcast(id: Long) = broadcastControl.sendCancelAlarm(id)

    override fun sendNotifyNotesBroadcast() = broadcastControl.sendNotifyNotesBind()

    override fun sendCancelNoteBroadcast(id: Long) = broadcastControl.sendCancelNoteBind(id)

    override fun sendNotifyInfoBroadcast(count: Int?) = broadcastControl.sendNotifyInfoBind(count)

    //endregion

    override fun copyClipboard(text: String) = clipboardControl.copy(text)


    companion object {
        const val FAB_STANDSTILL_TIME = 2000L
    }
}