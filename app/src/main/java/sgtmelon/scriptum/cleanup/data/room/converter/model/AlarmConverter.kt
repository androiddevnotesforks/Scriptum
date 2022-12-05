package sgtmelon.scriptum.cleanup.data.room.converter.model

import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

/**
 * Converter for [AlarmEntity] and [NoteItem]
 */
class AlarmConverter {

    fun toEntity(item: NoteItem) = with(item) { AlarmEntity(alarmId, id, alarmDate) }
}