package sgtmelon.scriptum.test.auto.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.main.RankFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test list item actions result for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankItemTest : ParentUiTest() {

    @Test fun visibleForNotes() = data.insertRankForNotes().let {
        launch {
            mainScreen {
                notesScreen()
                rankScreen { onClickVisible() }
                notesScreen(empty = true, hide = true)
                rankScreen { onClickVisible() }
                notesScreen()
            }
        }
    }

    @Test fun visibleForBin() = data.insertRankForBin().let {
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

    @Test fun clearFromList() = data.insertRank().let {
        launch { mainScreen { rankScreen { onClickCancel().assert(empty = true) } } }
    }

    @Test fun clearForNote() = data.insertRankForNotes().let {
        launch {
            mainScreen {
                rankScreen { onClickVisible() }
                notesScreen(empty = true, hide = true)
                rankScreen { onClickCancel() }
                notesScreen()
            }
        }
    }

    @Test fun clearForBin() = data.insertRankForBin().let {
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