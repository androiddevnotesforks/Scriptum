package sgtmelon.scriptum.ui

import sgtmelon.scriptum.basic.extension.waitAfter
import sgtmelon.scriptum.basic.extension.waitBefore

/**
 * Interface need use in dialog classes
 */
interface IDialogUi : IPressBack {

    fun onCloseSoft() = waitClose { onPressBack() }

    fun waitClose(func: () -> Unit = {}) = waitAfter(CLOSE_TIME) { func() }

    fun waitOpen(func: () -> Unit) = waitBefore(OPEN_TIME) { func() }

    fun waitOperation(func: () -> Unit) = waitAfter(OPERATION_TIME) { func() }

    companion object {
        private const val CLOSE_TIME = 300L
        private const val OPEN_TIME = 100L
        private const val OPERATION_TIME = 500L
    }
}