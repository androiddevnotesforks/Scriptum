package sgtmelon.scriptum.cleanup.test.ui.control.anim

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.iconanim.control.IconAnimControl
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment
import sgtmelon.scriptum.parent.ui.ParentUiTest
import sgtmelon.scriptum.parent.ui.launch

/**
 * Test of [IconAnimControl] animations for [RankFragment]
 */
@RunWith(AndroidJUnit4::class)
class RankIconAnimTest : ParentUiTest() {

    @Test fun visibleClick() = db.insertRank().let {
        launch { mainScreen { rankScreen { repeat(REPEAT_COUNT) { onClickVisible() } } } }
    }

    companion object {
        private const val REPEAT_COUNT = 7
    }
}