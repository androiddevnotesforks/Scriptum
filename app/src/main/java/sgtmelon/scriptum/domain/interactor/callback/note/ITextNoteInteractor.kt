package sgtmelon.scriptum.domain.interactor.callback.note

import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.domain.interactor.impl.note.TextNoteInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.presentation.screen.vm.impl.note.TextNoteViewModel
import java.util.*

/**
 * Interface for communication [TextNoteViewModel] with [TextNoteInteractor]
 */
interface ITextNoteInteractor : IParentInteractor {

    fun getSaveModel(): SaveControl.Model?

    @Theme val theme: Int?

    @Color val defaultColor: Int?


    suspend fun getItem(id: Long): NoteItem.Text?

    suspend fun getRankDialogItemArray(emptyName: String): Array<String>?


    suspend fun getRankId(check: Int): Long?

    suspend fun getDateList(): List<String>?

    suspend fun clearDate(noteItem: NoteItem.Text)

    suspend fun setDate(noteItem: NoteItem.Text, calendar: Calendar)

    suspend fun convertNote(noteItem: NoteItem.Text)


    suspend fun restoreNote(noteItem: NoteItem.Text)

    suspend fun updateNote(noteItem: NoteItem.Text, updateBind: Boolean)

    suspend fun clearNote(noteItem: NoteItem.Text)

    suspend fun saveNote(noteItem: NoteItem.Text, isCreate: Boolean)

    suspend fun deleteNote(noteItem: NoteItem.Text)

}