package sgtmelon.scriptum.infrastructure.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

/**
 * Diff for [NoteItem].
 */
class NoteDiff : DiffUtil.ItemCallback<NoteItem>() {

    override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
        return oldItem == newItem
    }
}