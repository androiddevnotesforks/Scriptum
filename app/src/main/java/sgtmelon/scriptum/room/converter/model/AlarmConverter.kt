package sgtmelon.scriptum.room.converter.model

import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.room.entity.AlarmEntity

/**
 * Converter for [AlarmEntity] and [NoteItem]
 */
class AlarmConverter {

    fun toEntity(item: NoteItem) = with(item) { AlarmEntity(alarmId, id, alarmDate) }

}