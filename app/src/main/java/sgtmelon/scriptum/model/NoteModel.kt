package sgtmelon.scriptum.model

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.item.StatusItem
import sgtmelon.scriptum.model.key.DbField
import sgtmelon.scriptum.model.key.NoteType

/**
 * Модель заметки
 *
 * @author SerjantArbuz
 */
class NoteModel(@field:Embedded val noteItem: NoteItem,
                @field:Relation(parentColumn = DbField.Note.ID, entityColumn = DbField.Roll.NOTE_ID)
                val listRoll: MutableList<RollItem>,
                @field:Ignore val statusItem: StatusItem) {

    /**
     * При отметке всех пунктов
     */
    fun updateCheck(check: Boolean) = listRoll.forEach { it.isCheck = check }

    fun updateStatus(status: Boolean) =
            if (status) statusItem.notifyNote() else statusItem.cancelNote()

    fun updateStatus(listRankVisible: List<Long>) =
            statusItem.updateNote(noteItem, listRankVisible)

    fun isSaveEnabled(): Boolean = when (noteItem.type) {
        NoteType.TEXT -> noteItem.text.isNotEmpty()
        NoteType.ROLL -> {
            if (listRoll.isNotEmpty()) listRoll.forEach { if (it.text.isNotEmpty()) return true }
            false
        }
    }

}