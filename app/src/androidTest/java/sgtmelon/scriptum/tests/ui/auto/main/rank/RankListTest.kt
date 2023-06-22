package sgtmelon.scriptum.tests.ui.auto.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment
import sgtmelon.scriptum.source.ui.tests.launchRank
import sgtmelon.scriptum.source.ui.tests.launchRankList
import sgtmelon.scriptum.source.cases.list.ListContentCase
import sgtmelon.scriptum.source.cases.list.ListScrollCase
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchMain

/**
 * Test list for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankListTest : ParentUiRotationTest(),
    ListContentCase,
    ListScrollCase {

    @Test override fun contentEmpty() = launchRank(isEmpty = true)

    @Test override fun contentList() = db.fillRankRelation().let {
        launchRank { assertList(it) }
    }

    @Test override fun contentRotateEmpty() = launchMain {
        openRank(isEmpty = true) {
            rotate.toSide()
            assert(isEmpty = true)
        }
        assert(isFabVisible = false)
    }

    @Test override fun contentRotateList() = db.fillRank().let {
        launchMain {
            openRank {
                rotate.toSide()
                assert(isEmpty = false)
                assertList(it)
            }
            assert(isFabVisible = false)
        }
    }

    @Test override fun listScroll() = launchRankList { scrollThrough() }
}