package sgtmelon.scriptum.cleanup.test.ui.auto.screen.other

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.parent.ParentUiTest

/**
 * Test for [SplashActivity]
 */
@RunWith(AndroidJUnit4::class)
class SplashTest : ParentUiTest() {

    @Test fun introScreenOpen() = launch({ preferences.isFirstStart = true }) { introScreen() }

    @Test fun mainScreenOpen() = launch { mainScreen() }

    @Test fun alarmTextNoteOpen() = db.insertText().let { launchAlarm(it) { openAlarm(it) } }

    @Test fun alarmRollNoteOpen() = db.insertRoll().let { launchAlarm(it) { openAlarm(it) } }

    @Test fun bindTextNoteOpen() = db.insertText().let {
        launchBind(it) { openTextNoteBind(it) { onPressBack() }.mainScreen() }
    }

    @Test fun bindRollNoteOpen() = db.insertRoll().let {
        launchBind(it) { openRollNoteBind(it) { onPressBack() }.mainScreen() }
    }

    @Test fun notificationsOpen() = launchNotifications {
        openNotification(isEmpty = true) { onPressBack() }
        mainScreen()
    }

    // TODO finish test
    @Test fun helpDisappearOpen() = launchHelpDisappear { TODO() }

    @Test fun createTextOpen() {
        TODO()
    }

    @Test fun createRollOpen() {
        TODO()
    }
}