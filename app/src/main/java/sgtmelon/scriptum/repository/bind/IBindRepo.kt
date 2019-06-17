package sgtmelon.scriptum.repository.bind

import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Интерфейс для общения с [BindRepo]
 *
 * @author SerjantArbuz
 */
interface IBindRepo {

    /**
     * @throws ClassCastException если [NoteEntity.type] != [NoteType.ROLL]
     */
    fun getRollList(noteEntity: NoteEntity): List<RollEntity>

    fun unbindNote(id: Long): NoteEntity

}