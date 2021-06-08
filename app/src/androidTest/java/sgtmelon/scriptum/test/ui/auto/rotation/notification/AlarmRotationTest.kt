package sgtmelon.scriptum.test.ui.auto.rotation.notification

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.test.parent.ParentRotationTest

/**
 * Test of [AlarmActivity] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class AlarmRotationTest : ParentRotationTest() {

    @Test fun contentText() = data.insertText().let {
        launchAlarm(it) {
            openAlarm(it) {
                automator.rotateSide()
                assert()
                onAssertItem(it)
            }
        }
    }

    @Test fun contentRoll() = data.insertRoll().let {
        launchAlarm(it) {
            openAlarm(it) {
                automator.rotateSide()
                assert()
                onAssertItem(it)
            }
        }
    }
}