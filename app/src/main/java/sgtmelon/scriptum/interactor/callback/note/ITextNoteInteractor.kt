package sgtmelon.scriptum.interactor.callback.note

import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.note.TextNoteInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import java.util.*

/**
 * Interface for communicate with [TextNoteInteractor]
 */
interface ITextNoteInteractor : IParentInteractor {

    @Theme val theme: Int

    @Color val defaultColor: Int

    fun isRankEmpty(): Boolean

    fun getModel(id: Long, updateBind: Boolean): NoteModel?

    fun getRankDialogItemArray(): Array<String>

    suspend fun restoreNote(noteModel: NoteModel)

    suspend fun updateNote(noteModel: NoteModel)

    suspend fun clearNote(noteModel: NoteModel)

    fun saveNote(noteModel: NoteModel, isCreate: Boolean)

    suspend fun deleteNote(noteModel: NoteModel)

    fun getRankId(check: Int): Long

    suspend fun getDateList(): List<String>

    suspend fun clearDate(noteModel: NoteModel)

    suspend fun setDate(noteModel: NoteModel, calendar: Calendar)

    fun convert(noteModel: NoteModel)

}