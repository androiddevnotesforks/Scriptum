package sgtmelon.scriptum.cleanup.test.ui.auto.screen

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.splash.SplashActivity
import sgtmelon.scriptum.parent.ui.ParentUiTest
import sgtmelon.scriptum.parent.ui.launch
import sgtmelon.scriptum.parent.ui.launchAlarm
import sgtmelon.scriptum.parent.ui.launchBind
import sgtmelon.scriptum.parent.ui.launchHelpDisappear
import sgtmelon.scriptum.parent.ui.launchNewNote
import sgtmelon.scriptum.parent.ui.launchNotifications

/**
 * Test for [SplashActivity]
 */
@RunWith(AndroidJUnit4::class)
class SplashTest : ParentUiTest() {

    @Test fun mainScreenOpen() = launch { mainScreen() }

    @Test fun alarmTextNoteOpen() = db.insertText().let { launchAlarm(it) { alarmScreen(it) } }

    @Test fun alarmRollNoteOpen() = db.insertRoll().let { launchAlarm(it) { alarmScreen(it) } }

    @Test fun bindTextNoteOpen() = db.insertText().let {
        launchBind(it) { bindNoteScreen(it) { pressBack() }.mainScreen() }
    }

    @Test fun bindRollNoteOpen() = db.insertRoll().let {
        launchBind(it) { bindNoteScreen(it) { pressBack() }.mainScreen() }
    }

    @Test fun notificationsOpen() = launchNotifications {
        notificationsScreen { pressBack() }
        mainScreen()
    }

    @Test fun helpDisappearOpen() = launchHelpDisappear {
        helpDisappearScreen { TODO() }
    }

    @Test fun createTextOpen() = launchNewNote(NoteType.TEXT) {
        createNoteScreen(db.createText()) { pressBack() }
        mainScreen()
    }

    @Test fun createRollOpen() = launchNewNote(NoteType.ROLL) {
        createNoteScreen(db.createRoll()) { pressBack() }
        mainScreen()
    }
}