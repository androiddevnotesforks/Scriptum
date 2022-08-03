package sgtmelon.scriptum.cleanup.ui

/**
 * Interface need use in screen classes which can use keyboard imeOptions.
 */
interface IKeyboardOption {

    fun onImeOptionClick(isSuccess: Boolean = true)
}