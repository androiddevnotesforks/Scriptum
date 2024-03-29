package sgtmelon.scriptum.infrastructure.adapter.diff.manual

import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentManualDiff

/**
 * Diff for [RankItem].
 */
class RankDiff : ParentManualDiff<RankItem>() {

    override fun areItemsTheSame(oldItem: RankItem, newItem: RankItem): Boolean {
        return oldItem.id == newItem.id
    }
}