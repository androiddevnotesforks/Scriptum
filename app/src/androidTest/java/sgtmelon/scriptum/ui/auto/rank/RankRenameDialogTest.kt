package sgtmelon.scriptum.ui.auto.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.DialogCloseCase
import sgtmelon.test.common.nextString

/**
 * Test rename dialog for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankRenameDialogTest : ParentUiTest(),
    DialogCloseCase {

    @Test override fun close() = db.insertRank().let {
        launch {
            mainScreen {
                openRank {
                    openRenameDialog(it.name) { softClose() }.assertItem(it)
                    openRenameDialog(it.name) { cancel() }.assertItem(it)
                }
            }
        }
    }

    @Test fun applySameName() = db.insertRank().let {
        launch {
            mainScreen {
                openRank { openRenameDialog(it.name) { enter(it.name, isEnabled = false) } }
            }
        }
    }

    @Test fun applyFromList() = db.fillRank().let {
        launch {
            mainScreen {
                openRank {
                    openRenameDialog(it[0].name, p = 0) { enter(it[1].name, isEnabled = false) }
                }
            }
        }
    }

    @Test fun applyRegister() = db.insertRank().let {
        launch {
            mainScreen {
                openRank {
                    openRenameDialog(it.name) { enter(it.name.uppercase()).apply() }
                    it.name = it.name.uppercase()
                    assertItem(it)
                }
            }
        }
    }

    @Test fun apply() = db.insertRank().let {
        val newName = nextString()

        launch {
            mainScreen {
                openRank {
                    openRenameDialog(it.name) { enter(newName).apply() }
                    it.name = newName
                    assertItem(it)
                }
            }
        }
    }
}