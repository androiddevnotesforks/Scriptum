package sgtmelon.scriptum.source.ui.feature

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.closeSoftKeyboard

/**
 * Interface need use in screen classes which can be closed by back press.
 */
interface BackPress {

    fun pressBack() {
        closeSoftKeyboard()
        Espresso.pressBack()
    }
}