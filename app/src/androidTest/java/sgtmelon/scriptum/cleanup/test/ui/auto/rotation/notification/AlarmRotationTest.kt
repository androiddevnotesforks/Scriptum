package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.notification

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.parent.ParentUiRotationTest

/**
 * Test of [AlarmActivity] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class AlarmRotationTest : ParentUiRotationTest() {

    @Test fun contentText() = db.insertText().let {
        launchAlarm(it) {
            openAlarm(it) {
                automator.rotateSide()
                assert()
                onAssertItem(it)
            }
        }
    }

    @Test fun contentRoll() = db.insertRoll().let {
        launchAlarm(it) {
            openAlarm(it) {
                automator.rotateSide()
                assert()
                onAssertItem(it)
            }
        }
    }
}