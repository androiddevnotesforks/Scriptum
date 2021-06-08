package sgtmelon.scriptum.test.ui.control.notification.count

import org.junit.Test
import sgtmelon.scriptum.test.parent.ParentNotificationTest

/**
 * Test for alarm notification count text.
 */
class DisplayTest : ParentNotificationTest() {

    // TODO fix all

    /**
     * Notify on start is implied
     */

    /**
     * Will cancel notification
     */
    @Test fun displayInfoZero() = startDisplayTest(count = 0)

    @Test fun displayInfoOne() = startDisplayTest(count = 1)

    @Test fun displayInfoTwo() = startDisplayTest(count = 2)

    @Test fun displayInfoFew() = startDisplayTest(count = 4)

    @Test fun displayInfoMany() = startDisplayTest(count = 7)

    private fun startDisplayTest(count: Int) {
        TODO()

        repeat(count) { data.insertNotification() }

        launch { onSee { mainScreen() } }
    }

}