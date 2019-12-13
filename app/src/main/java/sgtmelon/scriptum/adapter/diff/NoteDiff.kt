package sgtmelon.scriptum.adapter.diff

import sgtmelon.scriptum.model.item.NoteItem

class NoteDiff : ParentDiff<NoteItem>() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

}