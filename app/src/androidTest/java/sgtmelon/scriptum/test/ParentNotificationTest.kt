package sgtmelon.scriptum.test

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import sgtmelon.scriptum.basic.extension.waitBefore
import sgtmelon.scriptum.basic.notifications.NotificationAutomator
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.control.system.callback.IBindControl

/**
 * Parent class for tests with bindings notifications in status bar
 */
abstract class ParentNotificationTest : ParentUiTest() {

    protected val bindControl: IBindControl = BindControl(context)

    protected var automator: NotificationAutomator? = null

    override fun setUp() {
        super.setUp()

        automator = NotificationAutomator(context, getInstrumentation())
    }

    override fun tearDown() {
        super.tearDown()

        automator = null
    }

    // TODO remove
    protected fun onSee(afterFunc: () -> Unit = {}) {
        testRule.activity?.runOnUiThread { context.showToast(SEE_TOAST) }
        waitBefore(SEE_TIME) { afterFunc() }
    }

    protected fun onOpen(afterFunc: () -> Unit = {}) {
        testRule.activity?.runOnUiThread { context.showToast(OPEN_TOAST) }
        waitBefore(OPEN_TIME) { afterFunc() }
    }

    companion object {
        private const val SEE_TIME = 3000L
        private const val SEE_TOAST = "SEE NOTIFICATION!!!"

        private const val OPEN_TIME = 7000L
        private const val OPEN_TOAST = "OPEN NOTIFICATION!!!"
    }
}