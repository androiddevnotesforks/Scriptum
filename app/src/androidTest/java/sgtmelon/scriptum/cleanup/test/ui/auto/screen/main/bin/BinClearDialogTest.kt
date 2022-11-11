package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test clear dialog for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinClearDialogTest : ParentUiTest() {

    @Test fun closeAndWork() = launch({ db.fillBin() }) {
        mainScreen {
            openBin {
                openClearDialog { onCloseSoft() }.assert(isEmpty = false)
                openClearDialog { onClickNo() }.assert(isEmpty = false)
                openClearDialog { onClickYes() }.assert(isEmpty = true)
            }
        }
    }

    @Test fun workWithHideNotes() = db.insertRankForBin().let {
        launch({ db.fillBin(count = 5) }) {
            mainScreen {
                openBin()

                openRank { itemVisible() }
                openBin { openClearDialog { onClickYes() }.assert(isEmpty = true) }

                openRank { itemVisible() }
                openBin(isEmpty = true)
            }
        }
    }

}