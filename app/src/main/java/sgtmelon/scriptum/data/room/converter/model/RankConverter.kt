package sgtmelon.scriptum.data.room.converter.model

import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.domain.model.item.RankItem

/**
 * Converter for [RankEntity] and [RankItem]
 */
class RankConverter {

    fun toItem(entity: RankEntity) = with(entity) {
        RankItem(id, noteId, position, name, isVisible)
    }

    fun toItem(list: List<RankEntity>) = list.map { toItem(it) }.toMutableList()

    fun toEntity(item: RankItem) = with(item) {
        RankEntity(id, noteId, position, name, isVisible)
    }

    fun toEntity(list: List<RankItem>) = list.map { toEntity(it) }.toMutableList()

}