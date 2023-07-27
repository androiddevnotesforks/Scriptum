package sgtmelon.scriptum.infrastructure.adapter.diff.manual

import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentManualDiff

/**
 * Diff for [RollItem].
 */
class RollContentDiff : ParentManualDiff<RollItem>() {

    override fun areItemsTheSame(oldItem: RollItem, newItem: RollItem): Boolean {
        return when {
            oldItem.id != null && newItem.id != null && oldItem.id == newItem.id -> true
            oldItem.uniqueId == newItem.uniqueId -> true
            else -> false
        }
    }
}