package sgtmelon.scriptum.cleanup.domain.model.state

/**
 * State for fragments with notes, need for control editing
 */
data class NoteState(var isCreate: Boolean = ND_CREATE, var isBin: Boolean = ND_BIN) {

    // TODO move into constructor?
    var isEdit = isCreate

    companion object {
        const val ND_CREATE = false
        const val ND_BIN = false
    }
}

// TODO ПЕРВЫЙ ШАГ:
// TODO сначала надо сделать liveData<NoteState> и сам этот стейт переделать в enum
// TODO с ключами: CREATE, EDIT, READ, BIN
// TODO и обновлять ui в соответствии с этим.