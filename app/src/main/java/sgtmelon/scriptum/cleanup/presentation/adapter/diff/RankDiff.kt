package sgtmelon.scriptum.cleanup.presentation.adapter.diff

import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.presentation.adapter.RankAdapter

/**
 * Diff for [RankAdapter]
 */
class RankDiff : ParentDiff<RankItem>() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

}