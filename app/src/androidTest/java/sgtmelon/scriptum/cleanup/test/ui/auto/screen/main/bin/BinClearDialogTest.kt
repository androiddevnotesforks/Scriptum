package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment
import sgtmelon.scriptum.parent.ui.ParentUiTest
import sgtmelon.scriptum.parent.ui.launch

/**
 * Test clear dialog for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinClearDialogTest : ParentUiTest() {

    @Test fun closeAndWork() = launch({ db.fillBin() }) {
        mainScreen {
            binScreen {
                clearDialog { onCloseSoft() }.assert(isEmpty = false)
                clearDialog { onClickNo() }.assert(isEmpty = false)
                clearDialog { onClickYes() }.assert(isEmpty = true)
            }
        }
    }

    @Test fun workWithHideNotes() = db.insertRankForBin().let {
        launch({ db.fillBin(count = 5) }) {
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