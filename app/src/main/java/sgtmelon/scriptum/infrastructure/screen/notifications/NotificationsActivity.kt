package sgtmelon.scriptum.infrastructure.screen.notifications

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.extensions.collect
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.databinding.ActivityNotificationsBinding
import sgtmelon.scriptum.infrastructure.adapter.NotificationAdapter
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NotificationClickListener
import sgtmelon.scriptum.infrastructure.animation.ShowListAnimation
import sgtmelon.scriptum.infrastructure.model.key.ReceiverFilter
import sgtmelon.scriptum.infrastructure.receiver.screen.InfoChangeReceiver
import sgtmelon.scriptum.infrastructure.screen.Screens
import sgtmelon.scriptum.infrastructure.screen.notifications.state.UndoState
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListScreen
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.SnackbarDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.scriptum.infrastructure.utils.extensions.getTintDrawable
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.setMarginInsets
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.setPaddingInsets
import sgtmelon.scriptum.infrastructure.utils.extensions.isTrue
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener
import javax.inject.Inject

/**
 * Screen with list of feature notifications.
 */
class NotificationsActivity : ThemeActivity<ActivityNotificationsBinding>(),
    ListScreen<NotificationItem>,
    SnackbarDelegator.Callback {

    override val layoutId: Int = R.layout.activity_notifications

    override val navigation = WindowUiKeys.Navigation.RotationCatch
    override val navDivider = WindowUiKeys.NavDivider.RotationCatch

    @Inject override lateinit var viewModel: NotificationsViewModel

    private val listAnimation = ShowListAnimation()

    private val infoChangeReceiver by lazy { InfoChangeReceiver[viewModel] }
    override val receiverFilter = ReceiverFilter.NOTIFICATION
    override val receiverList get() = listOf(infoChangeReceiver)

    override val adapter: NotificationAdapter by lazy {
        NotificationAdapter(object : NotificationClickListener {
            override fun onNotificationClick(item: NotificationItem) = openNoteScreen(item)
            override fun onNotificationCancel(p: Int) = removeNotification(p)
        })
    }
    override val layoutManager by lazy { LinearLayoutManager(this) }
    override val recyclerView: RecyclerView? get() = binding?.recyclerView

    private val snackbar = SnackbarDelegator(
        lifecycle,
        R.string.snackbar_message_notification,
        R.string.snackbar_action_cancel,
        callback = this
    )

    //region System

    override fun inject(component: ScriptumComponent) {
        component.getNotificationBuilder()
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    override fun setupInsets() {
        super.setupInsets()

        binding?.parentContainer?.setMarginInsets(InsetsDir.LEFT, InsetsDir.TOP, InsetsDir.RIGHT)
        binding?.recyclerView?.setPaddingInsets(InsetsDir.BOTTOM)
    }

    override fun setupView() {
        super.setupView()

        binding?.appBar?.toolbar?.apply {
            title = getString(R.string.title_notification)
            navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
            setNavigationOnClickListener { finish() }
        }

        binding?.recyclerView?.let {
            it.addOnScrollListener(RecyclerOverScrollListener(showFooter = false))
            it.setHasFixedSize(true) /** The height of all items absolutely the same. */
            it.layoutManager = layoutManager
            it.adapter = adapter
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
        viewModel.showSnackbar.observe(this) { if (it) showSnackbar() }
    }

    override fun onResume() {
        super.onResume()

        viewModel.updateData()

        /** Need clear [open], because may be case with [openNoteScreen]. */
        open.clear()

        /** Restore our snack bar if it must be shown. */
        if (viewModel.showSnackbar.value.isTrue() && !snackbar.isDisplayed) {
            showSnackbar()
        }
    }

    //endregion

    private fun showSnackbar() {
        val parentContainer = binding?.recyclerContainer ?: return
        snackbar.show(parentContainer, withInsets = true)
    }

    private fun openNoteScreen(item: NotificationItem) {
        viewModel.getNote(item).collect(owner = this) {
            open.attempt {
                startActivity(Screens.Note.toExist(context = this, it))
            }
        }
    }

    private fun removeNotification(p: Int) = open.attempt(withSwitch = false) {
        viewModel.removeItem(p).collect(owner = this) {
            val (item, size) = it
            system?.broadcast?.sendCancelAlarm(item)
            system?.broadcast?.sendNotifyInfoBind(size)
        }
    }

    override fun onSnackbarAction() {
        viewModel.undoRemove().collect(owner = this) { collectUndo(it) }
    }

    private fun collectUndo(it: UndoState) {
        system?.broadcast?.run {
            when (it) {
                is UndoState.NotifyInfoCount -> sendNotifyInfoBind(it.count)
                is UndoState.NotifyAlarm -> sendSetAlarm(it.id, it.calendar, showToast = false)
            }
        }
    }

    override fun onSnackbarDismiss() = viewModel.clearUndoStack()

}