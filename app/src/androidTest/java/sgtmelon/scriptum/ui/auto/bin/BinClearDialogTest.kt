package sgtmelon.scriptum.ui.auto.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.DialogCloseCase

/**
 * Test clear dialog for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinClearDialogTest : ParentUiTest(),
    DialogCloseCase {

    @Test override fun close() = launch({ db.fillBin() }) {
        mainScreen { openBin { openClearDialog { softClose() }.assert(isEmpty = false) } }
    }

    @Test fun work() = launch({ db.fillBin() }) {
        mainScreen {
            openBin {
                openClearDialog { no() }.assert(isEmpty = false)
                openClearDialog { yes() }.assert(isEmpty = true)
            }
        }
    }

    @Test fun workWithHideNotes() = db.insertRankForBin().let {
        assertTrue(it.first.isVisible)

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