package sgtmelon.scriptum.cleanup.domain.interactor.callback.note

import sgtmelon.scriptum.cleanup.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

/**
 * Parent interface for [ITextNoteInteractor] and [IRollNoteInteractor].
 */
interface IParentNoteInteractor<N : NoteItem> : IParentInteractor {

    suspend fun getItem(id: Long): N?

    suspend fun getRankDialogItemArray(emptyName: String): Array<String>


    suspend fun getRankId(check: Int): Long


    suspend fun convertNote(item: N)

    suspend fun updateNote(item: N)

    suspend fun saveNote(item: N, isCreate: Boolean)

}