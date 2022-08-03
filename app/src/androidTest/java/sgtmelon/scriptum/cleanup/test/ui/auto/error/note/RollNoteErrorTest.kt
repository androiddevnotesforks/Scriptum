package sgtmelon.scriptum.cleanup.test.ui.auto.error.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.cleanup.test.parent.ParentUiTest
import sgtmelon.scriptum.cleanup.test.ui.auto.error.Description

/**
 * Test fix of old errors for [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteErrorTest : ParentUiTest() {


    /**
     * [Description.Note.Roll.RemoveEmptyItem]
     */
    @Test fun removeEmptyItemsAfterChangeDone() {
        var item = data.createRoll()

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
                            onPressBack()
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
    @Test fun restoreChanges() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        onClickCheck()
                        controlPanel { onEdit() }
                        toolbar { onClickBack() }

                        controlPanel { onBind().onEdit() }
                        toolbar { onClickBack() }

                        controlPanel {
                            onNotification { onClickApply { onTime(min = 3).onClickApply() } }
                            onEdit()
                        }
                        toolbar { onClickBack() }
                    }
                }
            }
        }
    }
}