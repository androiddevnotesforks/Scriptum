package sgtmelon.scriptum.test.auto.screen.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test for [RollNoteFragment]
 */
@RunWith(AndroidJUnit4::class)
class RollNoteDialogTest : ParentUiTest() {

    @Test fun dateDialogCloseAndWork() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onNotification { onCloseSoft() } }.assert()
                        controlPanel { onNotification { onClickCancel() } }.assert()
                        controlPanel { onNotification { onClickApply() } }
                    }
                }
            }
        }
    }

    @Test fun convertDialogCloseAndWork() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onConvert { onCloseSoft() } }.assert()
                        controlPanel { onConvert { onClickNo() } }.assert()
                        controlPanel { onConvert { onClickYes() } }.afterConvert()
                    }
                }
            }
        }
    }

    @Test fun rankDialogCloseAndWork() = data.fillRank(count = 3).let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(data.createRoll(), isRankEmpty = false) {
                        controlPanel { onRank(it) { onCloseSoft() } }.assert()
                        controlPanel { onRank(it) { onClickCancel() } }.assert()
                        controlPanel { onRank(it) { onClickItem(p = 1).onClickApply() } }.assert()
                    }
                }
            }
        }
    }
}