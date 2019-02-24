package sgtmelon.scriptum.office.state

/**
 * Состояние для фрагментов с заметками, необходимое для управления её редактированием
 */
class NoteState(var isCreate: Boolean,
                var isBin: Boolean = false) {

    var isEdit: Boolean = false
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