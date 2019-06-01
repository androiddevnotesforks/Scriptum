package sgtmelon.scriptum.model.state

import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.item.SortItem

/**
 * Состояние для сортировки, определяющее позицию элемента [SortItem], до которого будет происходить сортировка
 */
class SortState {

    var end: Int = 0
        private set

    fun updateEnd(listSort: List<SortItem>) {
        for (i in listSort.indices) {
            val key = listSort[i].key

            if (key == Sort.create || key == Sort.change) {
                end = i
                break
            }
        }
    }

}