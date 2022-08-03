package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.parent.ParentUiTest

/**
 * Test list item actions result for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankItemTest : ParentUiTest() {

    @Test fun visibleForNotes() = db.insertRankForNotes().let {
        launch {
            mainScreen {
                notesScreen()
                rankScreen { onClickVisible() }
                notesScreen(isEmpty = true, isHide = true)
                rankScreen { onClickVisible() }
                notesScreen()
            }
        }
    }

    @Test fun visibleForBin() = db.insertRankForBin().let {
        launch {
            mainScreen {
                binScreen()
                rankScreen { onClickVisible() }
                binScreen()
                rankScreen { onClickVisible() }
                binScreen()
            }
        }
    }

    @Test fun clearFromList() = db.insertRank().let {
        launch { mainScreen { rankScreen { onClickCancel().assert(isEmpty = true) } } }
    }

    @Test fun clearForNote() = db.insertRankForNotes().let {
        launch {
            mainScreen {
                rankScreen { onClickVisible() }
                notesScreen(isEmpty = true, isHide = true)
                rankScreen { onClickCancel() }
                notesScreen()
            }
        }
    }

    @Test fun clearForBin() = db.insertRankForBin().let {
        launch {
            mainScreen {
                binScreen()
                rankScreen { onClickVisible() }
                binScreen()
                rankScreen { onClickCancel() }
                binScreen()
            }
        }
    }

}