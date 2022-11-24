package sgtmelon.scriptum.ui.weight

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment
import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.tests.ParentUiWeighTest
import sgtmelon.scriptum.ui.cases.list.ListScrollCase

/**
 * Weigh test for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankWeighTest : ParentUiWeighTest(),
    ListScrollCase {

    @Test override fun listScroll() = launch({ db.fillRankRelation(ITEM_COUNT) }) {
        mainScreen { openRank { scrollTo(Scroll.END, SCROLL_COUNT) } }
    }

}