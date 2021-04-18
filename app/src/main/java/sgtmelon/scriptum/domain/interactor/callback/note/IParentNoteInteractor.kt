package sgtmelon.scriptum.domain.interactor.callback.note

import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.note.save.SaveControl
import java.util.*

/**
 * Parent interface for [ITextNoteInteractor] and [IRollNoteInteractor].
 */
interface IParentNoteInteractor<N : NoteItem> : IParentInteractor, SaveControl.Setup {

    @Color val defaultColor: Int


    suspend fun getItem(id: Long): N?

    suspend fun getRankDialogItemArray(emptyName: String): Array<String>


    suspend fun getRankId(check: Int): Long

    suspend fun getDateList(): List<String>

    suspend fun clearDate(item: N)

    suspend fun setDate(item: N, calendar: Calendar)


    suspend fun convertNote(item: N)

    suspend fun restoreNote(item: N)

    suspend fun updateNote(item: N, updateBind: Boolean)

    suspend fun clearNote(item: N)

    suspend fun saveNote(item: N, isCreate: Boolean)

    suspend fun deleteNote(item: N)

}