package sgtmelon.scriptum.infrastructure.adapter.diff.manual

import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentManualDiff

/**
 * Diff for [NotificationItem].
 */
class NotificationDiff : ParentManualDiff<NotificationItem>() {

    override fun areItemsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
        return oldItem.note.id == newItem.note.id && oldItem.alarm.id == newItem.alarm.id
    }
}