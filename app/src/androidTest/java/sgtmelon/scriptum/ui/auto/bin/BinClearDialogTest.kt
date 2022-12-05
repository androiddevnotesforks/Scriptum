package sgtmelon.scriptum.ui.auto.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.parent.ui.tests.launchBinList
import sgtmelon.scriptum.parent.ui.tests.launchMain
import sgtmelon.scriptum.ui.cases.dialog.DialogCloseCase

/**
 * Test clear dialog for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinClearDialogTest : ParentUiTest(),
    DialogCloseCase {

    @Test override fun close() = launchBinList {
        openClearDialog { softClose() }
        assert(isEmpty = false)
    }

    @Test fun work() = launchBinList {
        openClearDialog { no() }
        assert(isEmpty = false)
        openClearDialog { yes() }
        assert(isEmpty = true)
    }

    @Test fun workWithHideNotes() = db.insertRankForBin().let {
        assertTrue(it.first.isVisible)

        launchMain({ db.fillBin(count = 5) }) {
            openBin()

            openRank { itemVisible() }
            openBin {
                openClearDialog { yes() }
                assert(isEmpty = true)
            }

            openRank { itemVisible() }
            openBin(isEmpty = true)
        }
    }
}