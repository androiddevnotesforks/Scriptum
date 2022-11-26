package sgtmelon.scriptum.ui.auto.bin


import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.NoteOpenCase
import sgtmelon.scriptum.ui.cases.list.ListContentCase
import sgtmelon.scriptum.ui.cases.list.ListScrollCase

/**
 * Test list for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinListTest : ParentUiTest(),
    ListContentCase,
    ListScrollCase,
    NoteOpenCase {

    @Test override fun contentEmpty() = launchSplash { mainScreen { openBin(isEmpty = true) } }

    @Test override fun contentList() = startBinListTest { assertList(it) }

    @Test override fun listScroll() = startBinListTest { scrollThrough() }

    @Test override fun itemTextOpen() = startBinItemTest(db.insertTextToBin()) {
        openText(it) { pressBack() }
        assert(isEmpty = false)
    }

    @Test override fun itemRollOpen() = startBinItemTest(db.insertRollToBin()) {
        openRoll(it) { pressBack() }
        assert(isEmpty = false)
    }
}