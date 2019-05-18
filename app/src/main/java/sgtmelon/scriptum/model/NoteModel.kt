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
                val rollList: MutableList<RollItem>,
                @field:Ignore val statusItem: StatusItem) {

    /**
     * При отметке всех пунктов
     */
    fun updateCheck(check: Boolean) = rollList.forEach { it.isCheck = check }

    fun updateStatus(status: Boolean) =
            if (status) statusItem.notifyNote() else statusItem.cancelNote()

    fun updateStatus(rankVisibleList: List<Long>) = statusItem.updateNote(noteItem, rankVisibleList)

    fun isSaveEnabled(): Boolean = when (noteItem.type) {
        NoteType.TEXT -> noteItem.text.isNotEmpty()
        NoteType.ROLL -> {
            if (rollList.isNotEmpty()) rollList.forEach { if (it.text.isNotEmpty()) return true }
            false
        }
    }

}