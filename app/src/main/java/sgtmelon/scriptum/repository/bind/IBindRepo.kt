package sgtmelon.scriptum.repository.bind

import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Interface for communicate with [BindRepo]
 */
interface IBindRepo {

    fun getNoteList(): List<NoteModel>

    fun getRollList(noteId: Long): MutableList<RollEntity>

    fun unbindNote(id: Long): NoteEntity?

    fun getNotificationCount(): Int

}