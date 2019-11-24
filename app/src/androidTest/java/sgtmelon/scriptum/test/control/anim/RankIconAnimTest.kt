package sgtmelon.scriptum.test.control.anim

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.iconanim.IconAnimControl
import sgtmelon.scriptum.screen.ui.main.RankFragment
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
        launch { mainScreen { rankScreen { it.forEach { onLongClickVisible(it) } } } }
    }


    private companion object {
        const val REPEAT_COUNT = 7
    }

}