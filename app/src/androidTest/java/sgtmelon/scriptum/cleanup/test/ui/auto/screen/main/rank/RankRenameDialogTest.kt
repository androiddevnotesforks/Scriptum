package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test rename dialog for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankRenameDialogTest : ParentUiTest() {

    @Test fun dialogClose() = db.insertRank().let {
        launch {
            mainScreen {
                rankScreen {
                    openRenameDialog(it.name) { onCloseSoft() }.assert(isEmpty = false)
                    openRenameDialog(it.name) { onClickCancel() }.assert(isEmpty = false)
                }
            }
        }
    }

    @Test fun dialogApplySameName() = db.insertRank().let {
        launch {
            mainScreen {
                rankScreen { openRenameDialog(it.name) { onEnter(it.name, isEnabled = false) } }
            }
        }
    }

    @Test fun dialogApplyFromList() = db.fillRank().let {
        launch {
            mainScreen {
                rankScreen {
                    openRenameDialog(it[0].name, p = 0) { onEnter(it[1].name, isEnabled = false) }
                }
            }
        }
    }

    @Test fun dialogApplyRegister() = db.insertRank().let {
        launch {
            mainScreen {
                rankScreen { openRenameDialog(it.name) { onEnter(it.name.uppercase()) } }
            }
        }
    }

    @Test fun dialogResult() = db.insertRank().let {
        val newName = nextString()

        launch {
            mainScreen {
                rankScreen {
                    openRenameDialog(it.name) { onEnter(newName).onClickApply() }
                    it.name = newName
                    assertItem(it)
                }
            }
        }
    }

}