package sgtmelon.scriptum.cleanup.test.parent

import sgtmelon.scriptum.cleanup.basic.automator.NotificationAutomator
import sgtmelon.scriptum.source.ui.tests.ParentUiTest

/**
 * Parent class for tests with bindings notifications in status bar
 */
abstract class ParentNotificationTest : ParentUiTest() {

    protected val automator by lazy { NotificationAutomator(context, uiDevice) }

    // TODO remove
    protected fun onSee(afterFunc: () -> Unit = {}) {
        TODO("remove it")

        //        testRule.activity?.runOnUiThread { context.showToast(SEE_TOAST) }
        //        waitBefore(SEE_TIME) { afterFunc() }
    }

    protected fun onOpen(afterFunc: () -> Unit = {}) {
        TODO("remove it")

        //        testRule.activity?.runOnUiThread { context.showToast(OPEN_TOAST) }
        //        waitBefore(OPEN_TIME) { afterFunc() }
    }
}