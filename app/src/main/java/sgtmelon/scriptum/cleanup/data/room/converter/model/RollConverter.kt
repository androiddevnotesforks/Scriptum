package sgtmelon.scriptum.cleanup.data.room.converter.model

import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem

/**
 * Converter for [RollEntity] and [RollItem]
 */
class RollConverter {

    fun toItem(entity: RollEntity) = with(entity) { RollItem(id, position, isCheck, text) }

    fun toItem(list: MutableList<RollEntity>) = list.map { toItem(it) }.toMutableList()

    fun toEntity(noteId: Long, item: RollItem) = with(item) {
        RollEntity(id, noteId, position, isCheck, text)
    }

    fun toEntity(noteId: Long, list: List<RollItem>): MutableList<RollEntity> {
        return list.map { toEntity(noteId, it) }.toMutableList()
    }

}