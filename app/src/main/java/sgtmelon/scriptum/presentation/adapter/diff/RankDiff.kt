package sgtmelon.scriptum.presentation.adapter.diff

import sgtmelon.scriptum.presentation.adapter.RankAdapter
import sgtmelon.scriptum.model.item.RankItem

/**
 * Diff for [RankAdapter]
 */
class RankDiff : ParentDiff<RankItem>() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

}