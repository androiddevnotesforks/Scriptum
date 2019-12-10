package sgtmelon.scriptum.interactor.callback.note

import sgtmelon.scriptum.control.SaveControl
import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.note.RollNoteInteractor
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel
import java.util.*

/**
 * Interface for communication [RollNoteViewModel] with [RollNoteInteractor]
 */
interface IRollNoteInteractor : IParentInteractor {

    fun getSaveModel(): SaveControl.Model

    @Theme val theme: Int

    @Color val defaultColor: Int


    suspend fun isRankEmpty(): Boolean

    suspend fun getItem(id: Long, updateBind: Boolean): NoteItem?

    suspend fun getRankDialogItemArray(): Array<String>


    suspend fun updateRollCheck(noteItem: NoteItem, p: Int)

    suspend fun updateRollCheck(noteItem: NoteItem, check: Boolean)

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