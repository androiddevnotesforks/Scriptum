package sgtmelon.scriptum.test.auto.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test keyboard ime click for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNoteImeTest : ParentUiTest() {

    @Test fun toolbarImeNext() = data.createText().let {
        launch {
            mainScreen {
                notesScreen(empty = true) {
                    openAddDialog {
                        createText(it) {
                            onEnterText(data.uniqueString)
                            toolbar { onEnterName(data.uniqueString).onImeOptionName() }
                        }
                    }
                }
            }
        }
    }

    /**
     * TODO improve ime option test
     */
    @Test fun enterImeAction() = data.createText().let {
        launch {
            mainScreen {
                notesScreen(empty = true) {
                    openAddDialog {
                        createText(it) {
                            onEnterText(data.uniqueString).onImeOptionText()
                        }
                    }
                }
            }
        }
    }

}