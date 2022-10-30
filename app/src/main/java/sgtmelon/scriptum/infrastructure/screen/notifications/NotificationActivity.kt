package sgtmelon.scriptum.infrastructure.screen.notifications

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import javax.inject.Inject
import sgtmelon.extensions.collect
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.databinding.ActivityNotificationBinding
import sgtmelon.scriptum.infrastructure.adapter.NotificationAdapter
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NotificationClickListener
import sgtmelon.scriptum.infrastructure.animation.ShowListAnimation
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.SnackbarDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.scriptum.infrastructure.utils.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.getTintDrawable
import sgtmelon.scriptum.infrastructure.utils.setDefaultAnimator
import sgtmelon.scriptum.infrastructure.utils.setMarginInsets
import sgtmelon.scriptum.infrastructure.utils.setPaddingInsets
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerInsertScroll
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerOverScrollListener

/**
 * Screen with list of feature notifications.
 */
class NotificationActivity : ThemeActivity<ActivityNotificationBinding>(),
    SnackbarDelegator.Callback {

    override val layoutId: Int = R.layout.activity_notification

    override val navigation = WindowUiKeys.Navigation.RotationCatch
    override val navDivider = WindowUiKeys.NavDivider.RotationCatch

    @Inject lateinit var viewModel: NotificationViewModel

    private val animation = ShowListAnimation()

    private val adapter: NotificationAdapter by lazy {
        NotificationAdapter(object : NotificationClickListener {
            override fun onNotificationClick(item: NotificationItem) = openNoteScreen(item)
            override fun onNotificationCancel(p: Int) = removeNotification(p)
        })
    }
    private val layoutManager by lazy { LinearLayoutManager(this) }

    private val snackbar = SnackbarDelegator(
        R.string.snackbar_message_notification, R.string.snackbar_action_cancel, callback = this
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
    }

    override fun inject(component: ScriptumComponent) {
        component.getNotificationBuilder()
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    override fun setupInsets() {
        binding?.parentContainer?.setMarginInsets(InsetsDir.LEFT, InsetsDir.TOP, InsetsDir.RIGHT)
        binding?.recyclerView?.setPaddingInsets(InsetsDir.BOTTOM)
    }

    override fun setupView() {
        binding?.toolbarInclude?.toolbar?.apply {
            title = getString(R.string.title_notification)
            navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
            setNavigationOnClickListener { finish() }
        }

        binding?.recyclerView?.let {
            /** Clear [open] after click on item cancel. */
            it.setDefaultAnimator { open.clear() }
            it.addOnScrollListener(RecyclerOverScrollListener(showFooter = false))
            it.setHasFixedSize(true)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
    }

    override fun setupObservers() {
        viewModel.showList.observe(this) {
            val binding = binding ?: return@observe

            animation.startListFade(
                it, binding.parentContainer, binding.progressBar,
                binding.recyclerView, binding.infoInclude.parentContainer
            )
        }
        viewModel.itemList.observe(this) { observeItemList(it) }
        viewModel.showSnackbar.observe(this) { if (it) showSnackbar() }
    }

    override fun onResume() {
        super.onResume()

        /** Need clear [open], because may be case with [openNoteScreen]. */
        open.clear()

        /** Restore our snack bar if it must be shown. */
        if (viewModel.showSnackbar.value == true && !snackbar.isDisplayed) {
            showSnackbar()
        }
    }

    override fun onStop() {
        super.onStop()

        /**
         * Need dismiss without listener for control leave screen case. We don't want to lost
         * snackbar stack inside [viewModel] during rotation/app close/ect. So, need restore
         * [snackbar] after screen reopen - [onResume].
         */
        snackbar.dismiss(skipDismissResult = true)
    }

    /**
     * Use here [UpdateListState.NotifyHard] case, because it will prevent lags during
     * undo (insert) first item. When empty info hides and list appears. Insert animation
     * and list fade in animation concurrent with each other and it's looks laggy.
     */
    private fun observeItemList(it: List<NotificationItem>) {
        when (val state = viewModel.updateList) {
            is UpdateListState.Set -> adapter.setList(it)
            is UpdateListState.Notify -> adapter.notifyList(it)
            is UpdateListState.NotifyHard -> adapter.setList(it).notifyDataSetChanged()
            is UpdateListState.Remove -> adapter.setList(it).notifyItemRemoved(state.p)
            is UpdateListState.Insert -> {
                adapter.setList(it).notifyItemInserted(state.p)
                RecyclerInsertScroll(binding?.recyclerView, layoutManager).scroll(it, state.p)
            }
        }
    }

    private fun showSnackbar() {
        val parentContainer = binding?.recyclerContainer ?: return
        snackbar.show(parentContainer, withInsets = true)
    }

    private fun openNoteScreen(item: NotificationItem) = open.attempt {
        startActivity(InstanceFactory.Note[this, item])
    }

    private fun removeNotification(p: Int) {
        viewModel.removeNotification(p).collect(owner = this) {
            val (item, size) = it
            delegators?.broadcast?.sendCancelAlarm(item)
            delegators?.broadcast?.sendNotifyInfoBind(size)
        }
    }

    override fun onSnackbarAction() {
        viewModel.undoRemove().collect(owner = this) { collectUndo(it) }
    }

    private fun collectUndo(it: UndoState) {
        delegators?.broadcast?.run {
            when (it) {
                is UndoState.NotifyInfoCount -> sendNotifyInfoBind(it.count)
                is UndoState.NotifyAlarm -> sendSetAlarm(it.id, it.calendar, showToast = false)
            }
        }
    }

    override fun onSnackbarDismiss() = viewModel.clearUndoStack()

}