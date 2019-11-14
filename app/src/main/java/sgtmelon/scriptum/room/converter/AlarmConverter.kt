package sgtmelon.scriptum.room.converter

import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Converter for [AlarmEntity] and [NoteItem]
 */
class AlarmConverter {

    fun toEntity(item: NoteItem) = with(item) {
        AlarmEntity(item.alarmId, item.id, item.alarmDate)
    }

}