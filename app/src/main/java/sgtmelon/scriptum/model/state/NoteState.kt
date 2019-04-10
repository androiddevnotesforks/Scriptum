package sgtmelon.scriptum.model.state

/**
 * Состояние для фрагментов с заметками, необходимое для управления её редактированием
 */
class NoteState(var isCreate: Boolean, var isBin: Boolean = false) {

    var isEdit: Boolean = false

    init {
        isEdit = isCreate
    }

    inline fun ifCreate(function: () -> Unit) {
        if (isCreate) {
            isCreate = false
            function.invoke()
        }
    }

    /**
     * @return - Строка для Log
     */
    override fun toString() = "create = $isCreate | edit = $isEdit | bin = $isBin"

}