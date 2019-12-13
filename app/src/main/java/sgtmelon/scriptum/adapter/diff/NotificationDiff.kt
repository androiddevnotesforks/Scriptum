package sgtmelon.scriptum.adapter.diff

import sgtmelon.scriptum.adapter.NotificationAdapter
import sgtmelon.scriptum.model.item.NotificationItem

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