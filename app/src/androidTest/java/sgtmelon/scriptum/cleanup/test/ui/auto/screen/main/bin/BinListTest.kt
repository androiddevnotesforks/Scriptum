package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.bin


import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.cleanup.test.parent.ParentUiTest

/**
 * Test list for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinListTest : ParentUiTest() {

    @Test fun contentEmpty() = launch { mainScreen { binScreen(isEmpty = true) } }

    @Test fun contentList() = launch({ data.fillBin() }) { mainScreen { binScreen() } }

    @Test fun listScroll() = launch({ data.fillBin() }) {
        mainScreen { binScreen { onScrollThrough() } }
    }


    @Test fun textNoteOpen() = data.insertTextToBin().let {
        launch {
            mainScreen { binScreen { openTextNote(it) { onPressBack() }.assert(isEmpty = false) } }
        }
    }

    @Test fun rollNoteOpen() = data.insertRollToBin().let {
        launch {
            mainScreen { binScreen { openRollNote(it) { onPressBack() }.assert(isEmpty = false) } }
        }
    }

}