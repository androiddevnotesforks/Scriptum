package sgtmelon.scriptum.data.room.converter.model

import sgtmelon.scriptum.data.room.entity.AlarmEntity
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RollEntity
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem

/**
 * Converter for [NoteEntity]/[RollEntity]/[AlarmEntity] and [NoteItem]
 */
class NoteConverter {

    fun toItem(noteEntity: NoteEntity,
               rollList: MutableList<RollItem> = ArrayList(),
               alarmEntity: AlarmEntity? = null): NoteItem = with(noteEntity){
        return@with if (alarmEntity == null) {
            NoteItem(
                    id, create, change, name, text, color, type, rankId, rankPs, isBin, isStatus,
                    rollList
            )
        } else {
            NoteItem(
                    id, create, change, name, text, color, type, rankId, rankPs, isBin, isStatus,
                    rollList, alarmEntity.id, alarmEntity.date
            )
        }
    }

    fun toEntity(item: NoteItem) = with(item) {
        NoteEntity(id, create, change, name, text, color, type, rankId, rankPs, isBin, isStatus)
    }

}