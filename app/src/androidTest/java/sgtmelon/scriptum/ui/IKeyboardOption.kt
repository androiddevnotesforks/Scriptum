package sgtmelon.scriptum.ui

import androidx.test.espresso.action.ViewActions.pressImeActionButton

/**
 * Interface need use in screen classes which can use keyboard imeOptions.
 */
interface IKeyboardOption {

    fun onImeOptionClick()

}