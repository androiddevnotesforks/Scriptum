package sgtmelon.scriptum.ui.auto.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity

import sgtmelon.scriptum.parent.ui.tests.ParentUiRotationTest

/**
 * Test of [AlarmActivity] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class AlarmRotationTest : ParentUiRotationTest() {

    @Test fun contentText() = startAlarmTest(db.insertText()) {
        rotate.toSide()
        assert()
        assertItem(it)
    }

    @Test fun contentRoll() = startAlarmTest(db.insertRoll()) {
        rotate.toSide()
        assert()
        assertItem(it)
    }
}