package sgtmelon.scriptum.app.model.state

/**
 * Состояние для фрагментов с заметками, необходимое для управления её редактированием
 */
class NoteState(var isCreate: Boolean,
                var isBin: Boolean = false) {

    // TODO extension для if(isCreate)

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