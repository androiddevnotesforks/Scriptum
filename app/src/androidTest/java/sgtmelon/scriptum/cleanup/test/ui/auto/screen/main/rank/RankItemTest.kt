package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test list item actions result for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankItemTest : ParentUiTest() {

    @Test fun visibleForNotes() = db.insertRankForNotes().let {
        launch {
            mainScreen {
                openNotes()
                openRank { itemVisible() }
                openNotes(isEmpty = true, isHide = true)
                openRank { itemVisible() }
                openNotes()
            }
        }
    }

    @Test fun visibleForBin() = db.insertRankForBin().let {
        launch {
            mainScreen {
                openBin()
                openRank { itemVisible() }
                openBin()
                openRank { itemVisible() }
                openBin()
            }
        }
    }

    @Test fun clearFromList() = db.insertRank().let {
        launch { mainScreen { openRank { itemCancel().assert(isEmpty = true) } } }
    }

    @Test fun clearForNote() = db.insertRankForNotes().let {
        launch {
            mainScreen {
                openRank { itemVisible() }
                openNotes(isEmpty = true, isHide = true)
                openRank { itemCancel() }
                openNotes()
            }
        }
    }

    @Test fun clearForBin() = db.insertRankForBin().let {
        launch {
            mainScreen {
                openBin()
                openRank { itemVisible() }
                openBin()
                openRank { itemCancel() }
                openBin()
            }
        }
    }
}