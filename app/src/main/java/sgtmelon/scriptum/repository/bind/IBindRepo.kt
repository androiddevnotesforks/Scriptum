package sgtmelon.scriptum.repository.bind

import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.entity.NoteItem
import sgtmelon.scriptum.room.entity.RollItem

/**
 * Интерфейс для общения с [BindRepo]
 *
 * @author SerjantArbuz
 */
interface IBindRepo {

    /**
     * @throws ClassCastException если [NoteItem.type] != [NoteType.ROLL]
     */
    fun getRollList(noteItem: NoteItem): List<RollItem>

    fun unbindNoteItem(id: Long): NoteItem

}