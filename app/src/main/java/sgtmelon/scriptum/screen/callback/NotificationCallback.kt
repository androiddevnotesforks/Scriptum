package sgtmelon.scriptum.screen.callback

import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.screen.view.NotificationActivity
import sgtmelon.scriptum.screen.vm.NotificationViewModel

/**
 * Интерфейс для общения [NotificationViewModel] с [NotificationActivity]
 *
 * @author SerjantArbuz
 */
interface NotificationCallback {

    fun notifyDataSetChanged(list: MutableList<NotificationItem>)

    fun testToast(text: String)

}