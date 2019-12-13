package sgtmelon.scriptum.adapter.diff

import sgtmelon.scriptum.adapter.RollAdapter
import sgtmelon.scriptum.model.item.RollItem

/**
 * Diff for [RollAdapter]
 */
class RollDiff : ParentDiff<RollItem>() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

}