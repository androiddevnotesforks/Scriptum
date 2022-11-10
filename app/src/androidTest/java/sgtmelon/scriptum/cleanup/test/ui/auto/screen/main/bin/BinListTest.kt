package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.bin


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

    @Test fun contentEmpty() = launch { mainScreen { binScreen(isEmpty = true) } }

    @Test fun contentList() = launch({ db.fillBin() }) { mainScreen { binScreen() } }

    @Test fun listScroll() = launch({ db.fillBin() }) {
        mainScreen { binScreen { scrollThrough() } }
    }


    @Test fun textNoteOpen() = db.insertTextToBin().let {
        launch {
            mainScreen { binScreen { openTextNote(it) { pressBack() }.assert(isEmpty = false) } }
        }
    }

    @Test fun rollNoteOpen() = db.insertRollToBin().let {
        launch {
            mainScreen { binScreen { openRollNote(it) { pressBack() }.assert(isEmpty = false) } }
        }
    }

}