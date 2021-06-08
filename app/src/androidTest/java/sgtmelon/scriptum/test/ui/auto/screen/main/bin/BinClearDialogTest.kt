package sgtmelon.scriptum.test.ui.auto.screen.main.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test clear dialog for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinClearDialogTest : ParentUiTest() {

    @Test fun closeAndWork() = launch({ data.fillBin() }) {
        mainScreen {
            binScreen {
                clearDialog { onCloseSoft() }.assert(isEmpty = false)
                clearDialog { onClickNo() }.assert(isEmpty = false)
                clearDialog { onClickYes() }.assert(isEmpty = true)
            }
        }
    }

    @Test fun workWithHideNotes() = data.insertRankForBin().let {
        launch({ data.fillBin(count = 5) }) {
            mainScreen {
                binScreen()

                rankScreen { onClickVisible() }
                binScreen { clearDialog { onClickYes() }.assert(isEmpty = true) }

                rankScreen { onClickVisible() }
                binScreen(isEmpty = true)
            }
        }
    }

}