package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.parent.ParentUiTest

/**
 * Test keyboard ime click for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNoteImeTest : ParentUiTest() {

    @Test fun toolbarImeNext() = db.createText().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createText(it) {
                            onEnterText(nextString())
                            toolbar { onEnterName(nextString()).onImeOptionName() }
                        }
                    }
                }
            }
        }
    }

    /**
     * TODO improve ime option test
     */
    @Test fun enterImeAction() = db.createText().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createText(it) {
                            onEnterText(nextString()).onImeOptionText()
                        }
                    }
                }
            }
        }
    }

}