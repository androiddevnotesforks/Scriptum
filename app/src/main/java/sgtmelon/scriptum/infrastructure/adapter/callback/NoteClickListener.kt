package sgtmelon.scriptum.infrastructure.adapter.callback

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

/**
 * Callback for catch note events.
 */
interface NoteClickListener {

    fun onNoteClick(item: NoteItem)

    fun onNoteLongClick(item: NoteItem, p: Int)
}