package sgtmelon.scriptum.model

import androidx.room.Embedded
import androidx.room.Relation
import sgtmelon.scriptum.model.annotation.Color
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

    constructor(model: NoteModel) :
            this(model.noteEntity.copy(), model.rollList.toMutableList(), model.alarmEntity.copy())

    // TODO заменить получение модели через один query

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

    companion object {
        fun getCreate(create: String, @Color color: Int, type: NoteType) = NoteModel(NoteEntity(
                create = create,
                color = color,
                type = type
        ))
    }

}