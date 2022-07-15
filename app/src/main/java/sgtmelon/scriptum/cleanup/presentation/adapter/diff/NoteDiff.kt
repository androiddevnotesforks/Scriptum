package sgtmelon.scriptum.cleanup.presentation.adapter.diff

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.adapter.NoteAdapter

/**
 * Diff for [NoteAdapter]
 */
class NoteDiff : ParentDiff<NoteItem>() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

}