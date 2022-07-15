package sgtmelon.scriptum.cleanup.presentation.adapter.diff

import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.presentation.adapter.NotificationAdapter

/**
 * Diff for [NotificationAdapter]
 */
class NotificationDiff : ParentDiff<NotificationItem>() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.note.id == newItem.note.id && oldItem.alarm.id == newItem.alarm.id
    }

}