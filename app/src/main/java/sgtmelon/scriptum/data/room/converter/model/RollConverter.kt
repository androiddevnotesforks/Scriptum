package sgtmelon.scriptum.data.room.converter.model

import sgtmelon.scriptum.data.room.entity.RollEntity
import sgtmelon.scriptum.domain.model.item.RollItem

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