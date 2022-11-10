package sgtmelon.scriptum.ui.auto.splash

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.parent.ui.launchAlarm
import sgtmelon.scriptum.parent.ui.launchBind
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Open screens with wrong intent data, check error handling.
 */
@RunWith(AndroidJUnit4::class)
class SplashWrongTest : ParentUiTest() {

    @Test fun bindTextNoteOpen() = launchBind(db.getInvalidNote(NoteType.TEXT)) {
        mainScreen { notesScreen(isEmpty = true) }
    }

    @Test fun bindRollNoteOpen() = launchBind(db.getInvalidNote(NoteType.ROLL)) {
        mainScreen { notesScreen(isEmpty = true) }
    }

    @Test fun alarmTextNoteOpen() = launchAlarm(db.getInvalidNote(NoteType.TEXT)) {
        mainScreen { notesScreen(isEmpty = true) }
    }

    @Test fun alarmRollNoteOpen() = launchAlarm(db.getInvalidNote(NoteType.ROLL)) {
        mainScreen { notesScreen(isEmpty = true) }
    }
}