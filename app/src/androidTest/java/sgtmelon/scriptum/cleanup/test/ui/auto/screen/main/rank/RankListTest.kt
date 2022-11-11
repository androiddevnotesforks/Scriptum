package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.ListContentCase
import sgtmelon.scriptum.ui.cases.ListScrollCase

/**
 * Test list for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankListTest : ParentUiTest(),
    ListContentCase,
    ListScrollCase {

    @Test override fun contentEmpty() = launch { mainScreen { openRank(isEmpty = true) } }

    @Test override fun contentList() = launch({ db.fillRank() }) { mainScreen { openRank() } }

    @Test override fun listScroll() = launch({ db.fillRank() }) {
        mainScreen { openRank { scrollThrough() } }
    }
}