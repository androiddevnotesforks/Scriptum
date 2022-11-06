package sgtmelon.scriptum.cleanup.test.ui.auto.screen.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.ui.testing.parent.ParentUiTest
import sgtmelon.scriptum.ui.testing.parent.launchAlarm

/**
 * Test for [AlarmActivity].
 */
@RunWith(AndroidJUnit4::class)
class AlarmTest : ParentUiTest() {

    @Test fun openTextNote() = db.insertText().let {
        launchAlarm(it) { alarmScreen(it) { openTextNote { onPressBack() } }.mainScreen() }
    }

    @Test fun openRollNote() = db.insertRoll().let {
        launchAlarm(it) { alarmScreen(it) { openRollNote { onPressBack() } }.mainScreen() }
    }

    @Test fun clickDisable() = db.insertNote().let {
        launchAlarm(it) { alarmScreen(it) { onClickDisable() }.mainScreen() }
    }
}