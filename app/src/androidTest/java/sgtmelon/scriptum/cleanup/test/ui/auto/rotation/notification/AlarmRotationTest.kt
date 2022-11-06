package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.notification

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.ui.testing.parent.ParentUiRotationTest
import sgtmelon.scriptum.ui.testing.parent.launchAlarm

/**
 * Test of [AlarmActivity] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class AlarmRotationTest : ParentUiRotationTest() {

    @Test fun contentText() = db.insertText().let {
        launchAlarm(it) {
            alarmScreen(it) {
                rotate.toSide()
                assert()
                onAssertItem(it)
            }
        }
    }

    @Test fun contentRoll() = db.insertRoll().let {
        launchAlarm(it) {
            alarmScreen(it) {
                rotate.toSide()
                assert()
                onAssertItem(it)
            }
        }
    }
}