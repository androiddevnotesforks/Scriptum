package sgtmelon.scriptum.ui.auto.bin

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
                openClearDialog { softClose() }.assert(isEmpty = false)
                openClearDialog { no() }.assert(isEmpty = false)
                openClearDialog { yes() }.assert(isEmpty = true)
            }
        }
    }

    @Test fun workWithHideNotes() = db.insertRankForBin().let {
        launch({ db.fillBin(count = 5) }) {
            mainScreen {
                openBin()

                openRank { itemVisible() }
                openBin { openClearDialog { yes() }.assert(isEmpty = true) }

                openRank { itemVisible() }
                openBin(isEmpty = true)
            }
        }
    }
}