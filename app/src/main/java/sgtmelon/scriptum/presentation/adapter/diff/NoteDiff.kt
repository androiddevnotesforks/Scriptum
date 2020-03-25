package sgtmelon.scriptum.presentation.adapter.diff

import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.adapter.NoteAdapter

/**
 * Diff for [NoteAdapter]
 */
class NoteDiff : ParentDiff<NoteItem>() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

}