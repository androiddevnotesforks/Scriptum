package sgtmelon.scriptum.app.model

import sgtmelon.scriptum.app.model.item.RankItem

/**
 * Репозиторий категории
 */
class RankRepo(val listRank: MutableList<RankItem>,
               val listName: MutableList<String>
) {

    fun setListRank(listRank: List<RankItem>) {
        this.listRank.clear()
        this.listRank.addAll(listRank)
    }

    fun size(): Int = listRank.size

    fun set(position: Int, name: String) {
        listRank[position].name = name
        listName[position] = name.toUpperCase()
    }

    fun add(position: Int, rankItem: RankItem) {
        listRank.add(position, rankItem)
        listName.add(position, rankItem.name.toUpperCase())
    }

    fun remove(position: Int) {
        listRank.removeAt(position)
        listName.removeAt(position)
    }

    fun move(positionFrom: Int, positionTo: Int) {
        val rankItem = listRank[positionFrom]

        remove(positionFrom)
        add(positionTo, rankItem)
    }

}