package sgtmelon.scriptum.ui

import sgtmelon.scriptum.basic.extension.waitAfter
import sgtmelon.scriptum.basic.extension.waitBefore

/**
 * Interface need use in dialog classes
 */
interface IDialogUi : IPressBack {

    fun onCloseSoft() = waitClose { onPressBack() }

    fun waitClose(func: () -> Unit = {}) = waitAfter(TIME_CLOSE) { func() }

    fun waitOpen(func: () -> Unit) = waitBefore(TIME_OPEN) { func() }

    fun waitOperation(func: () -> Unit) = waitAfter(TIME_OPERATION) { func() }

    private companion object {
        const val TIME_CLOSE = 300L
        const val TIME_OPEN = 100L
        const val TIME_OPERATION = 500L
    }

}