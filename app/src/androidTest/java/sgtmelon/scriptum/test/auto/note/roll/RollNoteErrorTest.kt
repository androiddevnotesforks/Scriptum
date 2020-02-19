package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test fix of old errors for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteErrorTest : ParentUiTest() {

    @Test fun removeEmptyItemsAfterChangeDone() {
        var item = data.createRoll()

        launch {
            mainScreen {
                notesScreen(empty = true) {
                    openAddDialog {
                        createRoll(item) {
                            enterPanel { repeat(times = 4) { onAdd(data.uniqueString) } }
                            onEnterText()

                            controlPanel { onSave() }
                            onAssertAll()

                            item = noteItem
                            onPressBack()
                        }
                    }

                    onAssertItem(item)
                }
            }
        }
    }

}