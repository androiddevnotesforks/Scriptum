package sgtmelon.scriptum.ui.auto.splash

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.splash.SplashActivity
import sgtmelon.scriptum.ui.testing.parent.ParentUiTest
import sgtmelon.scriptum.ui.testing.parent.launch
import sgtmelon.scriptum.ui.testing.parent.launchAlarm
import sgtmelon.scriptum.ui.testing.parent.launchBind
import sgtmelon.scriptum.ui.testing.parent.launchHelpDisappear
import sgtmelon.scriptum.ui.testing.parent.launchNewNote
import sgtmelon.scriptum.ui.testing.parent.launchNotifications

/**
 * Test for [SplashActivity]
 */
@RunWith(AndroidJUnit4::class)
class SplashTest : ParentUiTest() {

    @Test fun mainScreenOpen() = launch { mainScreen() }

    @Test fun alarmTextNoteOpen() = db.insertText().let { launchAlarm(it) { alarmScreen(it) } }

    @Test fun alarmRollNoteOpen() = db.insertRoll().let { launchAlarm(it) { alarmScreen(it) } }

    @Test fun bindTextNoteOpen() = db.insertText().let {
        launchBind(it) { bindNoteScreen(it) { onPressBack() }.mainScreen() }
    }

    @Test fun bindRollNoteOpen() = db.insertRoll().let {
        launchBind(it) { bindNoteScreen(it) { onPressBack() }.mainScreen() }
    }

    @Test fun notificationsOpen() = launchNotifications {
        notificationsScreen(isEmpty = true) { onPressBack() }
        mainScreen()
    }

    @Test fun helpDisappearOpen() = launchHelpDisappear {
        helpDisappearScreen { TODO() }
    }

    @Test fun createTextOpen() = launchNewNote(NoteType.TEXT) {
        createNoteScreen(db.createText()) { onPressBack() }
        mainScreen()
    }

    @Test fun createRollOpen() = launchNewNote(NoteType.ROLL) {
        createNoteScreen(db.createRoll()) { onPressBack() }
        mainScreen()
    }
}