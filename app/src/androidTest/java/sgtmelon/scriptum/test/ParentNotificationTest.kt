package sgtmelon.scriptum.test

import sgtmelon.scriptum.basic.extension.waitBefore
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.control.bind.IBindControl
import sgtmelon.scriptum.extension.showToast

/**
 * Parent class for tests with bindings notifications in status bar
 */
abstract class ParentNotificationTest : ParentUiTest() {

    protected val bindControl: IBindControl = BindControl(context)

    protected fun onSee(afterFunc: () -> Unit = {}) {
        testRule.activity?.runOnUiThread { context.showToast(SEE_TOAST) }
        waitBefore(SEE_TIME) { afterFunc() }
    }

    protected fun onOpen(afterFunc: () -> Unit = {}) {
        testRule.activity?.runOnUiThread { context.showToast(OPEN_TOAST) }
        waitBefore(OPEN_TIME) { afterFunc() }
    }

    private companion object {
        const val SEE_TIME = 3000L
        const val SEE_TOAST = "SEE NOTIFICATION!!!"

        const val OPEN_TIME = 7000L
        const val OPEN_TOAST = "OPEN NOTIFICATION!!!"
    }

}