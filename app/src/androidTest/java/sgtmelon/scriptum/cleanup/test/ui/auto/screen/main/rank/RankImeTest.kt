package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test keyboard ime click for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankImeTest : ParentUiTest() {

    @Test fun toolbarImeAdd() = launch({ db.fillRank() }) {
        val name = nextString()

        mainScreen {
            openRank {
                /** Check ime action with wrong input data. (Empty name) */
                toolbar { enter(name = " ", isGood = false).imeClick(isSuccess = false) }

                toolbar { enter(name).imeClick() }
                openRenameDialog(name, p = count - 1) { softClose() }

                /** Check ime action with wrong input data. (Name from list) */
                toolbar { enter(name, isGood = false).imeClick(isSuccess = false) }

                itemCancel(p = count - 1)

                toolbar { enter(name).imeClick() }
                openRenameDialog(name, p = count - 1)
            }
        }
    }

    @Test fun renameImeResult() = db.insertRank().let {
        val newName = nextString()

        launch {
            mainScreen {
                openRank {
                    /** Check ime action with wrong input data. (Empty name) */
                    openRenameDialog(it.name) {
                        enter(name = " ", isEnabled = false).imeClick(isSuccess = false)
                        softClose()
                    }

                    /** Check ime action with wrong input data. (Name from list) */
                    openRenameDialog(it.name) {
                        enter(it.name, isEnabled = false).imeClick(isSuccess = false)
                        softClose()
                    }

                    openRenameDialog(it.name) { enter(newName).imeClick() }

                    it.name = newName
                    assertItem(it)
                }
            }
        }
    }
}