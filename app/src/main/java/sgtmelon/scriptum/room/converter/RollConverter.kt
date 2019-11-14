package sgtmelon.scriptum.room.converter

import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Converter for [RollEntity] and [RollItem]
 */
class RollConverter {

    fun toItem(entity: RollEntity) = with(entity) { RollItem(id, position, isCheck, text) }

    fun toItem(list: MutableList<RollEntity>) = list.map { toItem(it) }.toMutableList()

    fun toEntity(noteId: Long, item: RollItem) = with(item) {
        RollEntity(id, noteId, position, isCheck, text)
    }

}