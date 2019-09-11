package sgtmelon.scriptum.screen.ui.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NotificationAdapter
import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.databinding.ActivityNotificationBinding
import sgtmelon.scriptum.extension.createVisibleAnim
import sgtmelon.scriptum.extension.getTintDrawable
import sgtmelon.scriptum.extension.inflateBinding
import sgtmelon.scriptum.factory.ViewModelFactory
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.ui.callback.notification.INotificationActivity

/**
 * Screen with list of notifications
 *
 * @author SerjantArbuz
 */
class NotificationActivity : AppActivity(), INotificationActivity {

    private var binding: ActivityNotificationBinding? = null

    private val iViewModel by lazy { ViewModelFactory.getNotificationViewModel(activity = this) }

    private val iAlarmControl by lazy { AlarmControl.getInstance(context = this) }

    private val openState = OpenState()

    private val adapter: NotificationAdapter by lazy {
        NotificationAdapter(ItemListener.Click { v, p ->
            when (v.id) {
                R.id.notification_click_container -> openState.tryInvoke { iViewModel.onClickNote(p) }
                R.id.notification_cancel_button -> iViewModel.onClickCancel(p)
            }
        })
    }

    private val parentContainer by lazy { findViewById<ViewGroup?>(R.id.notification_parent_container) }
    private val emptyInfoView by lazy { findViewById<View?>(R.id.notification_info_include) }
    private val recyclerView by lazy { findViewById<RecyclerView?>(R.id.notification_recycler) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateBinding(R.layout.activity_notification)

        openState.get(savedInstanceState)
        iViewModel.onSetup()
    }

    override fun onResume() {
        super.onResume()

        openState.clear()
        iViewModel.onUpdateData()
    }

    override fun onDestroy() {
        super.onDestroy()
        iViewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply { openState.save(bundle = this) })

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
                override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) = bind()
            }

            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }
    }

    override fun bind() {
        val empty = adapter.itemCount == 0

        parentContainer?.createVisibleAnim(emptyInfoView, empty, if (!empty) 0 else 200)

        binding?.apply { isListEmpty = empty }?.executePendingBindings()
    }

    override fun notifyDataSetChanged(list: MutableList<NotificationItem>) =
            adapter.notifyDataSetChanged(list)

    override fun notifyItemRemoved(p: Int, list: MutableList<NotificationItem>) =
            adapter.notifyItemRemoved(p, list)

    override fun cancelAlarm(intent: PendingIntent) = iAlarmControl.cancel(intent)

    companion object {
        fun getInstance(context: Context) = Intent(context, NotificationActivity::class.java)
    }

}