package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteFragmentImpl

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test enter focus for [RollNoteFragmentImpl].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteFocusTest : ParentUiTest() {

    @Test fun focusOnCreate() = db.createRoll().let {
        launchSplash {
            mainScreen {
                openNotes(isEmpty = true) {
                    openAddDialog { createRoll(it) { toolbar { assertFocus() } } }
                }
            }
        }
    }

    @Test fun focusOnEdit() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
                        controlPanel { onEdit() }
                        enterPanel { onEnterText(nextString()) }

                        controlPanel { onSave().onEdit() }
                        enterPanel { assertFocus() }
                    }
                }
            }
        }
    }
}