package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.cleanup.test.parent.ParentUiTest

/**
 * Test add panel for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteAddTest : ParentUiTest() {

    @Test fun enterAddEmpty() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog { createRoll(it) { enterPanel { onAdd(text = "") } } }
                }
            }
        }
    }

    @Test fun addItems() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createRoll(it) {
                            repeat(times = 5) { enterPanel { onAdd(nextString()) } }
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