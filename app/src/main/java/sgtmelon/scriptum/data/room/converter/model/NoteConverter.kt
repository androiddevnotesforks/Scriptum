package sgtmelon.scriptum.data.room.converter.model

import sgtmelon.scriptum.data.room.entity.AlarmEntity
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RollEntity
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.key.NoteType

/**
 * Converter for [NoteEntity]/[RollEntity]/[AlarmEntity] and [NoteItem]
 */
class NoteConverter {

    fun toItem(noteEntity: NoteEntity,
               rollList: MutableList<RollItem> = ArrayList(),
               alarmEntity: AlarmEntity? = null): NoteItem = with(noteEntity){
        return@with when(noteEntity.type) {
            NoteType.TEXT -> alarmEntity?.let {
                NoteItem.Text(
                        id, create, change, name, text, color, rankId, rankPs, isBin, isStatus,
                        it.id, it.date
                )
            } ?: run {
                NoteItem.Text(
                        id, create, change, name, text, color, rankId, rankPs, isBin, isStatus
                )
            }
            NoteType.ROLL -> alarmEntity?.let {
                NoteItem.Roll(
                        id, create, change, name, text, color, rankId, rankPs, isBin, isStatus,
                        it.id, it.date, rollList
                )
            } ?: run {
                NoteItem.Roll(
                        id, create, change, name, text, color, rankId, rankPs, isBin, isStatus,
                        list = rollList
                )
            }
        }
    }

    fun toEntity(item: NoteItem) = with(item) {
        NoteEntity(id, create, change, name, text, color, type, rankId, rankPs, isBin, isStatus)
    }

    fun toEntity(list: List<NoteItem>): MutableList<NoteEntity> {
        return list.map { toEntity(it) }.toMutableList()
    }

}