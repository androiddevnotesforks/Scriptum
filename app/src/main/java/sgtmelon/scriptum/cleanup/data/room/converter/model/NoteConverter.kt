package sgtmelon.scriptum.cleanup.data.room.converter.model

import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.infrastructure.database.DbData.Alarm
import sgtmelon.scriptum.infrastructure.database.DbData.RollVisible
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.utils.extensions.type

/**
 * Converter for [NoteEntity]/[RollEntity]/[AlarmEntity] and [NoteItem]
 */
class NoteConverter {

    // TODO check null, arraylist null usage
    fun toItem(
        noteEntity: NoteEntity,
        isVisible: Boolean? = null,
        rollList: MutableList<RollItem> = ArrayList(),
        alarmEntity: AlarmEntity? = null
    ): NoteItem = with(noteEntity) {
        val isRollVisible = isVisible ?: RollVisible.Default.VALUE
        val alarmId = alarmEntity?.id ?: Alarm.Default.ID
        val alarmDate = alarmEntity?.date ?: Alarm.Default.DATE

        return@with when (noteEntity.type) {
            NoteType.TEXT -> NoteItem.Text(
                id, create, change, name, text, color, rankId, rankPs, isBin, isStatus,
                alarmId, alarmDate
            )
            NoteType.ROLL -> NoteItem.Roll(
                id, create, change, name, text, color, rankId, rankPs, isBin, isStatus,
                alarmId, alarmDate, isRollVisible, rollList
            )
        }
    }

    fun toEntity(item: NoteItem) = with(item) {
        NoteEntity(id, create, change, name, text, color, type, rankId, rankPs, isBin, isStatus)
    }

    fun toEntity(list: List<NoteItem>): MutableList<NoteEntity> {
        return list.map { toEntity(it) }.toMutableList()
    }

}