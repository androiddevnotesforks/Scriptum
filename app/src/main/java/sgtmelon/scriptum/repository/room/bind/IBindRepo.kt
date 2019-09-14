package sgtmelon.scriptum.repository.room.bind

import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Interface for communication with [BindRepo]
 *
 * @author SerjantArbuz
 */
interface IBindRepo {

    fun getRollList(noteId: Long): List<RollEntity>

    fun unbindNote(id: Long): NoteEntity?

}