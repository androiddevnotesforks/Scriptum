package sgtmelon.scriptum.cleanup.presentation.adapter.diff

import sgtmelon.scriptum.cleanup.domain.model.item.RankItem

/**
 * Diff for [RankItem].
 */
class RankDiff : ParentDiff<RankItem>() {

    override fun areItemsTheSame(oldItem: RankItem, newItem: RankItem): Boolean {
        return oldItem.id == newItem.id
    }
}