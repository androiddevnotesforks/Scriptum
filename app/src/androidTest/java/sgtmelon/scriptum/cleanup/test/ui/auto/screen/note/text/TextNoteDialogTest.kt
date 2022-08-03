package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.text

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.parent.ParentUiTest

/**
 * Test dialogs for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNoteDialogTest : ParentUiTest() {

    @Test fun dateDialogCloseAndWork() = data.insertText().let {
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

    @Test fun convertDialogCloseAndWork() = data.insertText().let {
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

    @Test fun rankDialogCloseAndWork() = data.fillRank(count = 3).let {
        launch {
            mainScreen {
                openAddDialog {
                    createText(data.createText(), isRankEmpty = false) {
                        controlPanel { onRank(it) { onCloseSoft() } }.assert()
                        controlPanel { onRank(it) { onClickCancel() } }.assert()
                        controlPanel { onRank(it) { onClickItem(p = 1).onClickApply() } }.assert()
                    }
                }
            }
        }
    }
}