package sgtmelon.scriptum.domain.interactor.callback.note

import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.domain.interactor.note.RollNoteInteractor
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


    suspend fun getItem(id: Long): NoteItem?

    suspend fun getRankDialogItemArray(): Array<String>


    suspend fun setVisible(noteItem: NoteItem, isVisible: Boolean)

    suspend fun getVisible(noteItem: NoteItem): Boolean


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