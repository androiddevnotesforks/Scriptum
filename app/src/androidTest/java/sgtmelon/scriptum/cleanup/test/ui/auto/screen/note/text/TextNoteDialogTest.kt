package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test dialogs for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNoteDialogTest : ParentUiTest() {

    @Test fun dateDialogCloseAndWork() = db.insertText().let {
        launch {
            mainScreen {
                openNotes {
                    openText(it) {
                        controlPanel { onNotification { softClose() } }.assert()
                        controlPanel { onNotification { cancel() } }.assert()
                        controlPanel { onNotification { applyDate() } }
                    }
                }
            }
        }
    }

    @Test fun convertDialogCloseAndWork() = db.insertText().let {
        launch {
            mainScreen {
                openNotes {
                    openText(it) {
                        controlPanel { onConvert { softClose() } }.assert()
                        controlPanel { onConvert { onClickNo() } }.assert()
                        controlPanel { onConvert { onClickYes() } }.afterConvert()
                    }
                }
            }
        }
    }

    @Test fun rankDialogCloseAndWork() = db.fillRank(count = 3).let {
        launch {
            mainScreen {
                openAddDialog {
                    createText(db.createText(), isRankEmpty = false) {
                        controlPanel { onRank(it) { softClose() } }.assert()
                        controlPanel { onRank(it) { onClickCancel() } }.assert()
                        controlPanel { onRank(it) { onClickItem(p = 1).onClickApply() } }.assert()
                    }
                }
            }
        }
    }
}