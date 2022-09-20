package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar
import javax.inject.Inject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.domain.model.state.OpenState
import sgtmelon.scriptum.cleanup.extension.InsetsDir
import sgtmelon.scriptum.cleanup.extension.animateAlpha
import sgtmelon.scriptum.cleanup.extension.getTintDrawable
import sgtmelon.scriptum.cleanup.extension.initLazy
import sgtmelon.scriptum.cleanup.extension.setDefaultAnimator
import sgtmelon.scriptum.cleanup.extension.setMarginInsets
import sgtmelon.scriptum.cleanup.extension.setPaddingInsets
import sgtmelon.scriptum.cleanup.presentation.adapter.NotificationAdapter
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.notification.INotificationActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.INotificationViewModel
import sgtmelon.scriptum.databinding.ActivityNotificationBinding
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.BroadcastDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.SnackbarDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.scriptum.infrastructure.widgets.listeners.RecyclerOverScrollListener

/**
 * Screen with list of notifications.
 */
class NotificationActivity : ThemeActivity<ActivityNotificationBinding>(),
    INotificationActivity,
    SnackbarDelegator.Callback {

    override val layoutId: Int = R.layout.activity_notification

    override val navigation = WindowUiKeys.Navigation.RotationCatch
    override val navDivider = WindowUiKeys.NavDivider.RotationCatch

    //region Variables

    @Inject internal lateinit var viewModel: INotificationViewModel

    private val broadcast by lazy { BroadcastDelegator(context = this) }

    private val openState = OpenState()

    private val adapter: NotificationAdapter by lazy {
        NotificationAdapter(object: ItemListener.Click {
            override fun onItemClick(view: View, p: Int) = openState.tryInvoke {
                when (view.id) {
                    R.id.notification_click_container -> viewModel.onClickNote(p)
                    R.id.notification_cancel_button -> viewModel.onClickCancel(p)
                }
            }
        })
    }
    private val layoutManager by lazy { LinearLayoutManager(this) }

    private val snackbar = SnackbarDelegator(
        R.string.snackbar_message_notification, R.string.snackbar_action_cancel, callback = this
    )

    // TODO remove and use binding
    private val emptyInfoView by lazy { findViewById<View?>(R.id.info_include) }

    //endregion

    //region System

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        broadcast.initLazy()

        openState.get(savedInstanceState)

        /**
         * Inside [savedInstanceState] saved snackbar data.
         */
        viewModel.onSetup(savedInstanceState)
    }

    override fun inject(component: ScriptumComponent) {
        component.getNotificationBuilder()
            .set(activity = this)
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    override fun onResume() {
        super.onResume()

        /**
         * Clear [openState] after click on item container.
         */
        openState.clear()
        viewModel.onUpdateData()
    }

    override fun onStop() {
        super.onStop()
        // TODO На сколько я понимаю, после поворота экрана будет восстановлен snackbar и поэтому не нужно чтобы отработал dismissResult
        snackbar.dismiss(skipDismissResult = true)
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.onSnackbarDismiss()
        viewModel.onDestroy()
    }

    /**
     * Save snackbar data on rotation and screen turn off. Func [onSaveInstanceState] will be
     * called in both cases.
     *
     * - But on rotation case [outState] will be restored inside [onCreate].
     * - On turn off screen case [outState] will be restored only if activity will be
     *   recreated.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        openState.save(outState)
        viewModel.onSaveData(outState)
    }

    //endregion

    override fun setupToolbar() {
        findViewById<Toolbar>(R.id.toolbar_container).apply {
            title = getString(R.string.title_notification)
            navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
            setNavigationOnClickListener { finish() }
        }
    }

    override fun setupRecycler() {
        binding?.recyclerView?.let {
            it.setDefaultAnimator {
                onBindingList()

                /**
                 * Clear [openState] after click on item cancel OR [onSnackbarAction].
                 */
                openState.clear()
            }

            it.addOnScrollListener(RecyclerOverScrollListener(showFooter = false))
            it.setHasFixedSize(true)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
    }

    override fun setupInsets() {
        binding?.parentContainer?.setMarginInsets(InsetsDir.LEFT, InsetsDir.TOP, InsetsDir.RIGHT)
        binding?.recyclerView?.setPaddingInsets(InsetsDir.BOTTOM)
    }

    /**
     * For first time [recyclerView] visibility flag set inside xml file.
     */
    override fun prepareForLoad() {
        emptyInfoView?.visibility = View.GONE
        binding?.progressBar?.visibility = View.GONE
    }

    override fun showProgress() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    override fun hideEmptyInfo() {
        emptyInfoView?.visibility = View.GONE
    }


    override fun onBindingList() {
        binding?.progressBar?.visibility = View.GONE

        if (adapter.itemCount == 0) {
            /**
             * Prevent useless calls from [RecyclerView.setDefaultAnimator].
             */
            if (emptyInfoView?.visibility == View.VISIBLE
                && binding?.recyclerView?.visibility == View.INVISIBLE
            ) return

            emptyInfoView?.visibility = View.VISIBLE
            binding?.recyclerView?.visibility = View.INVISIBLE

            emptyInfoView?.alpha = 0f
            emptyInfoView?.animateAlpha(isVisible = true)
        } else {
            /**
             * Prevent useless calls from [RecyclerView.setDefaultAnimator].
             */
            if (emptyInfoView?.visibility == View.GONE
                && binding?.recyclerView?.visibility == View.VISIBLE
            ) return

            binding?.recyclerView?.visibility = View.VISIBLE

            emptyInfoView?.animateAlpha(isVisible = false) {
                emptyInfoView?.visibility = View.GONE
            }
        }
    }

    override fun openNoteScreen(item: NotificationItem) = startActivity(NoteActivity[this, item])

    override fun showSnackbar() {
        binding?.recyclerContainer?.let { snackbar.show(it, withInsets = true) }
    }

    override fun onSnackbarAction() = openState.tryInvoke { viewModel.onSnackbarAction() }

    override fun onSnackbarDismiss() = viewModel.onSnackbarDismiss()


    override fun setList(list: List<NotificationItem>) {
        adapter.setList(list)
    }

    override fun notifyList(list: List<NotificationItem>) = adapter.notifyList(list)

    override fun notifyItemRemoved(list: List<NotificationItem>, p: Int) {
        adapter.setList(list).notifyItemRemoved(p)
    }

    override fun notifyItemInsertedScroll(list: List<NotificationItem>, p: Int) {
        adapter.setList(list).notifyItemInserted(p)

        val firstVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
        val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()

        /**
         *  FirstVisiblePosition can be equal [p] if:
         *  - Click on first item remove;
         *  - Click on snackbar undo.
         *
         *  Then [p] = 0 and firstVisiblePosition = 0.
         */
        if (p <= firstVisiblePosition || p > lastVisiblePosition) {
            binding?.recyclerView?.smoothScrollToPosition(p)
        }
    }


    //region Broadcast functions

    override fun sendSetAlarmBroadcast(id: Long, calendar: Calendar, showToast: Boolean) {
        broadcast.sendSetAlarm(id, calendar, showToast)
    }

    override fun sendCancelAlarmBroadcast(id: Long) = broadcast.sendCancelAlarm(id)

    /**
     * Not used here.
     */
    override fun sendNotifyNotesBroadcast() = Unit

    /**
     * Not used here.
     */
    override fun sendCancelNoteBroadcast(id: Long) = Unit

    override fun sendNotifyInfoBroadcast(count: Int?) = broadcast.sendNotifyInfoBind(count)

    //endregion

    companion object {
        operator fun get(context: Context) = Intent(context, NotificationActivity::class.java)
    }
}