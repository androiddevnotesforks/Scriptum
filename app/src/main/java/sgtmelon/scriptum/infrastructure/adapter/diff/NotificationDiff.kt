package sgtmelon.scriptum.infrastructure.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem

/**
 * Diff for [NotificationItem].
 */
class NotificationDiff : DiffUtil.ItemCallback<NotificationItem>() {

    override fun areItemsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
        return oldItem.note.id == newItem.note.id && oldItem.alarm.id == newItem.alarm.id
    }

    override fun areContentsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean {
        return oldItem == newItem
    }
}