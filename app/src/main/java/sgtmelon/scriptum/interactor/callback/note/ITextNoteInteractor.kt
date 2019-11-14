package sgtmelon.scriptum.interactor.callback.note

import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.note.TextNoteInteractor
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel
import java.util.*

/**
 * Interface for communication [TextNoteViewModel] with [TextNoteInteractor]
 */
interface ITextNoteInteractor : IParentInteractor {

    fun getSaveModel(): SaveControl.Model

    @Theme val theme: Int

    @Color val defaultColor: Int


    fun isRankEmpty(): Boolean

    fun getItem(id: Long, updateBind: Boolean): NoteItem?

    fun getRankDialogItemArray(): Array<String>


    fun getRankId(check: Int): Long

    suspend fun getDateList(): List<String>

    suspend fun clearDate(noteItem: NoteItem)

    suspend fun setDate(noteItem: NoteItem, calendar: Calendar)

    fun convert(noteItem: NoteItem)


    suspend fun restoreNote(noteItem: NoteItem)

    suspend fun updateNote(noteItem: NoteItem, updateBind: Boolean)

    suspend fun clearNote(noteItem: NoteItem)

    fun saveNote(noteItem: NoteItem, isCreate: Boolean)

    suspend fun deleteNote(noteItem: NoteItem)

}