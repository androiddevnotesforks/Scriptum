package sgtmelon.scriptum.repository.bind

import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Interface for communicate with [BindRepo]
 */
interface IBindRepo {

    fun getRollList(noteId: Long): MutableList<RollEntity>

    fun unbindNote(id: Long): NoteEntity?

}