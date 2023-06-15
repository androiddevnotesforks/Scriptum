package sgtmelon.scriptum.tests.ui.auto.main.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment

import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchBinItem
import sgtmelon.scriptum.source.ui.tests.launchBinList
import sgtmelon.scriptum.source.ui.tests.launchMain
import sgtmelon.scriptum.source.cases.list.ListContentCase

/**
 * Test of [BinFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class BinRotationTest : ParentUiRotationTest(),
    ListContentCase {

    @Test override fun contentEmpty() = launchMain {
        openBin(isEmpty = true) {
            rotate.toSide()
            assert(isEmpty = true)
        }
        assert(isFabVisible = false)
    }

    @Test override fun contentList() = db.fillBin().let {
        launchMain {
            openBin {
                rotate.toSide()
                assert(isEmpty = false)
                assertList(it)
            }
            assert(isFabVisible = false)
        }
    }

    @Test fun clearDialog() = launchBinList {
        openClearDialog {
            rotate.toSide()
            assert()
        }
    }

    @Test fun textNoteDialog() = launchBinItem(db.insertTextToBin()) {
        openNoteDialog(it) {
            rotate.toSide()
            assert()
        }
    }

    @Test fun rollNoteDialog() = launchBinItem(db.insertRollToBin()) {
        openNoteDialog(it) {
            rotate.toSide()
            assert()
        }
    }
}