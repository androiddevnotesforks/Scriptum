package sgtmelon.scriptum.test.auto.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test dialogs for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankDialogTest : ParentUiTest() {

    @Test fun renameClose() = data.insertRank().let {
        launch {
            mainScreen {
                rankScreen {
                    openRenameDialog(it.name) { onCloseSoft() }.assert(empty = false)
                    openRenameDialog(it.name) { onClickCancel() }.assert(empty = false)
                }
            }
        }
    }

    @Test fun renameApplySameName() = data.insertRank().let {
        launch {
            mainScreen {
                rankScreen { openRenameDialog(it.name) { onEnter(it.name, enabled = false) } }
            }
        }
    }

    @Test fun renameApplyFromList() = data.fillRank().let {
        launch {
            mainScreen {
                rankScreen {
                    openRenameDialog(it[0].name, p = 0) { onEnter(it[1].name, enabled = false) }
                }
            }
        }
    }

    @Test fun renameApplyRegister()  = data.insertRank().let {
        launch {
            mainScreen {
                rankScreen { openRenameDialog(it.name) { onEnter(it.name.toUpperCase()) } }
            }
        }
    }

    @Test fun renameResult() = data.insertRank().let {
        val newName = data.uniqueString

        launch {
            mainScreen {
                rankScreen {
                    openRenameDialog(it.name) { onEnter(newName).onClickApply() }
                    openRenameDialog(newName)
                }
            }
        }
    }

}