package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteFragment

import sgtmelon.scriptum.source.ui.tests.ParentUiTest

/**
 * Test for [RollNoteFragment]
 */
@RunWith(AndroidJUnit4::class)
class RollNoteDialogTest : ParentUiTest() {

    @Test fun dateDialogCloseAndWork() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
                        controlPanel { onNotification { softClose() } }.assert()
                        controlPanel { onNotification { cancel() } }.assert()
                        controlPanel { onNotification { applyDate() } }
                    }
                }
            }
        }
    }

    @Test fun convertDialogCloseAndWork() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
                        controlPanel { onConvert { softClose() } }.assert()
                        controlPanel { onConvert { negative() } }.assert()
                        controlPanel { onConvert { positive() } }.afterConvert()
                    }
                }
            }
        }
    }

    @Test fun rankDialogCloseAndWork() = db.fillRank(count = 3).let {
        launchSplash {
            mainScreen {
                openAddDialog {
                    createRoll(db.createRoll(), isRankEmpty = false) {
                        controlPanel { onRank(it) { softClose() } }.assert()
                        controlPanel { onRank(it) { onClickCancel() } }.assert()
                        controlPanel { onRank(it) { onClickItem(p = 1).onClickApply() } }.assert()
                    }
                }
            }
        }
    }
}