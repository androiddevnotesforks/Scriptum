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
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onNotification { onCloseSoft() } }.assert()
                        controlPanel { onNotification { onClickCancel() } }.assert()
                        controlPanel { onNotification { onClickApply() } }
                    }
                }
            }
        }
    }

    @Test fun convertDialogCloseAndWork() = db.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onConvert { onCloseSoft() } }.assert()
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
                        controlPanel { onRank(it) { onCloseSoft() } }.assert()
                        controlPanel { onRank(it) { onClickCancel() } }.assert()
                        controlPanel { onRank(it) { onClickItem(p = 1).onClickApply() } }.assert()
                    }
                }
            }
        }
    }
}