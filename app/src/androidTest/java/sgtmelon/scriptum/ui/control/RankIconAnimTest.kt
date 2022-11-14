package sgtmelon.scriptum.ui.control

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.auto.rank.startRankItemTest

/**
 * Test of icon animation in [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankIconAnimTest : ParentUiTest() {

    @Test fun visibleClick() = startRankItemTest(db.insertRank()) {
        repeat(times = 7) { itemVisible() }
    }
}