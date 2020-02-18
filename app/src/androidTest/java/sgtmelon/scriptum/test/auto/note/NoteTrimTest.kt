package sgtmelon.scriptum.test.auto.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test text trim for [TextNoteFragment] and [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class NoteTrimTest : ParentUiTest() {

    @Test fun textNote() = data.createText().let {
        launch {
            mainScreen {
                notesScreen(empty = true) {
                    openAddDialog {
                        createText(it) {
                            toolbar { onEnterName(data.uniqueString.plus(other = ".   as")) }
                            onEnterText(data.uniqueString)
                            controlPanel { onSave() }
                        }
                    }
                }
            }
        }
    }

    @Test fun rollNote() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(empty = true) {
                    openAddDialog {
                        createRoll(it) {
                            toolbar { onEnterName(data.uniqueString.plus(other = ".   as")) }

                            val itemText = data.uniqueString.plus(other = ".   ll.   ")

                            enterPanel { onAdd(itemText) }
                            onAssertAll()
                            onEnterText(itemText)

                            controlPanel { onSave() }
                            onAssertAll()
                        }
                    }
                }
            }
        }
    }

}