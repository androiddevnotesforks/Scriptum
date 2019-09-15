package sgtmelon.scriptum.interactor.callback.main

import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.interactor.main.NotesInteractor
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

    fun updateNote(noteEntity: NoteEntity, callback: BindControl.Bridge.Notify?)

    fun convert(noteModel: NoteModel, callback: BindControl.Bridge.Notify?): NoteModel

    suspend fun deleteNote(noteModel: NoteModel, alarmCallback: AlarmControl.Bridge.Cancel?,
                           bindCallback: BindControl.Bridge.Cancel?)

}