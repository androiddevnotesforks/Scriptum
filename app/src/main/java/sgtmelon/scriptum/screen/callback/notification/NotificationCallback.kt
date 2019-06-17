package sgtmelon.scriptum.screen.callback.notification

import android.content.Intent
import sgtmelon.scriptum.model.NotificationItem
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.view.notification.NotificationActivity
import sgtmelon.scriptum.screen.vm.notification.NotificationViewModel

/**
 * Интерфейс для общения [NotificationViewModel] с [NotificationActivity]
 *
 * @author SerjantArbuz
 */
interface NotificationCallback {

    fun setupToolbar()

    fun setupRecycler(@Theme theme: Int)

    fun bind()

    fun notifyDataSetChanged(list: MutableList<NotificationItem>)

    fun notifyItemRemoved(p: Int, list: MutableList<NotificationItem>)

    fun startActivity(intent: Intent)

}