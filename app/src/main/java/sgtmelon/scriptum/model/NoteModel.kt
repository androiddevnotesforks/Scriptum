package sgtmelon.scriptum.model

import androidx.room.Embedded
import androidx.room.Relation
import sgtmelon.scriptum.model.data.DbData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Модель заметки
 *
 * @author SerjantArbuz
 */
data class NoteModel(
        @Embedded val noteEntity: NoteEntity,
        @Relation(parentColumn = DbData.Note.ID, entityColumn = DbData.Roll.NOTE_ID)
        val rollList: MutableList<RollEntity> = ArrayList(),
        @Embedded val alarmEntity: AlarmEntity = AlarmEntity()
) {

    /**
     * При отметке всех пунктов
     */
    fun updateCheck(check: Boolean) = rollList.forEach { it.isCheck = check }

    fun isSaveEnabled(): Boolean = when (noteEntity.type) {
        NoteType.TEXT -> noteEntity.text.isNotEmpty()
        NoteType.ROLL -> {
            if (rollList.isNotEmpty()) rollList.forEach { if (it.text.isNotEmpty()) return true }
            false
        }
    }

}