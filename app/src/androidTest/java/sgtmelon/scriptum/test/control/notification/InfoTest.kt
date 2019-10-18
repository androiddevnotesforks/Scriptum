package sgtmelon.scriptum.test.control.notification

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.test.ParentNotificationTest

/**
 * Test of info about notification in status bar
 */
@RunWith(AndroidJUnit4::class)
class InfoTest : ParentNotificationTest() {

    /**
     * Will cancel notification
     */
    @Test fun displayInfoZero() = startDisplayTest(count = 0)

    @Test fun displayInfoOne() = startDisplayTest(count = 1)

    @Test fun displayInfoTwo() = startDisplayTest(count = 2)

    @Test fun displayInfoFew() = startDisplayTest(count = 4)

    @Test fun displayInfoMany() = startDisplayTest(count = 7)

    private fun startDisplayTest(count: Int) {
        iBindControl.notifyInfo(count)

        launch { onSee { mainScreen() } }
    }

}