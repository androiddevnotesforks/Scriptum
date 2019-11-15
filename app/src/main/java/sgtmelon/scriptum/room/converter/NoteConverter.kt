package sgtmelon.scriptum.room.converter

import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity

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