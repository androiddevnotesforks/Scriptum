package sgtmelon.scriptum.test.auto.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test keyboard ime click for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankImeTest : ParentUiTest() {

    @Test fun toolbarImeAdd() = launch({ data.fillRank() }) {
        val name = data.uniqueString

        mainScreen {
            rankScreen {
                toolbar { onEnterName(name).onImeOptionClick() }
                openRenameDialog(name, p = count - 1) { onCloseSoft() }

                onClickCancel(p = count - 1)

                toolbar { onEnterName(name).onImeOptionClick() }
                openRenameDialog(name, p = count - 1)
            }
        }
    }

    @Test fun renameImeResult() = data.insertRank().let {
        val newName = data.uniqueString

        launch {
            mainScreen {
                rankScreen {
                    openRenameDialog(it.name) { onEnter(newName).onImeOptionClick() }
                    openRenameDialog(newName)
                }
            }
        }
    }

}