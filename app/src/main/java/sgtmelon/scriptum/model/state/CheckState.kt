package sgtmelon.scriptum.model.state

import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.office.utils.HelpUtils.Note.isAllCheck


/**
 * Состояние для отметок, определяющее отмечено ли всё в списке с элементами [RollItem]
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

}
