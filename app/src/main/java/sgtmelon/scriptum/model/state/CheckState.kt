package sgtmelon.scriptum.model.state

import sgtmelon.scriptum.room.entity.RollEntity


/**
 * Состояние для отметок, определяющее отмечено ли всё в списке с элементами [RollEntity]
 *
 * @author SerjantArbuz
 */
class CheckState {

    var isAll: Boolean = false
        private set

    fun setAll(rollList: MutableList<RollEntity>) {
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
        private fun MutableList<RollEntity>.isAllCheck(): Boolean {
            if (isEmpty()) return false

            this.forEach { if (!it.isCheck) return false }

            return true
        }
    }

}
