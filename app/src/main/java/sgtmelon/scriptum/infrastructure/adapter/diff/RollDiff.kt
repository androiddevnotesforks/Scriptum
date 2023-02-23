package sgtmelon.scriptum.infrastructure.adapter.diff

import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentDiff

/**
 * Diff for [RollItem].
 */
class RollDiff : ParentDiff<RollItem>() {

    override fun areItemsTheSame(oldItem: RollItem, newItem: RollItem): Boolean {
        return when {
            oldItem.id != null && newItem.id != null && oldItem.id == newItem.id -> true
            oldItem.uniqueId == newItem.uniqueId -> true
            else -> false
        }
    }
}