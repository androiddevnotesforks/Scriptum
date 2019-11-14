package sgtmelon.scriptum.model.state

import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.room.entity.RollEntity


/**
 * State which determining everything is checked or not in [RollEntity] list
 */
class CheckState {

    var isAll: Boolean = false
        private set

    fun setAll(list: MutableList<RollItem>) {
        isAll = list.isAllCheck()
    }

    /**
     * Check changes of [isAll], [check] - all checks, [size] - list size
     */
    fun setAll(check: Int, size: Int): Boolean {
        val all = check == size

        if (isAll != all) {
            isAll = all
            return true
        }

        return false
    }

    private fun List<RollItem>.isAllCheck(): Boolean {
        if (isEmpty()) return false

        forEach { if (!it.isCheck) return false }

        return true
    }

}
