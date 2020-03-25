package sgtmelon.scriptum.presentation.adapter.diff

import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.presentation.adapter.NotificationAdapter

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