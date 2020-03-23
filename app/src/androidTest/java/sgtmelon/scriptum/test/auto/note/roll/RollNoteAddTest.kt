package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test add panel for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteAddTest : ParentUiTest() {

    @Test fun enterAddEmpty() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(empty = true) {
                    openAddDialog { createRoll(it) { enterPanel { onAdd(text = "") } } }
                }
            }
        }
    }

    @Test fun addItems() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(empty = true) {
                    openAddDialog {
                        createRoll(it) {
                            repeat(times = 5) { enterPanel { onAdd(data.uniqueString) } }
                            onAssertAll()

                            controlPanel { onSave() }
                            onAssertAll()
                        }
                    }
                }
            }
        }
    }

}