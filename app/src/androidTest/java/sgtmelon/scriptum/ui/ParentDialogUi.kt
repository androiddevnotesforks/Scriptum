package sgtmelon.scriptum.ui

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.basic.extension.waitAfter
import sgtmelon.scriptum.basic.extension.waitBefore

/**
 * Parent class for dialogs
 */
abstract class ParentDialogUi : ParentUi() {

    fun onCloseSoft() = waitClose {
        closeSoftKeyboard()
        pressBack()
    }

    protected fun waitClose(func: () -> Unit) = waitAfter(TIME_CLOSE) { func() }

    protected fun waitOpen(func: () -> Unit) = waitBefore(TIME_OPEN) { func() }

    private companion object {
        const val TIME_CLOSE = 300L
        const val TIME_OPEN = 100L
    }

}