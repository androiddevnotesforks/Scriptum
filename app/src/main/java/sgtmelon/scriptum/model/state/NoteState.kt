package sgtmelon.scriptum.model.state

/**
 * State for fragments with notes, need for control editing
 */
class NoteState(var isCreate: Boolean = ND_CREATE, var isBin: Boolean = ND_BIN) {

    var isEdit: Boolean = ND_EDIT

    init {
        isEdit = isCreate
    }

    inline fun ifCreate(func: () -> Unit) {
        if (isCreate) {
            isCreate = ND_CREATE
            func()
        }
    }

    companion object {
        const val ND_CREATE = false
        const val ND_EDIT = false
        const val ND_BIN = false
    }

}