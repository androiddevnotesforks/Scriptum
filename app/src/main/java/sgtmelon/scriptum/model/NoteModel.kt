package sgtmelon.scriptum.model

import androidx.room.Embedded
import androidx.room.Relation
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.DbField
import sgtmelon.scriptum.model.key.NoteType

/**
 * Модель заметки
 *
 * @author SerjantArbuz
 */
class NoteModel(@field:Embedded val noteItem: NoteItem,
                @field:Relation(parentColumn = DbField.Note.ID, entityColumn = DbField.Roll.NOTE_ID)
                val rollList: MutableList<RollItem> = ArrayList()) {

    /**
     * При отметке всех пунктов
     */
    fun updateCheck(check: Boolean) = rollList.forEach { it.isCheck = check }

    fun isSaveEnabled(): Boolean = when (noteItem.type) {
        NoteType.TEXT -> noteItem.text.isNotEmpty()
        NoteType.ROLL -> {
            if (rollList.isNotEmpty()) rollList.forEach { if (it.text.isNotEmpty()) return true }
            false
        }
    }

}