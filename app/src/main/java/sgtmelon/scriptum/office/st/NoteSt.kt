package sgtmelon.scriptum.office.st

/**
 * Состояние для фрагментов с заметками, необходимое для управления её редактированием
 */
class NoteSt(var isCreate: Boolean) {

    var isEdit: Boolean = false
    var isBin: Boolean = false
    var isFirst = true

    init {
        isEdit = isCreate
    }

    /**
     * @return - Строка для Log
     */
    override fun toString(): String {
        return "create = $isCreate | first = $isFirst | edit = $isEdit | bin = $isBin"
    }

}