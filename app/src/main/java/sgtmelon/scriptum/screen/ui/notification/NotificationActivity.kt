package sgtmelon.scriptum.screen.ui.notification

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
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.control.bind.IBindControl
import sgtmelon.scriptum.databinding.ActivityNotificationBinding
import sgtmelon.scriptum.extension.createVisibleAnim
import sgtmelon.scriptum.extension.getTintDrawable
import sgtmelon.scriptum.extension.inflateBinding
import sgtmelon.scriptum.extension.initLazy
import sgtmelon.scriptum.factory.ViewModelFactory
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.receiver.AlarmReceiver
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.ui.callback.notification.INotificationActivity
import sgtmelon.scriptum.screen.ui.note.NoteActivity

/**
 * Screen with list of notifications
 */
class NotificationActivity : AppActivity(), INotificationActivity {

    private var binding: ActivityNotificationBinding? = null

    private val iViewModel by lazy { ViewModelFactory.getNotificationViewModel(activity = this) }

    private val iAlarmControl by lazy { AlarmControl[this] }
    private val iBindControl: IBindControl by lazy { BindControl(context = this) }

    private val openState = OpenState()

    private val adapter: NotificationAdapter by lazy {
        NotificationAdapter(object: ItemListener.Click {
            override fun onItemClick(view: View, p: Int) = openState.tryInvoke {
                when (view.id) {
                    R.id.notification_click_container -> iViewModel.onClickNote(p)
                    R.id.notification_cancel_button -> iViewModel.onClickCancel(p)
                }
            }
        })
    }

    private val parentContainer by lazy { findViewById<ViewGroup?>(R.id.notification_parent_container) }
    private val emptyInfoView by lazy { findViewById<View?>(R.id.notification_info_include) }
    private val recyclerView by lazy { findViewById<RecyclerView?>(R.id.notification_recycler) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateBinding(R.layout.activity_notification)

        iAlarmControl.initLazy()
        iBindControl.initLazy()
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
                    bindList()
                    openState.clear()
                }
            }

            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }
    }

    override fun bindList() {
        val isListEmpty = adapter.itemCount == 0

        parentContainer?.createVisibleAnim(emptyInfoView, isListEmpty)

        binding?.apply { this.isListEmpty = isListEmpty }?.executePendingBindings()
    }

    override fun startNoteActivity(notificationItem: NotificationItem) {
        startActivity(NoteActivity[this, notificationItem])
    }

    override fun notifyDataSetChanged(list: MutableList<NotificationItem>) =
            adapter.notifyDataSetChanged(list)

    override fun notifyItemRemoved(p: Int, list: MutableList<NotificationItem>) =
            adapter.notifyItemRemoved(p, list)


    override fun cancelAlarm(model: AlarmReceiver.Model) = iAlarmControl.cancel(model)

    override fun notifyInfoBind(count: Int) = iBindControl.notifyInfo(count)

    companion object {
        operator fun get(context: Context) = Intent(context, NotificationActivity::class.java)
    }

}