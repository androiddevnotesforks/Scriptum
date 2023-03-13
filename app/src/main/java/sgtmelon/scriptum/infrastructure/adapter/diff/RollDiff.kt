package sgtmelon.scriptum.infrastructure.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem

/**
 * Diff for [RollItem].
 */
class RollDiff : DiffUtil.ItemCallback<RollItem>() {

    override fun areItemsTheSame(oldItem: RollItem, newItem: RollItem): Boolean {
        return when {
            oldItem.id != null && newItem.id != null && oldItem.id == newItem.id -> true
            oldItem.uniqueId == newItem.uniqueId -> true
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: RollItem, newItem: RollItem): Boolean {
        return oldItem == newItem
    }
}