package sgtmelon.scriptum.app.model

import sgtmelon.scriptum.app.model.item.RankItem

/**
 * Репозиторий категории
 */
class RankRepo(private val listRank: MutableList<RankItem>,
               private val listName: MutableList<String>) {

    fun getListName(): List<String> {
        return listName
    }

    fun getListRank(): List<RankItem> {
        return listRank
    }

    fun setListRank(listRank: List<RankItem>) {
        this.listRank.clear()
        this.listRank.addAll(listRank)
    }

    fun size(): Int {
        return listRank.size
    }

    fun add(position: Int, rankItem: RankItem) {
        listRank.add(position, rankItem)
        listName.add(position, rankItem.name.toUpperCase())
    }

    fun remove(position: Int) {
        listRank.removeAt(position)
        listName.removeAt(position)
    }

    fun move(positionOld: Int, positionNew: Int) {
        val rankItem = listRank[positionOld]

        remove(positionOld)
        add(positionNew, rankItem)
    }

}