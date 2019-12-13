package sgtmelon.scriptum.adapter.diff

import sgtmelon.scriptum.model.item.RollItem

class RollDiff : ParentDiff<RollItem>() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

}