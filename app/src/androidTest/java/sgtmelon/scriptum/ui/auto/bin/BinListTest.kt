package sgtmelon.scriptum.ui.auto.bin


import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test list for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinListTest : ParentUiTest() {

    @Test fun contentEmpty() = launch { mainScreen { openBin(isEmpty = true) } }

    @Test fun contentList() = launch({ db.fillBin() }) { mainScreen { openBin() } }

    @Test fun listScroll() = launch({ db.fillBin() }) {
        mainScreen { openBin { scrollThrough() } }
    }


    @Test fun textNoteOpen() = db.insertTextToBin().let {
        launch {
            mainScreen {
                openBin {
                    openText(it) { pressBack() }
                    assert(isEmpty = false)
                }
            }
        }
    }

    @Test fun rollNoteOpen() = db.insertRollToBin().let {
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