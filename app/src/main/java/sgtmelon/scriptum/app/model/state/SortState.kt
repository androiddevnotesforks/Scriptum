package sgtmelon.scriptum.app.model.state

import sgtmelon.scriptum.app.model.item.SortItem
import sgtmelon.scriptum.office.annot.def.SortDef

/**
 * Состояние для сортировки, определяющее позицию элемента [SortItem], до которого будет происходить сортировка
 */
class SortState {

    var end: Int = 0
        private set

    fun updateEnd(listSort: List<SortItem>) {
        for (i in listSort.indices) {
            val key = listSort[i].key

            if (key == SortDef.create || key == SortDef.change) {
                end = i
                break
            }
        }
    }

}