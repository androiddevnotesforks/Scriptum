package sgtmelon.scriptum.domain.interactor.callback.note

import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.note.save.SaveControl
import java.util.*

/**
 * Parent interface for [ITextNoteInteractor] and [IRollNoteInteractor].
 */
interface IParentNoteInteractor<N : NoteItem> : IParentInteractor {

    fun getSaveModel(): SaveControl.Model

    @Theme val theme: Int

    @Color val defaultColor: Int


    suspend fun getItem(id: Long): N?

    suspend fun getRankDialogItemArray(emptyName: String): Array<String>


    suspend fun getRankId(check: Int): Long

    suspend fun getDateList(): List<String>

    suspend fun clearDate(noteItem: N)

    suspend fun setDate(noteItem: N, calendar: Calendar)

    suspend fun convertNote(noteItem: N)


    suspend fun restoreNote(noteItem: N)

    suspend fun updateNote(noteItem: N, updateBind: Boolean)

    suspend fun clearNote(noteItem: N)

    suspend fun saveNote(noteItem: N, isCreate: Boolean)

    suspend fun deleteNote(noteItem: N)

}