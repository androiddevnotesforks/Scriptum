package sgtmelon.scriptum.office.state

import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.office.utils.HelpUtils

/**
 * Состояние для отметок, определяющее отмечено ли всё в списке с элементами [RollItem]
 */
class CheckState {

    var isAll: Boolean = false
        private set

    fun setAll(listRoll: List<RollItem>) {
        isAll = HelpUtils.Note.isAllCheck(listRoll)
    }

    /**
     * @param check - Количество отметок
     * @param size  - Размер списка
     * @return - Произошло ли изменение состояния
     */
    fun setAll(check: Int, size: Int): Boolean {
        val all = check == size

        if (this.isAll != all) {
            this.isAll = all
            return true
        }

        return false
    }

}
