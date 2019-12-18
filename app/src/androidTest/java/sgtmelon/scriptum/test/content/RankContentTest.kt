package sgtmelon.scriptum.test.content

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.ui.screen.main.RankScreen

/**
 * Test for [RankScreen.Item]
 */
@RunWith(AndroidJUnit4::class)
class RankContentTest : ParentUiTest() {

    @Test fun itemList() = data.fillRankRelation(ITEM_COUNT).let { list ->
        launch { mainScreen { rankScreen { list.forEach { onAssertItem(it) } } } }
    }

    @Test fun visibleClick() = data.insertRank().let { item ->
        launch {
            mainScreen {
                rankScreen {
                    onAssertItem(item)

                    repeat(REPEAT_TIMES) {
                        onClickVisible(item)
                        onAssertItem(item.apply { isVisible = !isVisible })
                    }
                }
            }
        }
    }

    @Test fun visibleLongClick() = data.fillRank(ITEM_COUNT).let { list ->
        launch {
            mainScreen {
                rankScreen {
                    list.forEach { currentItem ->
                        onLongClickVisible(currentItem)
                        list.forEach { onAssertItem(it.apply { isVisible = it == currentItem }) }
                    }
                }
            }
        }
    }

    private companion object {
        const val ITEM_COUNT = 5
        const val REPEAT_TIMES = 3
    }

}