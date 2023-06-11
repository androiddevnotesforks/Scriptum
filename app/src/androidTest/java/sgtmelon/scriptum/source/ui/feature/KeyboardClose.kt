package sgtmelon.scriptum.source.ui.feature

import androidx.test.espresso.Espresso

/**
 * Interface need use in screen classes which need function for hide keyboard.
 */
interface KeyboardClose {

    fun closeKeyboard() = Espresso.closeSoftKeyboard()
}