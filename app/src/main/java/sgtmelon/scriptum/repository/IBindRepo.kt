package sgtmelon.scriptum.repository

import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.NoteType

/**
 * Интерфейс для общения с [BindRepo]
 *
 * @author SerjantArbuz
 */
interface IBindRepo {

    fun getNoteItem(id: Long) : NoteItem

    /**
     * @throws ClassCastException если [NoteItem.type] != [NoteType.ROLL]
     */
    fun getRollList(noteItem: NoteItem): List<RollItem>

}