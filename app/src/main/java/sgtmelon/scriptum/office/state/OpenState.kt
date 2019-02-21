package sgtmelon.scriptum.office.state

/**
 * Состояние для диалогов, сохраняющее открыт ли он
 */
class OpenState {

    var value: Boolean = false

    inline fun tryInvoke(function: () -> Unit) {
        if (!value) {
            value = true
            function.invoke()
        }
    }

    fun clear() {
        value = false
    }

    companion object {
        const val KEY = "INTENT_STATE_OPEN"
    }

}