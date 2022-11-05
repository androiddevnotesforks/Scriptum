package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.bin


import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment
import sgtmelon.scriptum.ui.testing.parent.ParentUiTest
import sgtmelon.scriptum.ui.testing.parent.launch

/**
 * Test list for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinListTest : ParentUiTest() {

    @Test fun contentEmpty() = launch { mainScreen { binScreen(isEmpty = true) } }

    @Test fun contentList() = launch({ db.fillBin() }) { mainScreen { binScreen() } }

    @Test fun listScroll() = launch({ db.fillBin() }) {
        mainScreen { binScreen { onScrollThrough() } }
    }


    @Test fun textNoteOpen() = db.insertTextToBin().let {
        launch {
            mainScreen { binScreen { openTextNote(it) { onPressBack() }.assert(isEmpty = false) } }
        }
    }

    @Test fun rollNoteOpen() = db.insertRollToBin().let {
        launch {
            mainScreen { binScreen { openRollNote(it) { onPressBack() }.assert(isEmpty = false) } }
        }
    }

}