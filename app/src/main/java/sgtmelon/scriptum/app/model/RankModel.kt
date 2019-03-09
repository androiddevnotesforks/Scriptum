package sgtmelon.scriptum.app.model

import sgtmelon.scriptum.app.model.item.RankItem

/**
 * Репозиторий категории
 */
class RankModel(val listRank: MutableList<RankItem>,
                val listName: MutableList<String>
) {

    fun size(): Int = listRank.size

    fun set(p: Int, name: String) {
        listRank[p].name = name
        listName[p] = name.toUpperCase()
    }

    fun add(p: Int, item: RankItem) {
        listRank.add(p, item)
        listName.add(p, item.name.toUpperCase())
    }

    fun remove(p: Int) {
        listRank.removeAt(p)
        listName.removeAt(p)
    }

    fun move(from: Int, to: Int) {
        val item = listRank[from]

        remove(from)
        add(to, item)
    }

}