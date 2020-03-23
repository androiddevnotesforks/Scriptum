package sgtmelon.scriptum.test.auto.main.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test clear dialog for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinClearDialogTest : ParentUiTest() {

    @Test fun closeAndWork() = launch({ data.fillBin() }) {
        mainScreen {
            binScreen {
                clearDialog { onCloseSoft() }.assert(empty = false)
                clearDialog { onClickNo() }.assert(empty = false)
                clearDialog { onClickYes() }.assert(empty = true)
            }
        }
    }

    @Test fun workWithHideNotes() = data.insertRankForBin().let {
        launch({ data.fillBin(count = 5) }) {
            mainScreen {
                binScreen()

                rankScreen { onClickVisible() }
                binScreen { clearDialog { onClickYes() }.assert(empty = true) }

                rankScreen { onClickVisible() }
                binScreen(empty = true)
            }
        }
    }

}