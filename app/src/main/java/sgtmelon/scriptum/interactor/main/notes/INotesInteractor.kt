package sgtmelon.scriptum.interactor.main.notes

import sgtmelon.scriptum.control.alarm.callback.IAlarmBridge
import sgtmelon.scriptum.control.notification.IBindBridge
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.room.entity.NoteEntity

/**
 * Interface for communicate with [NotesInteractor]
 */
interface INotesInteractor {

    @Theme val theme: Int

    fun getList(): MutableList<NoteModel>

    fun isListHide(): Boolean

    fun updateNote(noteEntity: NoteEntity, callback: IBindBridge.Notify?)

    fun convert(noteModel: NoteModel, callback: IBindBridge.Notify?): NoteModel

    suspend fun deleteNote(noteModel: NoteModel, alarmCallback: IAlarmBridge.Cancel?,
                           bindCallback: IBindBridge.Cancel?)

}