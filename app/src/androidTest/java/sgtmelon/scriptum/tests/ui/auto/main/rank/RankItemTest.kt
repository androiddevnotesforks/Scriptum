package sgtmelon.scriptum.tests.ui.auto.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchMain
import sgtmelon.scriptum.source.ui.tests.launchRankItem

/**
 * Test list item actions result for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankItemTest : ParentUiTest() {

    @Test fun visibleForNotes() = db.insertRankForNotes().let {
        launchMain {
            openNotes()
            openRank { itemVisible() }
            openNotes(isEmpty = true, isHide = true)
            openRank { itemVisible() }
            openNotes()
        }
    }

    @Test fun visibleForBin() = db.insertRankForBin().let {
        launchMain {
            openBin()
            openRank { itemVisible() }
            openBin()
            openRank { itemVisible() }
            openBin()
        }
    }

    @Test fun clearFromList() = launchRankItem(db.insertRank()) {
        itemCancel()
        assert(isEmpty = true)
    }

    @Test fun clearForNote() = db.insertRankForNotes().let {
        launchMain {
            openRank { itemVisible() }
            openNotes(isEmpty = true, isHide = true)
            openRank { itemCancel() }
            openNotes()
        }
    }

    @Test fun clearForBin() = db.insertRankForBin().let {
        launchMain {
            openBin()
            openRank { itemVisible() }
            openBin()
            openRank { itemCancel() }
            openBin()
        }
    }
}