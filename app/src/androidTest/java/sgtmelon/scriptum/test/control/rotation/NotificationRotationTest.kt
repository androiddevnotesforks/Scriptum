package sgtmelon.scriptum.test.control.rotation

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.test.ParentRotationTest

/**
 * Test of application work with phone rotation
 */
@RunWith(AndroidJUnit4::class)
class NotificationRotationTest : ParentRotationTest() {

    @Test fun notificationContentEmpty() = launch {
        mainScreen {
            openNotesPage(empty = true) {
                openNotification(empty = true) { onRotate { assert(empty = true) } }
            }
        }
    }

    @Test fun notificationContentList() = launch({ data.fillNotification() }) {
        mainScreen { openNotesPage { openNotification { onRotate { assert(empty = false) } } } }
    }

    @Test fun alarmContent() = data.insertText().let {
        launchAlarm(it) { openAlarm(it) { onRotate { assert() } } }
    }

}