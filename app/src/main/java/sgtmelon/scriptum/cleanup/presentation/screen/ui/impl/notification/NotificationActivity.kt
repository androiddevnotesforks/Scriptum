package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification

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
import sgtmelon.scriptum.cleanup.extension.animateAlpha
import sgtmelon.scriptum.cleanup.extension.setDefaultAnimator
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.notification.INotificationActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.INotificationViewModel
import sgtmelon.scriptum.databinding.ActivityNotificationBinding
import sgtmelon.scriptum.infrastructure.adapter.NotificationAdapter
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NotificationClickListener
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.SnackbarDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.scriptum.infrastructure.utils.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.getTintDrawable
import sgtmelon.scriptum.infrastructure.utils.setMarginInsets
import sgtmelon.scriptum.infrastructure.utils.setPaddingInsets
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerInsertScroll
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener

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

    @Inject lateinit var viewModel: INotificationViewModel

    private val adapter: NotificationAdapter by lazy {
        NotificationAdapter(object : NotificationClickListener {
            override fun onNotificationClick(item: NotificationItem) = openNoteScreen(item)
            override fun onNotificationCancel(p: Int) = viewModel.onClickCancel(p)
        })
    }
    private val layoutManager by lazy { LinearLayoutManager(this) }

    private val snackbar = SnackbarDelegator(
        R.string.snackbar_message_notification, R.string.snackbar_action_cancel, callback = this
    )

    //endregion

    //region System

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
         * Clear [open] after click on item container.
         */
        open.clear()
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
        viewModel.onSaveData(outState)
    }

    //endregion

    override fun setupToolbar() {
        findViewById<Toolbar>(R.id.toolbar).apply {
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
                 * Clear [open] after click on item cancel OR [onSnackbarAction].
                 */
                open.clear()
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
        binding?.infoInclude?.parentContainer?.visibility = View.GONE
        binding?.progressBar?.visibility = View.GONE
    }

    override fun showProgress() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    override fun hideEmptyInfo() {
        binding?.infoInclude?.parentContainer?.visibility = View.GONE
    }


    override fun onBindingList() {
        binding?.progressBar?.visibility = View.GONE

        if (adapter.itemCount == 0) {
            /**
             * Prevent useless calls from [RecyclerView.setDefaultAnimator].
             */
            if (binding?.infoInclude?.parentContainer?.visibility == View.VISIBLE
                && binding?.recyclerView?.visibility == View.INVISIBLE
            ) return

            binding?.infoInclude?.parentContainer?.visibility = View.VISIBLE
            binding?.recyclerView?.visibility = View.INVISIBLE

            binding?.infoInclude?.parentContainer?.alpha = 0f
            binding?.infoInclude?.parentContainer?.animateAlpha(isVisible = true)
        } else {
            /**
             * Prevent useless calls from [RecyclerView.setDefaultAnimator].
             */
            if (binding?.infoInclude?.parentContainer?.visibility == View.GONE
                && binding?.recyclerView?.visibility == View.VISIBLE
            ) return

            binding?.recyclerView?.visibility = View.VISIBLE

            binding?.infoInclude?.parentContainer?.animateAlpha(isVisible = false) {
                binding?.infoInclude?.parentContainer?.visibility = View.GONE
            }
        }
    }

    private fun openNoteScreen(item: NotificationItem) {
        startActivity(InstanceFactory.Note[this, item])
    }

    override fun showSnackbar() {
        binding?.recyclerContainer?.let { snackbar.show(it, withInsets = true) }
    }

    override fun onSnackbarAction() = open.attempt { viewModel.onSnackbarAction() }

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
        RecyclerInsertScroll(binding?.recyclerView, layoutManager).scroll(list.indices, p)
    }


    //region Broadcast functions

    override fun sendSetAlarmBroadcast(id: Long, calendar: Calendar, showToast: Boolean) {
        delegators.broadcast.sendSetAlarm(id, calendar, showToast)
    }

    override fun sendCancelAlarmBroadcast(id: Long) = delegators.broadcast.sendCancelAlarm(id)

    /** Not used here. */
    override fun sendNotifyNotesBroadcast() = Unit

    /** Not used here. */
    override fun sendCancelNoteBroadcast(id: Long) = Unit

    override fun sendNotifyInfoBroadcast(count: Int?) {
        delegators.broadcast.sendNotifyInfoBind(count)
    }

    //endregion

}