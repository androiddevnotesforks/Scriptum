package sgtmelon.scriptum.office.state

/**
 * Состояние для диалогов, сохраняющее открыт ли он
 */
class OpenState {

    var isOpen: Boolean = false

    fun tryInvoke(function: () -> Unit) {
        if (!isOpen) {
            isOpen = true
            function.invoke()
        }
    }

    fun clear() {
        isOpen = false
    }

}