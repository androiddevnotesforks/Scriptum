package sgtmelon.scriptum.ui.auto.bin


import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.ListContentCase
import sgtmelon.scriptum.ui.cases.ListScrollCase
import sgtmelon.scriptum.ui.cases.NoteOpenCase

/**
 * Test list for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinListTest : ParentUiTest(),
    ListContentCase,
    ListScrollCase,
    NoteOpenCase {

    @Test override fun contentEmpty() = launch { mainScreen { openBin(isEmpty = true) } }

    @Test override fun contentList() = launch({ db.fillBin() }) { mainScreen { openBin() } }

    @Test override fun listScroll() = launch({ db.fillBin() }) {
        mainScreen { openBin { scrollThrough() } }
    }

    @Test override fun itemTextOpen() = db.insertTextToBin().let {
        launch {
            mainScreen {
                openBin {
                    openText(it) { pressBack() }
                    assert(isEmpty = false)
                }
            }
        }
    }

    @Test override fun itemRollOpen() = db.insertRollToBin().let {
        launch {
            mainScreen {
                openBin {
                    openRoll(it) { pressBack() }
                    assert(isEmpty = false)
                }
            }
        }
    }
}