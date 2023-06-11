package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteFragmentImpl
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteFragmentImpl

import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test text trim for [TextNoteFragmentImpl] and [RollNoteFragmentImpl].
 */
@RunWith(AndroidJUnit4::class)
class NoteTrimTest : ParentUiTest() {

    @Test fun textNote() = db.createText().let {
        launchSplash {
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
        launchSplash {
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