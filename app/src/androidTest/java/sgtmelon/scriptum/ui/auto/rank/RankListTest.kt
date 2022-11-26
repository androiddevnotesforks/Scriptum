package sgtmelon.scriptum.ui.auto.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.list.ListContentCase
import sgtmelon.scriptum.ui.cases.list.ListScrollCase

/**
 * Test list for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankListTest : ParentUiTest(),
    ListContentCase,
    ListScrollCase {

    @Test override fun contentEmpty() = launchSplash { mainScreen { openRank(isEmpty = true) } }

    @Test override fun contentList() = db.fillRankRelation().let {
        launchSplash { mainScreen { openRank { assertList(it) } } }
    }

    @Test override fun listScroll() = startRankListTest { scrollThrough() }
}