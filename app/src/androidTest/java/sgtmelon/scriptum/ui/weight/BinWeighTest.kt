package sgtmelon.scriptum.ui.weight

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment
import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.tests.ParentUiWeighTest
import sgtmelon.scriptum.ui.auto.bin.startBinItemTest
import sgtmelon.scriptum.ui.auto.bin.startBinListTest
import sgtmelon.scriptum.ui.cases.NoteOpenCase
import sgtmelon.scriptum.ui.cases.list.ListScrollCase

/**
 * Weigh test for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinWeighTest : ParentUiWeighTest(),
    ListScrollCase,
    NoteOpenCase {

    @Test override fun listScroll() = startBinListTest(ITEM_COUNT) {
        scrollTo(Scroll.END, SCROLL_COUNT)
    }

    @Test override fun itemTextOpen() = startBinItemTest(db.insertTextToBin(dbWeight.textNote)) {
        repeat(REPEAT_COUNT) { _ -> openText(it) { toolbar { clickBack() } } }
    }

    @Test override fun itemRollOpen() = startBinItemTest(
        db.insertRollToBin(isVisible = true, list = dbWeight.rollList)
    ) {
        repeat(REPEAT_COUNT) { _ -> openRoll(it) { toolbar { clickBack() } } }
    }
}