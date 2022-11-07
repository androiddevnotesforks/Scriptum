package sgtmelon.scriptum.cleanup.ui

import sgtmelon.scriptum.cleanup.basic.extension.waitAfter
import sgtmelon.scriptum.cleanup.basic.extension.waitBefore
import sgtmelon.scriptum.parent.ui.feature.BackPress

/**
 * Interface need use in dialog classes
 */
interface IDialogUi : BackPress {

    fun onCloseSoft() = waitClose { pressBack() }

    fun waitClose(func: () -> Unit = {}) = waitAfter(CLOSE_TIME) { func() }

    fun waitOpen(func: () -> Unit) = waitBefore(OPEN_TIME) { func() }

    fun waitOperation(func: () -> Unit) = waitAfter(OPERATION_TIME) { func() }

    companion object {
        private const val CLOSE_TIME = 300L
        private const val OPEN_TIME = 100L
        private const val OPERATION_TIME = 500L
    }
}