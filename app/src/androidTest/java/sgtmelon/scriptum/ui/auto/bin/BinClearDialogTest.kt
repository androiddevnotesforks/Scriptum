package sgtmelon.scriptum.ui.auto.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.dialog.DialogCloseCase

/**
 * Test clear dialog for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinClearDialogTest : ParentUiTest(),
    DialogCloseCase {

    @Test override fun close() = startBinListTest {
        openClearDialog { softClose() }.assert(isEmpty = false)
    }

    @Test fun work() = startBinListTest {
        openClearDialog { no() }.assert(isEmpty = false)
        openClearDialog { yes() }.assert(isEmpty = true)
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