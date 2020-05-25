package sgtmelon.scriptum.presentation.screen.ui.impl.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ActivityNotificationBinding
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.domain.model.state.OpenState
import sgtmelon.scriptum.extension.createVisibleAnim
import sgtmelon.scriptum.extension.getTintDrawable
import sgtmelon.scriptum.extension.inflateBinding
import sgtmelon.scriptum.extension.initLazy
import sgtmelon.scriptum.presentation.adapter.NotificationAdapter
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.listener.ItemListener
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.INotificationActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.AppActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.notification.INotificationViewModel
import java.util.*
import javax.inject.Inject

/**
 * Screen with list of notifications.
 */
class NotificationActivity : AppActivity(), INotificationActivity {

    private var binding: ActivityNotificationBinding? = null

    @Inject internal lateinit var viewModel: INotificationViewModel

    private val alarmControl by lazy { AlarmControl[this] }
    private val bindControl by lazy { BindControl[this] }

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

    private var snackbar: Snackbar? = null

    private val snackbarCallback = object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)

            /**
             * If user not click on action button. (timeout dismiss, dismiss by swipe)
             */
            if (event != DISMISS_EVENT_ACTION) {
                viewModel.onSnackbarDismiss()
            }
        }
    }

    private val parentContainer by lazy { findViewById<ViewGroup?>(R.id.notification_parent_container) }
    private val contentContainer by lazy { findViewById<ViewGroup?>(R.id.notification_content_container) }
    private val emptyInfoView by lazy { findViewById<View?>(R.id.notification_info_include) }
    private val progressBar by lazy { findViewById<View?>(R.id.notification_progress)}
    private val recyclerView by lazy { findViewById<RecyclerView?>(R.id.notification_recycler) }

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getNotificationBuilder().set(activity = this).build()
                .inject(activity = this)

        super.onCreate(savedInstanceState)
        binding = inflateBinding(R.layout.activity_notification)

        alarmControl.initLazy()
        bindControl.initLazy()

        openState.get(savedInstanceState)

        viewModel.onSetup()
    }

    override fun onResume() {
        super.onResume()

        openState.clear()
        viewModel.onUpdateData()
    }

    override fun onPause() {
        super.onPause()

        snackbar?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()

        snackbar?.removeCallback(snackbarCallback)

        viewModel.onSnackbarDismiss()
        viewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply { openState.save(bundle = this) })
    }

    override fun setupToolbar() {
        findViewById<Toolbar>(R.id.toolbar_container).apply {
            title = getString(R.string.title_notification)
            navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
            setNavigationOnClickListener { finish() }
        }
    }

    override fun setupRecycler(@Theme theme: Int) {
        adapter.theme = theme

        recyclerView?.let {
            it.itemAnimator = object : DefaultItemAnimator() {
                override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) {
                    onBindingList()
                    openState.clear()
                }
            }

            it.setHasFixedSize(true)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
    }


    override fun beforeLoad() {
        emptyInfoView?.visibility = View.GONE
        progressBar?.visibility = View.GONE
    }

    override fun showProgress() {
        progressBar?.visibility = View.VISIBLE
    }


    override fun onBindingList() {
        progressBar?.visibility = View.GONE

        val isListEmpty = adapter.itemCount == 0

        parentContainer?.createVisibleAnim(emptyInfoView, isListEmpty)

        binding?.apply { this.isListEmpty = isListEmpty }?.executePendingBindings()
    }

    override fun startNoteActivity(item: NotificationItem) {
        startActivity(NoteActivity[this, item])
    }

    override fun showSnackbar() {
        val view = contentContainer ?: return

        /**
         * Need remove callback for old [snackbar], because it will call
         * [Snackbar.Callback.onDismissed] after show new one.
         */
        snackbar?.removeCallback(snackbarCallback)

        Snackbar.make(view, R.string.snackbar_message_notification, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_action_notification) { viewModel.onSnackbarAction() }
                .addCallback(snackbarCallback)
                .also { snackbar = it }
                .show()
    }

    override fun notifyList(list: List<NotificationItem>) = adapter.notifyList(list)

    override fun notifyItemRemoved(list: List<NotificationItem>, p: Int) {
        adapter.setList(list).notifyItemRemoved(p)
    }

    override fun notifyItemInserted(list: List<NotificationItem>, p: Int) {
        adapter.setList(list).notifyItemInserted(p)

//        if (p < layoutManager.findFirstCompletelyVisibleItemPosition()
//                || p > layoutManager.findLastCompletelyVisibleItemPosition()) {
//            recyclerView?.smoothScrollToPosition(p)
//        }
    }

    override fun setAlarm(calendar: Calendar, id: Long) {
        alarmControl.set(calendar, id, showToast = false)
    }

    override fun cancelAlarm(id: Long) = alarmControl.cancel(id)

    override fun notifyInfoBind(count: Int) = bindControl.notifyInfo(count)

    companion object {
        operator fun get(context: Context) = Intent(context, NotificationActivity::class.java)
    }

}