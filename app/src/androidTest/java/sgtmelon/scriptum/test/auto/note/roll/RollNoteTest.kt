package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test content for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteTest : ParentUiTest() {

    @Test fun contentOnBinWithoutName() = data.insertRollToBin(data.rollNote.copy(name = "")).let {
        launch { mainScreen { binScreen { openRollNote(it) } } }
    }

    @Test fun contentOnBinWithName() = data.insertRollToBin().let {
        launch { mainScreen { binScreen { openRollNote(it) } } }
    }

    @Test fun contentOnCreate() = data.createRoll().let {
        launch { mainScreen { openAddDialog { createRoll(it) } } }
    }

    @Test fun contentOnReadWithoutName() = data.insertRoll(data.rollNote.copy(name = "")).let {
        launch { mainScreen { notesScreen { openRollNote(it) } } }
    }

    @Test fun contentOnReadWithName() = data.insertRoll().let {
        launch { mainScreen { notesScreen { openRollNote(it) } } }
    }

}