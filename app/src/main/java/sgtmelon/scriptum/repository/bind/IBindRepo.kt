package sgtmelon.scriptum.repository.bind

import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollItem

/**
 * Интерфейс для общения с [BindRepo]
 *
 * @author SerjantArbuz
 */
interface IBindRepo {

    /**
     * @throws ClassCastException если [NoteEntity.type] != [NoteType.ROLL]
     */
    fun getRollList(noteEntity: NoteEntity): List<RollItem>

    fun unbindNote(id: Long): NoteEntity

}