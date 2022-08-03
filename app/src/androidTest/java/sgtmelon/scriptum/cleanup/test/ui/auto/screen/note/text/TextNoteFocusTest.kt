package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.cleanup.test.parent.ParentUiTest

/**
 * Test enter focus for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNoteFocusTest : ParentUiTest() {

    @Test fun focusOnCreate() = data.createText().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog { createText(it) { toolbar { assertFocus() } } }
                }
            }
        }
    }

    @Test fun focusOnEdit() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen { openTextNote(it) { controlPanel { onEdit() }.assertFocus() } }
            }
        }
    }

}