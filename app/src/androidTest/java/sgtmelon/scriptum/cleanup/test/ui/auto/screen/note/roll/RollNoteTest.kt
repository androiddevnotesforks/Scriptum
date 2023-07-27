package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteFragment

import sgtmelon.scriptum.source.ui.tests.ParentUiTest

/**
 * Test content for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteTest : ParentUiTest() {

    @Test fun contentOnBinWithoutName() = db.insertRollToBin(db.rollNote.copy(name = "")).let {
        launchSplash { mainScreen { openBin { openRoll(it) } } }
    }

    @Test fun contentOnBinWithName() = db.insertRollToBin().let {
        launchSplash { mainScreen { openBin { openRoll(it) } } }
    }

    @Test fun contentOnCreate() = db.createRoll().let {
        launchSplash { mainScreen { openAddDialog { createRoll(it) } } }
    }

    @Test fun contentOnReadWithoutName() = db.insertRoll(db.rollNote.copy(name = "")).let {
        launchSplash { mainScreen { openNotes { openRoll(it) } } }
    }

    @Test fun contentOnReadWithName() = db.insertRoll().let {
        launchSplash { mainScreen { openNotes { openRoll(it) } } }
    }
}