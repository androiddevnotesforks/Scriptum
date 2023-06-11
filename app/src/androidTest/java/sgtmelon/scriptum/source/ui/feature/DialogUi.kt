package sgtmelon.scriptum.source.ui.feature

import sgtmelon.test.cappuccino.utils.await

/**
 * Interface need use in dialog classes
 */
interface DialogUi : BackPress {

    fun softClose() = waitClose { pressBack() }

    fun waitClose(func: () -> Unit = {}) {
        func()
        await(CLOSE_TIME)
    }

    fun waitOpen(func: () -> Unit) {
        await(OPEN_TIME)
        func()
    }

    fun waitOperation(func: () -> Unit) {
        func()
        await(OPERATION_TIME)
    }

    companion object {
        private const val CLOSE_TIME = 300L
        private const val OPEN_TIME = 100L
        private const val OPERATION_TIME = 500L
    }
}