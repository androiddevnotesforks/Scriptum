package sgtmelon.scriptum.test.ui.auto.screen.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test text trim for [TextNoteFragment] and [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class NoteTrimTest : ParentUiTest() {

    @Test fun textNote() = data.createText().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createText(it) {
                            toolbar { onEnterName(nextString().plus(other = ".   as")) }
                            onEnterText(nextString())
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
                notesScreen(isEmpty = true) {
                    openAddDialog {
                        createRoll(it) {
                            toolbar { onEnterName(nextString().plus(other = ".   as")) }

                            val itemText = nextString().plus(other = ".   LL.   ")

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