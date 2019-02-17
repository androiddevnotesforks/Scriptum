package sgtmelon.scriptum.office.state

import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.office.utils.HelpUtils.Note.isAllCheck


/**
 * Состояние для отметок, определяющее отмечено ли всё в списке с элементами [RollItem]
 */
class CheckState {

    var isAll: Boolean = false
        private set

    fun setAll(listRoll: MutableList<RollItem>) {
        isAll = listRoll.isAllCheck()
    }

    /**
     * Проверка произошло ли изменение состояния отметки всех пунктов
     * [check] - Количество отметок, [size] - размер списка
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
