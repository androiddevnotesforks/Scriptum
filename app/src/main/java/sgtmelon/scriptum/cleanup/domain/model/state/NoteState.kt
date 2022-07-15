package sgtmelon.scriptum.cleanup.domain.model.state

/**
 * State for fragments with notes, need for control editing
 */
data class NoteState(var isCreate: Boolean = ND_CREATE, var isBin: Boolean = ND_BIN) {

    var isEdit = isCreate

    companion object {
        const val ND_CREATE = false
        const val ND_BIN = false
    }
}