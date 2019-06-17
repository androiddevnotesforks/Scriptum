package sgtmelon.scriptum.model.state

import sgtmelon.scriptum.room.entity.RollItem


/**
 * Состояние для отметок, определяющее отмечено ли всё в списке с элементами [RollItem]
 *
 * @author SerjantArbuz
 */
class CheckState {

    var isAll: Boolean = false
        private set

    fun setAll(rollList: MutableList<RollItem>) {
        isAll = rollList.isAllCheck()
    }

    /**
     * Проверка произошло ли изменение состояния отметки всех пунктов
     * [check] - Количество отметок, [size] - размер списка
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
        private fun MutableList<RollItem>.isAllCheck(): Boolean {
            if (isEmpty()) return false

            this.forEach { if (!it.isCheck) return false }

            return true
        }
    }

}
