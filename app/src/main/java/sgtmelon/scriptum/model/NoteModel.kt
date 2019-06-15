package sgtmelon.scriptum.model

import androidx.room.Embedded
import androidx.room.Relation
import sgtmelon.scriptum.model.data.DbData
import sgtmelon.scriptum.model.item.AlarmItem
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.NoteType

/**
 * Модель заметки
 *
 * @author SerjantArbuz
 */
class NoteModel(
        @field:Embedded val noteItem: NoteItem,
        @field:Relation(parentColumn = DbData.Note.ID, entityColumn = DbData.Roll.NOTE_ID)
        val rollList: MutableList<RollItem> = ArrayList(),
        @field:Relation(parentColumn = DbData.Note.ID, entityColumn = DbData.Alarm.NOTE_ID)
        val alarmItem: AlarmItem = AlarmItem()
) {

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