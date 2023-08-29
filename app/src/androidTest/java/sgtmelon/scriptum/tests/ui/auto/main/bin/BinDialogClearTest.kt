package sgtmelon.scriptum.tests.ui.auto.main.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.dialog.DialogRotateCase
import sgtmelon.scriptum.source.ui.screen.dialogs.message.ClearDialogUi
import sgtmelon.scriptum.source.ui.screen.main.BinScreen
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchBinList
import sgtmelon.scriptum.source.ui.tests.launchMain

/**
 * Test clear dialog for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinDialogClearTest : ParentUiRotationTest(),
    DialogCloseCase,
    DialogRotateCase {

    @Test override fun close() = launchBinList {
        openClearDialog { softClose() }
        assert(isEmpty = false)
        openClearDialog { negative() }
        assert(isEmpty = false)
    }

    @Test fun work() = launchBinList {
        openClearDialog { positive() }
        assert(isEmpty = true)
    }

    @Test fun workWithHideNotes() = db.insertRankForBin().let {
        assertTrue(it.first.isVisible)

        launchMain({ db.fillBin(count = 5) }) {
            openBin()

            openRank { itemVisible() }
            openBin {
                openClearDialog { positive() }
                assert(isEmpty = true)
            }

            openRank { itemVisible() }
            openBin(isEmpty = true)
        }
    }

    @Test override fun rotateClose() = launchBinList {
        assertRotationClose { softClose() }
        assertRotationClose { negative() }
    }

    /** Allow to [closeDialog] in different ways. */
    private fun BinScreen.assertRotationClose(closeDialog: ClearDialogUi.() -> Unit) {
        openClearDialog {
            rotate.switch()
            assert()
            closeDialog(this)
        }
        assert(isEmpty = false)
    }

    @Test override fun rotateWork() = launchBinList {
        openClearDialog {
            rotate.switch()
            assert()
            positive()
        }
        assert(isEmpty = true)
    }
}