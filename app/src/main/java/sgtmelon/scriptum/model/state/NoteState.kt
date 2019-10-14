package sgtmelon.scriptum.model.state

/**
 * State for fragments with notes, need for control editing
 */
class NoteState(var isCreate: Boolean = false, var isBin: Boolean = false) {

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

}