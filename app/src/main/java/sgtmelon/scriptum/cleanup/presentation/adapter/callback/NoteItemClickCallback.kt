package sgtmelon.scriptum.cleanup.presentation.adapter.callback

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

/**
 * Callback for catch note events.
 */
interface NoteItemClickCallback {

    fun onItemClick(item: NoteItem)

    fun onItemLongClick(item: NoteItem, p: Int)
}