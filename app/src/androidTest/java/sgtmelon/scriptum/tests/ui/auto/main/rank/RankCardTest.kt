package sgtmelon.scriptum.tests.ui.auto.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.utils.extensions.maxIndicatorTest
import sgtmelon.scriptum.source.ui.screen.item.RankItemUi
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchMain
import sgtmelon.scriptum.source.ui.tests.launchRankItem

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
        maxIndicatorTest = true
        launchRankItem(db.insertRank()) { assertItem(it) }
        maxIndicatorTest = false
    }
}