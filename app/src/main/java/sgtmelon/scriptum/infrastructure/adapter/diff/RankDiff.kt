package sgtmelon.scriptum.infrastructure.adapter.diff

import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentDiff

/**
 * Diff for [RankItem].
 */
class RankDiff : ParentDiff<RankItem>() {

    override fun areItemsTheSame(oldItem: RankItem, newItem: RankItem): Boolean {
        return oldItem.id == newItem.id
    }
}