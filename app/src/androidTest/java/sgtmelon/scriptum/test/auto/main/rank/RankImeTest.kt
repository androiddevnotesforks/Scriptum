package sgtmelon.scriptum.test.auto.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
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
                /**
                 * Check ime action with wrong input data. (Empty name)
                 */
                toolbar {
                    onEnterName(name = " ", enabled = false).onImeOptionClick(isSuccess = false)
                }

                toolbar { onEnterName(name).onImeOptionClick() }
                openRenameDialog(name, p = count - 1) { onCloseSoft() }

                /**
                 * Check ime action with wrong input data. (Name from list)
                 */
                toolbar {
                    onEnterName(name, enabled = false).onImeOptionClick(isSuccess = false)
                }

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
                    /**
                     * Check ime action with wrong input data. (Empty name)
                     */
                    openRenameDialog(it.name) {
                        onEnter(name = " ", enabled = false).onImeOptionClick(isSuccess = false)
                        onCloseSoft()
                    }

                    openRenameDialog(it.name) { onEnter(newName).onImeOptionClick() }
                    openRenameDialog(newName)
                }
            }
        }
    }

}