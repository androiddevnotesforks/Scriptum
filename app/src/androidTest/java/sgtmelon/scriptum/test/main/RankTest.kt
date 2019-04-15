package sgtmelon.scriptum.test.main

import org.junit.Test
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.screen.view.main.RankFragment
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.screen.main.MainScreen

/**
 * Тест работы [RankFragment]
 *
 * @author SerjantArbuz
 */
class RankTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        preference.firstStart = false
    }


    @Test fun contentEmpty() {
        beforeLaunch { testData.clearAllData() }

        MainScreen { rankScreen { assert { onDisplayContent(empty = true) } } }
    }

    @Test fun contentList() {
        beforeLaunch { testData.apply { clearAllData() }.fillRank() }

        MainScreen { rankScreen { assert { onDisplayContent(empty = false) } } }
    }

    @Test fun listScroll() {
        beforeLaunch { testData.apply { clearAllData() }.fillRank(times = 20) }

        MainScreen {
            rankScreen {
                onScroll(Scroll.END, time = 4)
                onScroll(Scroll.START, time = 4)
            }
        }
    }


    @Test fun toolbarEnterAddEnable() {
        TODO()
    }

    @Test fun toolbarEnterClear() {
        TODO()
    }

    @Test fun toolbarEnterAddStart() {
        TODO()
    }

    @Test fun toolbarEnterAddEnd() {
        TODO()
    }


    @Test fun rankVisible() {
        TODO()
    }

    @Test fun rankClear() {
        TODO()
    }


    @Test fun renameDialogOpen() {
        TODO()
    }

    @Test fun renameDialogCloseSoft() {
        TODO()
    }

    @Test fun renameDialogCloseCancel() {
        TODO()
    }

    @Test fun renameDialogContent() {
        TODO()
    }

    @Test fun renameDialogBlockApplySameName() {
        TODO()
    }

    @Test fun renameDialogBlockApplyFromList() {
        TODO()
    }

    @Test fun renameDialogResult() {
        TODO()
    }

}