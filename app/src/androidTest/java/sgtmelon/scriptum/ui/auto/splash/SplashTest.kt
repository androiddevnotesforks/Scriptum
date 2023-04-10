package sgtmelon.scriptum.ui.auto.splash

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.splash.SplashActivity
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test for [SplashActivity].
 */
@RunWith(AndroidJUnit4::class)
class SplashTest : ParentUiTest() {

    @Test fun mainScreenOpen() = launchSplash { mainScreen() }

    @Test fun alarmTextNoteOpen() = db.insertText().let {
        launchSplashAlarm(it) { alarmScreen(it) }
    }

    @Test fun alarmRollNoteOpen() = db.insertRoll().let {
        launchSplashAlarm(it) { alarmScreen(it) }
    }

    @Test fun bindTextNoteOpen() = db.insertText().let {
        launchSplashBind(it) { bindNoteScreen(it) { pressBack() }.mainScreen() }
    }

    @Test fun bindRollNoteOpen() = db.insertRoll().let {
        launchSplashBind(it) { bindNoteScreen(it) { pressBack() }.mainScreen() }
    }

    @Test fun notificationsOpen() = launchSplashNotifications {
        notificationsScreen { pressBack() }
        mainScreen()
    }

    @Test fun createTextOpen() = launchSplashNewNote(NoteType.TEXT) {
        createNoteScreen(db.createText()) { pressBack() }
        mainScreen()
    }

    @Test fun createRollOpen() = launchSplashNewNote(NoteType.ROLL) {
        createNoteScreen(db.createRoll()) { pressBack() }
        mainScreen()
    }
}