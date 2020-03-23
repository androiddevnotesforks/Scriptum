package sgtmelon.scriptum.interactor.callback.note

import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.note.TextNoteInteractor
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.SaveControl
import sgtmelon.scriptum.presentation.screen.vm.impl.note.TextNoteViewModel
import java.util.*

/**
 * Interface for communication [TextNoteViewModel] with [TextNoteInteractor]
 */
interface ITextNoteInteractor : IParentInteractor {

    fun getSaveModel(): SaveControl.Model

    @Theme val theme: Int

    @Color val defaultColor: Int


    suspend fun getItem(id: Long): NoteItem?

    suspend fun getRankDialogItemArray(): Array<String>


    suspend fun getRankId(check: Int): Long

    suspend fun getDateList(): List<String>

    suspend fun clearDate(noteItem: NoteItem)

    suspend fun setDate(noteItem: NoteItem, calendar: Calendar)

    suspend fun convert(noteItem: NoteItem)


    suspend fun restoreNote(noteItem: NoteItem)

    suspend fun updateNote(noteItem: NoteItem, updateBind: Boolean)

    suspend fun clearNote(noteItem: NoteItem)

    suspend fun saveNote(noteItem: NoteItem, isCreate: Boolean)

    suspend fun deleteNote(noteItem: NoteItem)

}