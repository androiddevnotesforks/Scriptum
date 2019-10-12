package sgtmelon.scriptum.ui

import androidx.test.espresso.Espresso.pressBackUnconditionally

/**
 * Interface need use in screen classes which can close application
 */
interface IPressClose {

    fun onPressClose() = pressBackUnconditionally()

}