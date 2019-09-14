package sgtmelon.scriptum.repository.bind

import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Interface for communication with [BindRepo]
 */
interface IBindRepo {

    fun getRollList(noteId: Long): List<RollEntity>

    fun unbindNote(id: Long): NoteEntity?

}