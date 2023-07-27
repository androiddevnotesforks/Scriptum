package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteFragment

import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test add panel for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteAddTest : ParentUiTest() {

    @Test fun enterAddEmpty() = db.createRoll().let {
        launchSplash {
            mainScreen {
                openNotes(isEmpty = true) {
                    openAddDialog { createRoll(it) { enterPanel { onAdd(text = "") } } }
                }
            }
        }
    }

    @Test fun addItems() = db.createRoll().let {
        launchSplash {
            mainScreen {
                openNotes(isEmpty = true) {
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