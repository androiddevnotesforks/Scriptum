package sgtmelon.scriptum.ui.auto.bin


import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.parent.ui.tests.launchBin
import sgtmelon.scriptum.parent.ui.tests.launchBinItem
import sgtmelon.scriptum.parent.ui.tests.launchBinList
import sgtmelon.scriptum.ui.cases.list.ListContentCase
import sgtmelon.scriptum.ui.cases.list.ListScrollCase
import sgtmelon.scriptum.ui.cases.note.NoteOpenCase

/**
 * Test list for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinListTest : ParentUiTest(),
    ListContentCase,
    ListScrollCase,
    NoteOpenCase {

    @Test override fun contentEmpty() = launchBin(isEmpty = true)

    @Test override fun contentList() = launchBinList { assertList(it) }

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