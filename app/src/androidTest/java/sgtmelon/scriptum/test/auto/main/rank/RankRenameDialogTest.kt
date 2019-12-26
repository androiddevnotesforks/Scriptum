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
class RankRenameDialogTest : ParentUiTest() {

    @Test fun dialogClose() = data.insertRank().let {
        launch {
            mainScreen {
                rankScreen {
                    openRenameDialog(it.name) { onCloseSoft() }.assert(empty = false)
                    openRenameDialog(it.name) { onClickCancel() }.assert(empty = false)
                }
            }
        }
    }

    @Test fun dialogApplySameName() = data.insertRank().let {
        launch {
            mainScreen {
                rankScreen { openRenameDialog(it.name) { onEnter(it.name, enabled = false) } }
            }
        }
    }

    @Test fun dialogApplyFromList() = data.fillRank().let {
        launch {
            mainScreen {
                rankScreen {
                    openRenameDialog(it[0].name, p = 0) { onEnter(it[1].name, enabled = false) }
                }
            }
        }
    }

    @Test fun dialogApplyRegister()  = data.insertRank().let {
        launch {
            mainScreen {
                rankScreen { openRenameDialog(it.name) { onEnter(it.name.toUpperCase()) } }
            }
        }
    }

    @Test fun dialogResult() = data.insertRank().let {
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