package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test list for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankListTest : ParentUiTest() {

    @Test fun contentEmpty() = launch { mainScreen { rankScreen(isEmpty = true) } }

    @Test fun contentList() = launch({ db.fillRank() }) { mainScreen { rankScreen() } }

    @Test fun listScroll() = launch({ db.fillRank() }) {
        mainScreen { rankScreen { onScrollThrough() } }
    }

}