package sgtmelon.scriptum.ui.auto.rank

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
        launchSplash {
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
        launchSplash {
            mainScreen {
                openBin()
                openRank { itemVisible() }
                openBin()
                openRank { itemVisible() }
                openBin()
            }
        }
    }

    @Test fun clearFromList() = startRankItemTest(db.insertRank()) {
        itemCancel()
        assert(isEmpty = true)
    }

    @Test fun clearForNote() = db.insertRankForNotes().let {
        launchSplash {
            mainScreen {
                openRank { itemVisible() }
                openNotes(isEmpty = true, isHide = true)
                openRank { itemCancel() }
                openNotes()
            }
        }
    }

    @Test fun clearForBin() = db.insertRankForBin().let {
        launchSplash {
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