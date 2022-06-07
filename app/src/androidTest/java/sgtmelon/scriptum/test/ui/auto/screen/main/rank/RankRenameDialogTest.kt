package sgtmelon.scriptum.test.ui.auto.screen.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.extension.toUpperCase
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test rename dialog for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankRenameDialogTest : ParentUiTest() {

    @Test fun dialogClose() = data.insertRank().let {
        launch {
            mainScreen {
                rankScreen {
                    openRenameDialog(it.name) { onCloseSoft() }.assert(isEmpty = false)
                    openRenameDialog(it.name) { onClickCancel() }.assert(isEmpty = false)
                }
            }
        }
    }

    @Test fun dialogApplySameName() = data.insertRank().let {
        launch {
            mainScreen {
                rankScreen { openRenameDialog(it.name) { onEnter(it.name, isEnabled = false) } }
            }
        }
    }

    @Test fun dialogApplyFromList() = data.fillRank().let {
        launch {
            mainScreen {
                rankScreen {
                    openRenameDialog(it[0].name, p = 0) { onEnter(it[1].name, isEnabled = false) }
                }
            }
        }
    }

    @Test fun dialogApplyRegister()  = data.insertRank().let {
        launch {
            mainScreen {
                rankScreen { openRenameDialog(it.name) { onEnter(it.name.uppercase()) } }
            }
        }
    }

    @Test fun dialogResult() = data.insertRank().let {
        val newName = nextString()

        launch {
            mainScreen {
                rankScreen {
                    openRenameDialog(it.name) { onEnter(newName).onClickApply() }
                    it.name = newName
                    onAssertItem(it)
                }
            }
        }
    }

}