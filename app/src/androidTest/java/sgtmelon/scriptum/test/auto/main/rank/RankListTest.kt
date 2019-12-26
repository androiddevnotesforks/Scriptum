package sgtmelon.scriptum.test.auto.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test list for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankListTest : ParentUiTest() {

    @Test fun contentEmpty() = launch { mainScreen { rankScreen(empty = true) } }

    @Test fun contentList() = launch({ data.fillRank() }) { mainScreen { rankScreen() } }

    @Test fun listScroll() = launch({ data.fillRank() }) {
        mainScreen { rankScreen { onScrollThrough() } }
    }

}