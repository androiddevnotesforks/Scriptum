package sgtmelon.scriptum.domain.interactor.callback.note

import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.domain.interactor.impl.note.RollNoteInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.presentation.screen.vm.impl.note.RollNoteViewModel
import java.util.*

/**
 * Interface for communication [RollNoteViewModel] with [RollNoteInteractor]
 */
interface IRollNoteInteractor : IParentInteractor {

    fun getSaveModel(): SaveControl.Model

    @Theme val theme: Int

    @Color val defaultColor: Int


    suspend fun getItem(id: Long): NoteItem.Roll?

    suspend fun getRankDialogItemArray(): Array<String>


    suspend fun setVisible(noteId: Long, isVisible: Boolean)

    suspend fun getVisible(noteId: Long): Boolean


    suspend fun updateRollCheck(noteItem: NoteItem.Roll, p: Int)

    suspend fun updateRollCheck(noteItem: NoteItem.Roll, check: Boolean)

    suspend fun getRankId(check: Int): Long

    suspend fun getDateList(): List<String>

    suspend fun clearDate(noteItem: NoteItem.Roll)

    suspend fun setDate(noteItem: NoteItem.Roll, calendar: Calendar)

    suspend fun convertNote(noteItem: NoteItem.Roll)


    suspend fun restoreNote(noteItem: NoteItem.Roll)

    suspend fun updateNote(noteItem: NoteItem.Roll, updateBind: Boolean)

    suspend fun clearNote(noteItem: NoteItem.Roll)

    suspend fun saveNote(noteItem: NoteItem.Roll, isCreate: Boolean)

    suspend fun deleteNote(noteItem: NoteItem.Roll)

}