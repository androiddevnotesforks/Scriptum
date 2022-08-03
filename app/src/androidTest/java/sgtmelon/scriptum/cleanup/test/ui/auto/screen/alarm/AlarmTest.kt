package sgtmelon.scriptum.cleanup.test.ui.auto.screen.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.cleanup.test.parent.ParentUiTest

/**
 * Test for [AlarmActivity].
 */
@RunWith(AndroidJUnit4::class)
class AlarmTest : ParentUiTest() {

    @Test fun openTextNote() = data.insertText().let {
        launchAlarm(it) { openAlarm(it) { openTextNote { onPressBack() } }.mainScreen() }
    }

    @Test fun openRollNote() = data.insertRoll().let {
        launchAlarm(it) { openAlarm(it) { openRollNote { onPressBack() } }.mainScreen() }
    }

    @Test fun clickDisable() = data.insertNote().let {
        launchAlarm(it) { openAlarm(it) { onClickDisable() }.mainScreen() }
    }
}