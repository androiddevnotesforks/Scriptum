package sgtmelon.scriptum.cleanup.test.ui.auto.error.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.cleanup.test.ui.auto.error.Description

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test fix of old errors for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteErrorTest : ParentUiTest() {


    /**
     * [Description.Note.Roll.RemoveEmptyItem]
     */
    @Test fun removeEmptyItemsAfterChangeDone() {
        var item = db.createRoll()

        launch {
            mainScreen {
                openNotes(isEmpty = true) {
                    openAddDialog {
                        createRoll(item) {
                            enterPanel { repeat(times = 4) { onAdd(nextString()) } }
                            onEnterText()

                            controlPanel { onSave() }
                            onAssertAll()

                            item = this.item
                            pressBack()
                        }
                    }

                    assertItem(item)
                }
            }
        }
    }

    /**
     * [Description.Note.RestoreChanges]
     */
    @Test fun restoreChanges() = db.insertRoll().let {
        launch {
            mainScreen {
                openNotes {
                    openRoll(it) {
                        onClickCheck()
                        controlPanel { onEdit() }
                        toolbar { clickBack() }

                        controlPanel { onBind().onEdit() }
                        toolbar { clickBack() }

                        controlPanel {
                            onNotification { applyDate { set(addMin = 3).applyTime() } }
                            onEdit()
                        }
                        toolbar { clickBack() }
                    }
                }
            }
        }
    }
}