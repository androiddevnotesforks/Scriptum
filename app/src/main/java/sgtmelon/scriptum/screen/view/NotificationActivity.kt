package sgtmelon.scriptum.screen.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.utils.getTintDrawable
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        setupToolbar()
    }

    private fun setupToolbar() = findViewById<Toolbar>(R.id.toolbar_container).apply {
        title = getString(R.string.title_notification)
        navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
        setNavigationOnClickListener { finish() }
    }

    companion object {
        fun getInstance(context: Context) = Intent(context, NotificationActivity::class.java)
    }

}