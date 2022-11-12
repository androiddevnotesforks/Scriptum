package sgtmelon.scriptum.ui.auto.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.ui.item.RankItemUi
import sgtmelon.scriptum.infrastructure.adapter.holder.RankHolder
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test for [RankItemUi].
 */
@RunWith(AndroidJUnit4::class)
class RankCardTest : ParentUiTest() {

    @Test fun visibleClick() = db.insertRank().let { item ->
        launch {
            mainScreen {
                openRank {
                    assertItem(item)

                    repeat(REPEAT_TIMES) {
                        itemVisible()
                        item.isVisible = !item.isVisible
                        assertItem(item)
                    }
                }
            }
        }
    }

    @Test fun bindIndicator() = db.insertRankForNotes().let {
        launch {
            mainScreen {
                openRank { assertItem(it.first) }
                openNotes { openNoteDialog(it.second) { bind() } }
                openRank { assertItem(it.first.apply { bindCount = 1 }) }
            }
        }
    }

    @Test fun notificationIndicator() = db.insertRankForNotes().let {
        launch {
            mainScreen {
                openRank { assertItem(it.first) }
                openNotes {
                    openNoteDialog(it.second) {
                        notification { set(addDay = 1).applyDate { applyTime() } }
                        it.first.notificationCount += 1
                    }
                }
                openRank { assertItem(it.first) }
            }
        }
    }

    @Test fun maxCountIndicator() = db.insertRank().let {
        launch({ RankHolder.isMaxTest = true }) {
            mainScreen { openRank { assertItem(it) } }
        }

        RankHolder.isMaxTest = false
    }

    companion object {
        private const val REPEAT_TIMES = 3
    }
}