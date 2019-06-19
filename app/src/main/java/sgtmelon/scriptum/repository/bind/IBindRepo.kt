package sgtmelon.scriptum.repository.bind

import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Интерфейс для общения с [BindRepo]
 *
 * @author SerjantArbuz
 */
interface IBindRepo {

    fun getRollList(noteId: Long): List<RollEntity>

    fun unbindNote(id: Long): NoteEntity

}