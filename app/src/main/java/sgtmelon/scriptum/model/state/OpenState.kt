package sgtmelon.scriptum.model.state

/**
 * Состояние для диалогов, сохраняющее открыт ли он
 */
class OpenState {

    var value: Boolean = false

    inline fun tryInvoke(func: () -> Unit) {
        if (!value) {
            value = true
            func()
        }
    }

    fun clear() {
        value = false
    }

    companion object {
        const val KEY = "INTENT_STATE_OPEN"
    }

}