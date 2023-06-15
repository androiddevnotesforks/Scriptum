package sgtmelon.scriptum.tests.ui.auto.splash

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.splash.SplashActivity
import sgtmelon.scriptum.source.ui.tests.ParentUiTest

/**
 * Open screens via [SplashActivity] with wrong intent data, check error handling.
 */
@RunWith(AndroidJUnit4::class)
class SplashWrongTest : ParentUiTest() {

    @Test fun alarmTextNote() = launchSplashAlarm(db.getInvalidNote(NoteType.TEXT)) {
        mainScreen { openNotes(isEmpty = true) }
    }

    @Test fun alarmRollNote() = launchSplashAlarm(db.getInvalidNote(NoteType.ROLL)) {
        mainScreen { openNotes(isEmpty = true) }
    }

    @Test fun bindTextNote() = launchSplashBind(db.getInvalidNote(NoteType.TEXT)) {
        mainScreen { openNotes(isEmpty = true) }
    }

    @Test fun bindRollNote() = launchSplashBind(db.getInvalidNote(NoteType.ROLL)) {
        mainScreen { openNotes(isEmpty = true) }
    }
}