package sgtmelon.scriptum.model

import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Модель для хранения краткой информации о списке всех категорий
 *
 * @author SerjantArbuz
 */
class RankModel(val itemList: MutableList<RankEntity>) {

    // TODO сделать получение через один query
    // TODO rename потому что по факту это репозиторий

    val nameList: MutableList<String> = ArrayList<String>().apply {
        itemList.forEach { add(it.name.toUpperCase()) }
    }

    fun size(): Int = itemList.size

    fun set(p: Int, name: String) {
        itemList[p].name = name
        nameList[p] = name.toUpperCase()
    }

    fun add(p: Int, item: RankEntity) {
        itemList.add(p, item)
        nameList.add(p, item.name.toUpperCase())
    }

    fun remove(p: Int) {
        itemList.removeAt(p)
        nameList.removeAt(p)
    }

    fun move(from: Int, to: Int) {
        val item = itemList[from]

        remove(from)
        add(to, item)
    }

}