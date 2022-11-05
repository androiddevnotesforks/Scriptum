package sgtmelon.scriptum.cleanup.test.ui.auto.item

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.ui.item.RankItemUi
import sgtmelon.scriptum.infrastructure.adapter.holder.RankHolder
import sgtmelon.scriptum.ui.testing.parent.ParentUiTest

/**
 * Test for [RankItemUi].
 */
@RunWith(AndroidJUnit4::class)
class RankContentTest : ParentUiTest() {

    @Test fun itemList() = db.fillRankRelation(ITEM_COUNT).let { list ->
        launch {
            mainScreen { rankScreen { for ((i, it) in list.withIndex()) onAssertItem(it, i) } }
        }
    }

    @Test fun visibleClick() = db.insertRank().let { item ->
        launch {
            mainScreen {
                rankScreen {
                    onAssertItem(item)

                    repeat(REPEAT_TIMES) {
                        onClickVisible()
                        onAssertItem(item.switchVisible())
                    }
                }
            }
        }
    }


    @Test fun itemBind() = db.insertRankForNotes().let {
        launch {
            mainScreen {
                rankScreen { onAssertItem(it.first) }
                notesScreen { openNoteDialog(it.second) { onBind() } }
                rankScreen { onAssertItem(it.first.apply { bindCount = 1 }) }
            }
        }
    }

    @Test fun itemNotification() = db.insertRankForNotes().let {
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

    @Test fun itemMaxIndicator() = db.insertRank().let {
        launch({ RankHolder.isMaxTest = true }) {
            mainScreen { rankScreen { onAssertItem(it) } }
        }

        RankHolder.isMaxTest = false
    }

    companion object {
        private const val ITEM_COUNT = 3
        private const val REPEAT_TIMES = 3
    }
}