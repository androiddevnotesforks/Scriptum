package sgtmelon.scriptum.cleanup.domain.interactor.callback.note

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

/**
 * Parent interface for [ITextNoteInteractor] and [IRollNoteInteractor].
 */
interface IParentNoteInteractor<N : NoteItem> {

    suspend fun getItem(id: Long): N?

    suspend fun convertNote(item: N)
}