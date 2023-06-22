package sgtmelon.scriptum.tests.ui.auto.main.bin


import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment
import sgtmelon.scriptum.source.ui.tests.launchBin
import sgtmelon.scriptum.source.ui.tests.launchBinItem
import sgtmelon.scriptum.source.ui.tests.launchBinList
import sgtmelon.scriptum.source.cases.list.ListContentCase
import sgtmelon.scriptum.source.cases.list.ListScrollCase
import sgtmelon.scriptum.source.cases.note.NoteOpenCase
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchMain

/**
 * Test list for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinListTest : ParentUiRotationTest(),
    ListContentCase,
    ListScrollCase,
    NoteOpenCase {

    @Test override fun contentEmpty() = launchBin(isEmpty = true)

    @Test override fun contentList() = launchBinList { assertList(it) }

    @Test override fun contentRotateEmpty() = launchMain {
        openBin(isEmpty = true) {
            rotate.toSide()
            assert(isEmpty = true)
        }
        assert(isFabVisible = false)
    }

    @Test override fun contentRotateList() = db.fillBin().let {
        launchMain {
            openBin {
                rotate.toSide()
                assert(isEmpty = false)
                assertList(it)
            }
            assert(isFabVisible = false)
        }
    }

    @Test override fun listScroll() = launchBinList { scrollThrough() }

    @Test override fun itemTextOpen() = launchBinItem(db.insertTextToBin()) {
        openText(it) { pressBack() }
        assert(isEmpty = false)
    }

    @Test override fun itemRollOpen() = launchBinItem(db.insertRollToBin()) {
        openRoll(it) { pressBack() }
        assert(isEmpty = false)
    }
}