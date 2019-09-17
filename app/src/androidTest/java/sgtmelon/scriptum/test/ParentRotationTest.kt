package sgtmelon.scriptum.test

import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.waitBefore

/**
 * Parent class for Rotation tests
 *
 * For rotate screen use: ctrl + left/rightArrow
 */
abstract class ParentRotationTest : ParentUiTest() {

    protected fun onRotate(beforeFunc: () -> Unit, afterFunc: () -> Unit) {
        beforeFunc()
        onRotate(afterFunc)
    }

    protected fun onRotate(afterFunc: () -> Unit) {
        testRule.activity?.runOnUiThread { context.showToast(TOAST_TEXT) }
        waitBefore(TIME) { afterFunc() }
    }

    private companion object {
        const val TIME = 5000L
        const val TOAST_TEXT = "ROTATE NOW!!!"
    }

}