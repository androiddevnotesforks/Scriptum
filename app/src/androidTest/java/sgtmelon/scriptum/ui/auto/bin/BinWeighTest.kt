package sgtmelon.scriptum.ui.auto.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment
import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.tests.ParentUiWeighTest
import sgtmelon.scriptum.ui.cases.ListScrollCase
import sgtmelon.scriptum.ui.cases.NoteOpenCase

/**
 * Weigh test for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinWeighTest : ParentUiWeighTest(),
    ListScrollCase,
    NoteOpenCase {

    @Test override fun listScroll() = launch({ db.fillBin(ITEM_COUNT) }) {
        mainScreen { openBin { scrollTo(Scroll.END, SCROLL_COUNT) } }
    }

    @Test override fun itemTextOpen() = db.insertText(dbWeight.textNote).let { model ->
        launch {
            mainScreen {
                openBin {
                    repeat(REPEAT_COUNT) { openText(model) { toolbar { clickBack() } } }
                }
            }
        }
    }

    @Test override fun itemRollOpen() = db.insertRoll(
        isVisible = true,
        list = dbWeight.rollList
    ).let { model ->
        launch {
            mainScreen {
                openBin {
                    repeat(REPEAT_COUNT) { openRoll(model) { toolbar { clickBack() } } }
                }
            }
        }
    }
}