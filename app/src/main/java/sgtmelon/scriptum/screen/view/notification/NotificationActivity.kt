package sgtmelon.scriptum.screen.view.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NotificationAdapter
import sgtmelon.scriptum.databinding.ActivityNotificationBinding
import sgtmelon.scriptum.extension.createVisibleAnim
import sgtmelon.scriptum.extension.getTintDrawable
import sgtmelon.scriptum.extension.inflateBinding
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.NotificationItem
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.screen.callback.notification.NotificationCallback
import sgtmelon.scriptum.screen.view.AppActivity
import sgtmelon.scriptum.screen.vm.notification.NotificationViewModel

/**
 * Экран со списком уведомлений
 *
 * @author SerjantArbuz
 */
class NotificationActivity : AppActivity(), NotificationCallback {

    private var binding: ActivityNotificationBinding? = null

    private val viewModel: NotificationViewModel by lazy {
        ViewModelProviders.of(this).get(NotificationViewModel::class.java).apply {
            callback = this@NotificationActivity
        }
    }

    private val openState = OpenState()

    private lateinit var adapter: NotificationAdapter

    private var parentContainer: ViewGroup? = null
    private var emptyInfoView: View? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateBinding(R.layout.activity_notification)

        openState.get(savedInstanceState)
        viewModel.onSetup()
    }

    override fun onResume() {
        super.onResume()

        openState.clear()
        viewModel.onUpdateData()
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
        parentContainer = findViewById(R.id.notification_parent_container)
        emptyInfoView = findViewById(R.id.notification_info_include)

        adapter = NotificationAdapter(theme, ItemListener.Click { v, p ->
            when (v.id) {
                R.id.notification_click_container -> openState.tryInvoke { viewModel.onClickNote(p) }
                R.id.notification_cancel_button -> viewModel.onClickCancel(p)
            }
        })

        recyclerView = findViewById(R.id.notification_recycler)
        recyclerView?.let {
            it.itemAnimator = object : DefaultItemAnimator() {
                override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) = bind()
            }

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

    companion object {
        fun getInstance(context: Context) = Intent(context, NotificationActivity::class.java)
    }

}