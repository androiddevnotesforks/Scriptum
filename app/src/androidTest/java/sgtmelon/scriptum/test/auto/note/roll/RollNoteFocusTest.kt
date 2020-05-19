package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.nextString
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest
import kotlin.random.Random

/**
 * Test enter focus for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteFocusTest : ParentUiTest() {

    @Test fun focusOnCreate() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(empty = true) {
                    openAddDialog { createRoll(it) { toolbar { assertFocus() } } }
                }
            }
        }
    }

    @Test fun focusOnEdit() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onEdit() }
                        enterPanel { onEnterText(Random.nextString()) }

                        controlPanel { onSave().onEdit() }
                        enterPanel { assertFocus() }
                    }
                }
            }
        }
    }

}