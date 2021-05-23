package sgtmelon.scriptum.test.auto.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test content for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNoteTest : ParentUiTest() {

    @Test fun contentOnBinWithoutName() = data.insertTextToBin(data.textNote.copy(name = "")).let {
        launch { mainScreen { binScreen { openTextNote(it) } } }
    }

    @Test fun contentOnBinWithName() = data.insertTextToBin().let {
        launch { mainScreen { binScreen { openTextNote(it) } } }
    }


    @Test fun contentOnCreate() = data.createText().let {
        launch { mainScreen { openAddDialog { createText(it) } } }
    }


    @Test fun contentOnReadWithoutName() = data.insertText(data.textNote.copy(name = "")).let {
        launch { mainScreen { notesScreen { openTextNote(it) } } }
    }

    @Test fun contentOnReadWithName() = data.insertText().let {
        launch { mainScreen { notesScreen { openTextNote(it) } } }
    }

}