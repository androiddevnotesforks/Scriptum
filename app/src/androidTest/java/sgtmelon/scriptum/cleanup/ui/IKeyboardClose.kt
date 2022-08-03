package sgtmelon.scriptum.cleanup.ui

import androidx.test.espresso.Espresso

/**
 * Interface need use in screen classes which need function for hide keyboard.
 */
interface IKeyboardClose {

    fun closeKeyboard() = Espresso.closeSoftKeyboard()
}