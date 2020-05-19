package sgtmelon.scriptum.test.auto.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.nextString
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.test.ParentUiTest
import kotlin.random.Random

/**
 * Test toolbar for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankToolbarTest : ParentUiTest() {

    @Test fun enterAddEmpty() = launch {
        mainScreen {
            rankScreen(empty = true) { toolbar { onEnterName(name = " ", enabled = false) } }
        }
    }

    @Test fun enterAddFromList() = data.insertRank().let {
        launch {
            mainScreen { rankScreen { toolbar { onEnterName(it.name, enabled = false) } } }
        }
    }

    @Test fun enterAddEnabled() = launch {
        val name = Random.nextString()
        mainScreen { rankScreen(empty = true) { toolbar { onEnterName(name) } } }
    }

    @Test fun enterClear() = launch {
        val name = Random.nextString()

        mainScreen {
            rankScreen(empty = true) {
                toolbar {
                    onEnterName(Random.nextString()).onClickClear()
                    onEnterName(name).onClickAdd()
                }
                openRenameDialog(name) { onCloseSoft() }
            }
        }
    }


    @Test fun enterAddOnEmpty() = launch {
        val name = Random.nextString()

        mainScreen {
            rankScreen(empty = true) {
                toolbar { onEnterName(name).onClickAdd() }
                openRenameDialog(name, p = 0) { onCloseSoft() }

                onClickCancel(p = 0)

                toolbar { onEnterName(name).onLongClickAdd() }
                openRenameDialog(name, p = 0)
            }
        }
    }

    @Test fun enterAddStart() = launch({ data.fillRank() }) {
        val name = Random.nextString()

        mainScreen {
            rankScreen {
                onScroll(Scroll.END)

                toolbar { onEnterName(name).onLongClickAdd() }
                openRenameDialog(name, p = 0) { onCloseSoft() }

                onClickCancel(p = 0)

                toolbar { onEnterName(name).onLongClickAdd() }
                openRenameDialog(name, p = 0)
            }
        }
    }

    @Test fun enterAddEnd() = launch({ data.fillRank() }) {
        val name = Random.nextString()

        mainScreen {
            rankScreen {
                toolbar { onEnterName(name).onClickAdd() }
                openRenameDialog(name, p = count - 1) { onCloseSoft() }

                onClickCancel(p = count - 1)

                toolbar { onEnterName(name).onClickAdd() }
                openRenameDialog(name, p = count - 1)
            }
        }
    }


    @Test fun updateOnRename() = data.insertRank().let {
        val newName = Random.nextString()

        launch {
            mainScreen {
                rankScreen {
                    toolbar {
                        onEnterName(newName)
                        openRenameDialog(it.name) { onEnter(newName).onClickApply() }
                        assert(isAddEnabled = false)
                    }
                }
            }
        }
    }

}