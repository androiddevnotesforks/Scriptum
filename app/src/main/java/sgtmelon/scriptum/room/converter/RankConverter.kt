package sgtmelon.scriptum.room.converter

import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Converter for [RankEntity] and [RankItem]
 */
class RankConverter {

    fun toItem(entity: RankEntity) = with(entity) {
        RankItem(id, noteId, position, name, isVisible)
    }

    fun toItem(list: List<RankEntity>) = list.map { toItem(it) }

    fun toEntity(item: RankItem) = with(item) {
        RankEntity(id, noteId, position, name, isVisible)
    }

    fun toEntity(list: List<RankItem>) = list.map { toEntity(it) }

}