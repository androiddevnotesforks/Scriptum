package sgtmelon.scriptum.infrastructure.screen.notifications

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import javax.inject.Inject
import sgtmelon.extensions.collect
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.extension.setDefaultAnimator
import sgtmelon.scriptum.databinding.ActivityNotificationBinding
import sgtmelon.scriptum.infrastructure.adapter.NotificationAdapter
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NotificationClickListener
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.model.state.ShowListState
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
 * Screen with list of feature notifications.
 */
class NotificationActivity : ThemeActivity<ActivityNotificationBinding>(),
    SnackbarDelegator.Callback {

    override val layoutId: Int = R.layout.activity_notification

    override val navigation = WindowUiKeys.Navigation.RotationCatch
    override val navDivider = WindowUiKeys.NavDivider.RotationCatch

    @Inject lateinit var viewModel: NotificationViewModel

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
            .set(activity = this)
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
            it.setDefaultAnimator {
                viewModel.notifyListState()

                /** Clear [open] after click on item cancel OR [onSnackbarAction]. */
                open.clear()
            }

            it.addOnScrollListener(RecyclerOverScrollListener(showFooter = false))
            it.setHasFixedSize(true)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
    }

    override fun setupObservers() {
        viewModel.showList.observe(this) { observeShowList(it) }
        viewModel.itemList.observe(this) { observeItemList(it) }
        viewModel.showSnackbar.observe(this) { observeShowSnackbar(it) }
    }

    override fun onResume() {
        super.onResume()

        /** Need clear [open], because may be case with [openNoteScreen]. */
        open.clear()
    }

    // TODO check work with rotation
    // TODO На сколько я понимаю, после поворота экрана будет восстановлен snackbar и поэтому не нужно чтобы отработал dismissResult, который отчищает список cancelList в vm
    override fun onStop() {
        super.onStop()

        /**
         * Need dismiss without listener for control rotation case. We don't want to lost
         * snackbar stack inside [viewModel] during rotation (need restore [snackbar]
         * after recreation).
         */
        snackbar.dismiss(skipDismissResult = true)
    }

    override fun onDestroy() {
        super.onDestroy()

        /** Inside [onStop] we clear listener -> here needed call dismiss action manually. */
        onSnackbarDismiss()
    }

    // TODO add animation
    private fun observeShowList(it: ShowListState) {
        when (it) {
            is ShowListState.Loading -> {
                binding?.progressBar?.visibility = View.VISIBLE
                binding?.recyclerView?.visibility = View.GONE
                binding?.infoInclude?.parentContainer?.visibility = View.GONE
            }
            is ShowListState.List -> {
                binding?.progressBar?.visibility = View.GONE
                binding?.recyclerView?.visibility = View.VISIBLE
                binding?.infoInclude?.parentContainer?.visibility = View.GONE
            }
            is ShowListState.Empty -> {
                binding?.progressBar?.visibility = View.GONE
                binding?.recyclerView?.visibility = View.GONE
                binding?.infoInclude?.parentContainer?.visibility = View.VISIBLE
            }
        }
    }

    private fun observeItemList(it: List<NotificationItem>) {
        when (val state = viewModel.updateList) {
            is UpdateListState.Set -> adapter.setList(it)
            is UpdateListState.Notify -> adapter.notifyList(it)
            is UpdateListState.Removed -> adapter.setList(it).notifyItemRemoved(state.p)
            is UpdateListState.InsertedScroll -> {
                adapter.setList(it).notifyItemInserted(state.p)
                RecyclerInsertScroll(binding?.recyclerView, layoutManager).scroll(it, state.p)
            }
        }
    }

    private fun observeShowSnackbar(it: Boolean) {
        if (!it) return

        val parentContainer = binding?.recyclerContainer ?: return
        snackbar.show(parentContainer, withInsets = true)
    }

    // TODO

    //region Clean up
    //
    //    override fun showProgress() {
    //        binding?.progressBar?.visibility = View.VISIBLE
    //    }
    //
    //    override fun hideEmptyInfo() {
    //        binding?.infoInclude?.parentContainer?.visibility = View.GONE
    //    }
    //
    //
    //    TODO check animation
    //    override fun onBindingList() {
    //        binding?.progressBar?.visibility = View.GONE
    //
    //        if (adapter.itemCount == 0) {
    //            /**
    //             * Prevent useless calls from [RecyclerView.setDefaultAnimator].
    //             */
    //            if (binding?.infoInclude?.parentContainer?.visibility == View.VISIBLE
    //                && binding?.recyclerView?.visibility == View.INVISIBLE
    //            ) return
    //
    //            binding?.infoInclude?.parentContainer?.visibility = View.VISIBLE
    //            binding?.recyclerView?.visibility = View.INVISIBLE
    //
    //            binding?.infoInclude?.parentContainer?.alpha = 0f
    //            binding?.infoInclude?.parentContainer?.animateAlpha(isVisible = true)
    //        } else {
    //            /**
    //             * Prevent useless calls from [RecyclerView.setDefaultAnimator].
    //             */
    //            if (binding?.infoInclude?.parentContainer?.visibility == View.GONE
    //                && binding?.recyclerView?.visibility == View.VISIBLE
    //            ) return
    //
    //            binding?.recyclerView?.visibility = View.VISIBLE
    //
    //            binding?.infoInclude?.parentContainer?.animateAlpha(isVisible = false) {
    //                binding?.infoInclude?.parentContainer?.visibility = View.GONE
    //            }
    //        }
    //    }

    //    override fun showSnackbar() {
    //        binding?.recyclerContainer?.let { snackbar.show(it, withInsets = true) }
    //    }
    //
    //    override fun setList(list: List<NotificationItem>) {
    //        adapter.setList(list)
    //    }
    //
    //    override fun notifyList(list: List<NotificationItem>) = adapter.notifyList(list)
    //
    //    override fun notifyItemRemoved(list: List<NotificationItem>, p: Int) {
    //        adapter.setList(list).notifyItemRemoved(p)
    //    }
    //
    //    override fun notifyItemInsertedScroll(list: List<NotificationItem>, p: Int) {
    //        adapter.setList(list).notifyItemInserted(p)
    //        RecyclerInsertScroll(binding?.recyclerView, layoutManager).scroll(list, p)
    //    }
    //
    //
    //    override fun sendSetAlarmBroadcast(id: Long, calendar: Calendar, showToast: Boolean) {
    //        delegators.broadcast.sendSetAlarm(id, calendar, showToast)
    //    }
    //
    //    override fun sendCancelAlarmBroadcast(id: Long) = delegators.broadcast.sendCancelAlarm(id)
    //
    //    override fun sendNotifyInfoBroadcast(count: Int?) = delegators.broadcast.sendNotifyInfoBind(count)

    //endregion

    private fun openNoteScreen(item: NotificationItem) = open.attempt {
        startActivity(InstanceFactory.Note[this, item])
    }

    private fun removeNotification(p: Int) {
        viewModel.removeNotification(p).collect(owner = this) {
            val (item, size) = it

            delegators.broadcast.sendCancelAlarm(item)
            delegators.broadcast.sendNotifyInfoBind(size)
        }
    }

    override fun onSnackbarAction() = open.attempt { viewModel.onSnackbarAction() }

    override fun onSnackbarDismiss() = viewModel.onSnackbarDismiss()

}