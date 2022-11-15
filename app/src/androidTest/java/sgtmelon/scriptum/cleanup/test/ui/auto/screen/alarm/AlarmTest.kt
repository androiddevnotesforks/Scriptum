package sgtmelon.scriptum.cleanup.test.ui.auto.screen.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test for [AlarmActivity].
 */
@RunWith(AndroidJUnit4::class)
class AlarmTest : ParentUiTest() {

    @Test fun openTextNote() = db.insertText().let {
        launchAlarm(it) { alarmScreen(it) { openTextNote { pressBack() } }.mainScreen() }
    }

    @Test fun openRollNote() = db.insertRoll().let {
        launchAlarm(it) { alarmScreen(it) { openRollNote { pressBack() } }.mainScreen() }
    }

    @Test fun clickDisable() = db.insertNote().let {
        launchAlarm(it) { alarmScreen(it) { disable() }.mainScreen() }
    }
}