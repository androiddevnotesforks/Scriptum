package sgtmelon.scriptum.tests.ui.weight

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment
import sgtmelon.scriptum.source.ui.model.key.Scroll
import sgtmelon.scriptum.source.ui.tests.ParentUiWeighTest
import sgtmelon.scriptum.source.ui.tests.launchBinItem
import sgtmelon.scriptum.source.ui.tests.launchBinList
import sgtmelon.scriptum.source.cases.list.ListScrollCase
import sgtmelon.scriptum.source.cases.note.NoteOpenCase

/**
 * Weigh test for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinWeighTest : ParentUiWeighTest(),
    ListScrollCase,
    NoteOpenCase {

    @Test override fun listScroll() = launchBinList(ITEM_COUNT) {
        scrollTo(Scroll.END, SCROLL_COUNT)
    }

    @Test override fun itemTextOpen() = launchBinItem(db.insertTextToBin(dbWeight.textNote)) {
        repeat(REPEAT_COUNT) { _ -> openText(it) { toolbar { clickBack() } } }
    }

    @Test override fun itemRollOpen() = launchBinItem(
        db.insertRollToBin(isVisible = true, list = dbWeight.rollList)
    ) {
        repeat(REPEAT_COUNT) { _ -> openRoll(it) { toolbar { clickBack() } } }
    }
}