package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test enter focus for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNoteFocusTest : ParentUiTest() {

    @Test fun focusOnCreate() = db.createText().let {
        launch {
            mainScreen {
                openNotes(isEmpty = true) {
                    openAddDialog { createText(it) { toolbar { assertFocus() } } }
                }
            }
        }
    }

    @Test fun focusOnEdit() = db.insertText().let {
        launch {
            mainScreen {
                openNotes { openText(it) { controlPanel { onEdit() }.assertFocus() } }
            }
        }
    }

}