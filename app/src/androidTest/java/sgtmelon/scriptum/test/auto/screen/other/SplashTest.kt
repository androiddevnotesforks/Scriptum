package sgtmelon.scriptum.test.auto.screen.other

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test for [SplashActivity]
 */
@RunWith(AndroidJUnit4::class)
class SplashTest : ParentUiTest() {

    @Test fun introScreenOpen() = launch({ preferenceRepo.firstStart = true }) { introScreen() }

    @Test fun mainScreenOpen() = launch { mainScreen() }

    @Test fun alarmTextNoteOpen() = data.insertText().let { launchAlarm(it) { openAlarm(it) } }

    @Test fun alarmRollNoteOpen() = data.insertRoll().let { launchAlarm(it) { openAlarm(it) } }

    @Test fun bindTextNoteOpen() = data.insertText().let {
        launchBind(it) { openTextNoteBind(it) { onPressBack() }.mainScreen() }
    }

    @Test fun bindRollNoteOpen() = data.insertRoll().let {
        launchBind(it) { openRollNoteBind(it) { onPressBack() }.mainScreen() }
    }

    @Test fun notificationsOpen() = launchNotifications {
        openNotification(isEmpty = true) { onPressBack() }
        mainScreen()
    }

    // TODO finish test
    @Test fun helpDisappearOpen() = launchHelpDisappear { TODO() }
}