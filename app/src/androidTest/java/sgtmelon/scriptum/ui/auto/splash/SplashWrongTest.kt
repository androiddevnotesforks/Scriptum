package sgtmelon.scriptum.ui.auto.splash

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Open screens with wrong intent data, check error handling.
 */
@RunWith(AndroidJUnit4::class)
class SplashWrongTest : ParentUiTest() {

    @Test fun bindTextNoteOpen() = launchSplashBind(db.getInvalidNote(NoteType.TEXT)) {
        mainScreen { openNotes(isEmpty = true) }
    }

    @Test fun bindRollNoteOpen() = launchSplashBind(db.getInvalidNote(NoteType.ROLL)) {
        mainScreen { openNotes(isEmpty = true) }
    }

    @Test fun alarmTextNoteOpen() = launchSplashAlarm(db.getInvalidNote(NoteType.TEXT)) {
        mainScreen { openNotes(isEmpty = true) }
    }

    @Test fun alarmRollNoteOpen() = launchSplashAlarm(db.getInvalidNote(NoteType.ROLL)) {
        mainScreen { openNotes(isEmpty = true) }
    }
}