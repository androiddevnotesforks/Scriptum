package sgtmelon.scriptum.model.state

import sgtmelon.scriptum.room.entity.RollEntity


/**
 * State which determining everything is checked or not in [RollEntity] list
 */
class CheckState {

    var isAll: Boolean = false
        private set

    fun setAll(list: MutableList<RollEntity>) {
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

    companion object {
        private fun MutableList<RollEntity>.isAllCheck(): Boolean {
            if (isEmpty()) return false

            this.forEach { if (!it.isCheck) return false }

            return true
        }
    }

}
