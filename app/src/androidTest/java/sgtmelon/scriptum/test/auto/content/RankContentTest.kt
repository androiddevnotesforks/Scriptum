package sgtmelon.scriptum.test.auto.content

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.adapter.holder.RankHolder
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.ui.screen.main.RankScreen

/**
 * Test for [RankScreen.Item]
 */
@RunWith(AndroidJUnit4::class)
class RankContentTest : ParentUiTest() {

    @Test fun itemList() = data.fillRankRelation(ITEM_COUNT).let { list ->
        launch { mainScreen { rankScreen { for(it in list) onAssertItem(it)  } } }
    }

    @Test fun visibleClick() = data.insertRank().let { item ->
        launch {
            mainScreen {
                rankScreen {
                    onAssertItem(item)

                    repeat(REPEAT_TIMES) {
                        onClickVisible(item)
                        onAssertItem(item.switchVisible())
                    }
                }
            }
        }
    }

    @Test fun visibleLongClick() = data.fillRank(ITEM_COUNT).let { list ->
        launch {
            mainScreen {
                rankScreen {
                    for (currentItem in list) {
                        onLongClickVisible(currentItem)

                        for (it in list) {
                            it.isVisible = it == currentItem
                            onAssertItem(it)
                        }
                    }
                }
            }
        }
    }


    @Test fun itemBind() = data.insertRankForNotes().let {
        launch {
            mainScreen {
                rankScreen { onAssertItem(it.first) }
                notesScreen { openNoteDialog(it.second) { onBind() } }
                rankScreen { onAssertItem(it.first.apply { bindCount = 1 }) }
            }
        }
    }

    @Test fun itemNotification() = data.insertRankForNotes().let {
        launch {
            mainScreen {
                rankScreen { onAssertItem(it.first) }
                notesScreen {
                    openNoteDialog(it.second) {
                        onNotification { onDate(day = 1).onClickApply { onClickApply() } }
                    }
                }
                rankScreen { onAssertItem(it.first.apply { notificationCount = 1 }) }
            }
        }
    }

    @Test fun itemMaxIndicator() = data.insertRank().let {
        launch(before = {
            RankHolder.isMaxTest = true
        }) {
            mainScreen { rankScreen { onAssertItem(it) } }
        }
    }

    companion object {
        private const val ITEM_COUNT = 3
        private const val REPEAT_TIMES = 3
    }
}