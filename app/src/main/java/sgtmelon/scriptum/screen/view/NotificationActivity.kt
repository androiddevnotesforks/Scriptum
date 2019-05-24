package sgtmelon.scriptum.screen.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NotificationAdapter
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.getTintDrawable
import sgtmelon.scriptum.office.utils.showToast
import sgtmelon.scriptum.screen.callback.NotificationCallback
import sgtmelon.scriptum.screen.vm.NotificationViewModel

/**
 * Экран со списком уведомлений
 *
 * @author SerjantArbuz
 */
class NotificationActivity : AppActivity(), NotificationCallback {

    private val viewModel: NotificationViewModel by lazy {
        ViewModelProviders.of(this).get(NotificationViewModel::class.java).apply {
            callback = this@NotificationActivity
        }
    }

    private val adapter by lazy {
        NotificationAdapter(ItemListener.ClickListener { v, p ->
            when (v.id) {
                R.id.notification_click_container -> openState.tryInvoke { viewModel.onClickNote(p) }
                R.id.notification_cancel_button -> viewModel.onClickCancel(p)
            }
        })
    }

    private var recyclerView: RecyclerView? = null

    private val openState = OpenState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        openState.get(savedInstanceState)

        setupToolbar()
        setupRecycler()
    }

    override fun onResume() {
        super.onResume()

        openState.clear()
        viewModel.onUpdateData()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply { openState.save(bundle = this) })

    private fun setupToolbar() = findViewById<Toolbar>(R.id.toolbar_container).apply {
        title = getString(R.string.title_notification)
        navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
        setNavigationOnClickListener { finish() }
    }

    private fun setupRecycler() {
        recyclerView = findViewById(R.id.notification_recycler)
        recyclerView?.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }
    }

    override fun notifyDataSetChanged(list: MutableList<NotificationItem>) =
            adapter.notifyDataSetChanged(list)

    override fun testToast(text: String) {
        showToast(text)
        openState.clear()
    }

    companion object {
        fun getInstance(context: Context) = Intent(context, NotificationActivity::class.java)
    }

}