package sgtmelon.scriptum.ui.testing.screen.parent.features

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack

/**
 * Interface need use in screen classes which can be closed
 */
interface IPressBack {

    fun onPressBack() {
        closeSoftKeyboard()
        pressBack()
    }
}