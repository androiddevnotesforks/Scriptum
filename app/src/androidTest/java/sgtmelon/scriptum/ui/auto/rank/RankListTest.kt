package sgtmelon.scriptum.ui.auto.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchRank
import sgtmelon.scriptum.source.ui.tests.launchRankList
import sgtmelon.scriptum.source.cases.list.ListContentCase
import sgtmelon.scriptum.source.cases.list.ListScrollCase

/**
 * Test list for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankListTest : ParentUiTest(),
    ListContentCase,
    ListScrollCase {

    @Test override fun contentEmpty() = launchRank(isEmpty = true)

    @Test override fun contentList() = db.fillRankRelation().let {
        launchRank { assertList(it) }
    }

    @Test override fun listScroll() = launchRankList { scrollThrough() }
}