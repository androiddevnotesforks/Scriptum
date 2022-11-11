package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test content for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNoteTest : ParentUiTest() {

    @Test fun contentOnBinWithoutName() = db.insertTextToBin(db.textNote.copy(name = "")).let {
        launch { mainScreen { openBin { openText(it) } } }
    }

    @Test fun contentOnBinWithName() = db.insertTextToBin().let {
        launch { mainScreen { openBin { openText(it) } } }
    }


    @Test fun contentOnCreate() = db.createText().let {
        launch { mainScreen { openAddDialog { createText(it) } } }
    }


    @Test fun contentOnReadWithoutName() = db.insertText(db.textNote.copy(name = "")).let {
        launch { mainScreen { openNotes { openText(it) } } }
    }

    @Test fun contentOnReadWithName() = db.insertText().let {
        launch { mainScreen { openNotes { openText(it) } } }
    }

}