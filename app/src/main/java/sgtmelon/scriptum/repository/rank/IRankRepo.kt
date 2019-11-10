package sgtmelon.scriptum.repository.rank

import sgtmelon.scriptum.model.item.RankItem

/**
 * Interface for communicate with [RankRepo]
 */
interface IRankRepo {

    fun insert(name: String): RankItem

    fun getList(): MutableList<RankItem>

    fun delete(rankItem: RankItem)

    fun update(item: RankItem)

    fun updatePosition(list: List<RankItem>)

}