package sgtmelon.scriptum.interactor.notes

import sgtmelon.scriptum.control.alarm.callback.AlarmCallback
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.room.entity.NoteEntity


/**
 * Interface for communication with [NotesInteractor]
 */
interface INotesInteractor {

    @Theme val theme: Int

    fun getList(): MutableList<NoteModel>

    fun isListHide(): Boolean

    fun updateNote(noteEntity: NoteEntity)

    fun convert(noteModel: NoteModel): NoteModel

    suspend fun deleteNote(noteModel: NoteModel, callback: AlarmCallback.Cancel?)

}