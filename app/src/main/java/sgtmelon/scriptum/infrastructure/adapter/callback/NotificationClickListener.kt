package sgtmelon.scriptum.infrastructure.adapter.callback

import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem

/**
 * Callback for catch notification events.
 */
interface NotificationClickListener {

    fun onNotificationClick(item: NotificationItem)

    fun onNotificationCancel(p: Int)
}