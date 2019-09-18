package sgtmelon.scriptum.ui

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.dialog.RenameDialogUi
import sgtmelon.scriptum.waitAfter
import sgtmelon.scriptum.waitBefore

/**
 * Parent class for dialogs
 */
abstract class ParentDialogUi<T : BasicMatch> : ParentUi() {

    init {
        waitOpen { assert() }
    }

    /**
     * If need assertion with not default values - create another function
     * @sample - [RenameDialogUi]
     */
    abstract fun assert(): T

    fun onCloseSoft() = waitClose {
        closeSoftKeyboard()
        pressBack()
    }

    protected fun waitClose(func: () -> Unit) = waitAfter(TIME_CLOSE) { func() }

    private fun waitOpen(func: () -> Unit) = waitBefore(TIME_OPEN) { func() }

    private companion object {
        const val TIME_CLOSE = 300L
        const val TIME_OPEN = 100L
    }

}