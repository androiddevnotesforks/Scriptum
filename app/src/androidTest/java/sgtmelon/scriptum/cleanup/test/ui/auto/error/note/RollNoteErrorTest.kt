package sgtmelon.scriptum.cleanup.test.ui.auto.error.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.cleanup.test.ui.auto.error.Description
import sgtmelon.scriptum.parent.ui.ParentUiTest
import sgtmelon.scriptum.parent.ui.launch
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
                notesScreen(isEmpty = true) {
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

                    onAssertItem(item)
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
                notesScreen {
                    openRollNote(it) {
                        onClickCheck()
                        controlPanel { onEdit() }
                        toolbar { clickBack() }

                        controlPanel { onBind().onEdit() }
                        toolbar { clickBack() }

                        controlPanel {
                            onNotification { onClickApply { onTime(min = 3).onClickApply() } }
                            onEdit()
                        }
                        toolbar { clickBack() }
                    }
                }
            }
        }
    }
}