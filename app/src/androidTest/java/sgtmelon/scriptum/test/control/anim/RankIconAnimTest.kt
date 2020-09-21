package sgtmelon.scriptum.test.control.anim

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.iconanim.IconAnimControl
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test of [IconAnimControl] animations for [RankFragment]
 */
@RunWith(AndroidJUnit4::class)
class RankIconAnimTest : ParentUiTest() {

    @Test fun visibleClick() = data.insertRank().let {
        launch { mainScreen { rankScreen { repeat(REPEAT_COUNT) { onClickVisible() } } } }
    }

    @Test fun visibleLongClick() = data.fillRank(REPEAT_COUNT).let {
        launch { mainScreen { rankScreen { for (item in it) onLongClickVisible(item) } } }
    }


    private companion object {
        const val REPEAT_COUNT = 7
    }
}