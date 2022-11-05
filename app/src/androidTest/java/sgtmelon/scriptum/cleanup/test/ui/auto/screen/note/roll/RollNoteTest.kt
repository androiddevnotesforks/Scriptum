package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.ui.testing.parent.ParentUiTest
import sgtmelon.scriptum.ui.testing.parent.launch

/**
 * Test content for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteTest : ParentUiTest() {

    @Test fun contentOnBinWithoutName() = db.insertRollToBin(db.rollNote.copy(name = "")).let {
        launch { mainScreen { binScreen { openRollNote(it) } } }
    }

    @Test fun contentOnBinWithName() = db.insertRollToBin().let {
        launch { mainScreen { binScreen { openRollNote(it) } } }
    }

    @Test fun contentOnCreate() = db.createRoll().let {
        launch { mainScreen { openAddDialog { createRoll(it) } } }
    }

    @Test fun contentOnReadWithoutName() = db.insertRoll(db.rollNote.copy(name = "")).let {
        launch { mainScreen { notesScreen { openRollNote(it) } } }
    }

    @Test fun contentOnReadWithName() = db.insertRoll().let {
        launch { mainScreen { notesScreen { openRollNote(it) } } }
    }

}