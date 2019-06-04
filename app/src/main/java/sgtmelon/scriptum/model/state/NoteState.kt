package sgtmelon.scriptum.model.state

/**
 * Состояние для фрагментов с заметками, необходимое для управления её редактированием
 *
 * @author SerjantArbuz
 */
class NoteState(var isCreate: Boolean, var isBin: Boolean = false) {

    var isEdit: Boolean = false

    init {
        isEdit = isCreate
    }

    inline fun ifCreate(func: () -> Unit) {
        if (isCreate) {
            isCreate = false
            func()
        }
    }

    /**
     * @return - Строка для Log
     */
    override fun toString() = "create = $isCreate | edit = $isEdit | bin = $isBin"

}