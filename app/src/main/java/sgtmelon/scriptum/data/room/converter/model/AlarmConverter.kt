package sgtmelon.scriptum.data.room.converter.model

import sgtmelon.scriptum.data.room.entity.AlarmEntity
import sgtmelon.scriptum.domain.model.item.NoteItem

/**
 * Converter for [AlarmEntity] and [NoteItem]
 */
class AlarmConverter {

    fun toEntity(item: NoteItem) = with(item) { AlarmEntity(alarmId, id, alarmDate) }

}