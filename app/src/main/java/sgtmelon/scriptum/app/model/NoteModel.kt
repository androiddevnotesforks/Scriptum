package sgtmelon.scriptum.app.model

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.model.item.StatusItem
import sgtmelon.scriptum.app.model.key.DbField

/**
 * Модель заметки
 *
 * @author SerjantArbuz
 * @version 1.0
 */
class NoteModel(@field:Embedded val noteItem: NoteItem,
                @field:Relation(parentColumn = DbField.Note.ID, entityColumn = DbField.Roll.NOTE_ID)
                val listRoll: MutableList<RollItem>,
                @field:Ignore val statusItem: StatusItem) {

    /**
     * При отметке всех пунктов
     */
    fun updateCheck(check: Boolean) = listRoll.forEach { it.isCheck = check }

    fun updateStatus(status: Boolean) = when (status) {
        true -> statusItem.notifyNote()
        false -> statusItem.cancelNote()
    }

    fun updateStatus(listRankVisible: List<Long>) =
            statusItem.updateNote(noteItem, listRankVisible)

}