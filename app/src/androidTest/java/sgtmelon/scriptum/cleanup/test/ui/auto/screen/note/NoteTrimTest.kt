package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test text trim for [TextNoteFragment] and [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class NoteTrimTest : ParentUiTest() {

    @Test fun textNote() = db.createText().let {
        launch {
            mainScreen {
                openNotes(isEmpty = true) {
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

    @Test fun rollNote() = db.createRoll().let {
        launch {
            mainScreen {
                openNotes(isEmpty = true) {
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