package sgtmelon.scriptum.cleanup.test.ui.auto.screen.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.parent.ParentUiTest

/**
 * Test for [AlarmActivity].
 */
@RunWith(AndroidJUnit4::class)
class AlarmTest : ParentUiTest() {

    @Test fun openTextNote() = db.insertText().let {
        launchAlarm(it) { openAlarm(it) { openTextNote { onPressBack() } }.mainScreen() }
    }

    @Test fun openRollNote() = db.insertRoll().let {
        launchAlarm(it) { openAlarm(it) { openRollNote { onPressBack() } }.mainScreen() }
    }

    @Test fun clickDisable() = db.insertNote().let {
        launchAlarm(it) { openAlarm(it) { onClickDisable() }.mainScreen() }
    }
}