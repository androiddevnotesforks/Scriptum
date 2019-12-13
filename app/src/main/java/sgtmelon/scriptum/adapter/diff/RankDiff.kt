package sgtmelon.scriptum.adapter.diff

import sgtmelon.scriptum.model.item.RankItem

class RankDiff : ParentDiff<RankItem>() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

}