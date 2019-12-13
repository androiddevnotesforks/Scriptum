package sgtmelon.scriptum.adapter.diff

import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.model.item.NoteItem

/**
 * Diff for [NoteAdapter]
 */
class NoteDiff : ParentDiff<NoteItem>() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

}