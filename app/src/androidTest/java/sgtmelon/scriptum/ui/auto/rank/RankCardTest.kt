package sgtmelon.scriptum.ui.auto.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.adapter.holder.RankHolder
import sgtmelon.scriptum.parent.ui.screen.item.RankItemUi
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.parent.ui.tests.launchMain
import sgtmelon.scriptum.parent.ui.tests.launchRankItem

/**
 * Test for [RankItemUi].
 */
@RunWith(AndroidJUnit4::class)
class RankCardTest : ParentUiTest() {

    @Test fun visibleClick() = launchRankItem(db.insertRank()) {
        assertItem(it)
        repeat(times = 4) { _ ->
            itemVisible()
            it.isVisible = !it.isVisible
            assertItem(it)
        }
    }

    @Test fun bindIndicator() = db.insertRankForNotes().let {
        launchMain {
            openRank { assertItem(it.first) }
            openNotes { openNoteDialog(it.second) { bind() } }
            openRank { assertItem(it.first.apply { bindCount = 1 }) }
        }
    }

    @Test fun notificationIndicator() = db.insertRankForNotes().let {
        launchMain {
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

    @Test fun maxCountIndicator() {
        RankHolder.isMaxTest = true
        launchRankItem(db.insertRank()) { assertItem(it) }
        RankHolder.isMaxTest = false
    }
}