package sgtmelon.scriptum.infrastructure.adapter.callback

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

/**
 * Callback for catch note events.
 */
interface NoteClickListener {

    fun onItemClick(item: NoteItem)

    fun onItemLongClick(item: NoteItem, p: Int)
}