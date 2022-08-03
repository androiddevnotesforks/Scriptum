package sgtmelon.scriptum.cleanup.test.ui.control.anim

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.iconanim.control.IconAnimControl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.parent.ParentUiTest

/**
 * Test of [IconAnimControl] animations for [RankFragment]
 */
@RunWith(AndroidJUnit4::class)
class RankIconAnimTest : ParentUiTest() {

    @Test fun visibleClick() = data.insertRank().let {
        launch { mainScreen { rankScreen { repeat(REPEAT_COUNT) { onClickVisible() } } } }
    }

    @Test fun visibleLongClick() = data.fillRank(REPEAT_COUNT).let {
        launch { mainScreen { rankScreen { for (i in it.indices) onLongClickVisible(i) } } }
    }


    companion object {
        private const val REPEAT_COUNT = 7
    }
}