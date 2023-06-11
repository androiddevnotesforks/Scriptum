package sgtmelon.scriptum.ui.weight

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment
import sgtmelon.scriptum.source.ui.model.key.Scroll
import sgtmelon.scriptum.source.ui.tests.ParentUiWeighTest
import sgtmelon.scriptum.source.ui.tests.launchMain
import sgtmelon.scriptum.ui.cases.list.ListScrollCase

/**
 * Weigh test for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankWeighTest : ParentUiWeighTest(),
    ListScrollCase {

    @Test override fun listScroll() = launchMain({ db.fillRankRelation(ITEM_COUNT) }) {
        openRank { scrollTo(Scroll.END, SCROLL_COUNT) }
    }

}