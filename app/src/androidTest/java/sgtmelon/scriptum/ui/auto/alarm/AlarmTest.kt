package sgtmelon.scriptum.ui.auto.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.NoteOpenCase

/**
 * Test for [AlarmActivity].
 */
@RunWith(AndroidJUnit4::class)
class AlarmTest : ParentUiTest(),
    NoteOpenCase {

    // tODO check scroll list
    // TODO check long click

    @Test override fun itemTextOpen() = db.insertText().let {
        launchAlarm(it) {
            alarmScreen(it) { openText(it) { pressBack() } }
            mainScreen()
        }
    }

    @Test override fun itemRollOpen() = db.insertRoll().let {
        launchAlarm(it) {
            alarmScreen(it) { openRoll(it) { pressBack() } }
            mainScreen()
        }
    }

    @Test fun disable() = db.insertNote().let {
        launchAlarm(it) {
            alarmScreen(it) { disable() }
            mainScreen()
        }
    }
}