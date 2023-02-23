package sgtmelon.scriptum.infrastructure.adapter.diff

import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentDiff

/**
 * Diff for [NotificationItem].
 */
class NotificationDiff : ParentDiff<NotificationItem>() {

    override fun areItemsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
        return oldItem.note.id == newItem.note.id && oldItem.alarm.id == newItem.alarm.id
    }
}