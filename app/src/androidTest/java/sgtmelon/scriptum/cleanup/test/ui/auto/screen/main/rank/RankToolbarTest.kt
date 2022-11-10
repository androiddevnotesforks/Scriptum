package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test toolbar for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankToolbarTest : ParentUiTest() {

    @Test fun enterAddEmpty() = launch {
        mainScreen {
            rankScreen(isEmpty = true) { toolbar { onEnterName(name = " ", isEnabled = false) } }
        }
    }

    @Test fun enterAddFromList() = db.insertRank().let {
        launch {
            mainScreen { rankScreen { toolbar { onEnterName(it.name, isEnabled = false) } } }
        }
    }

    @Test fun enterAddEnabled() = launch {
        val name = nextString()
        mainScreen { rankScreen(isEmpty = true) { toolbar { onEnterName(name) } } }
    }

    @Test fun enterClear() = launch {
        val name = nextString()

        mainScreen {
            rankScreen(isEmpty = true) {
                toolbar {
                    onEnterName(nextString()).onClickClear()
                    onEnterName(name).onClickAdd()
                }
                openRenameDialog(name) { onCloseSoft() }
            }
        }
    }


    @Test fun enterAddOnEmpty() = launch {
        val name = nextString()

        mainScreen {
            rankScreen(isEmpty = true) {
                toolbar { onEnterName(name).onClickAdd() }
                openRenameDialog(name, p = 0) { onCloseSoft() }

                itemCancel(p = 0)

                toolbar { onEnterName(name).onLongClickAdd() }
                openRenameDialog(name, p = 0)
            }
        }
    }

    @Test fun enterAddStart() = launch({ db.fillRank() }) {
        val name = nextString()

        mainScreen {
            rankScreen {
                scrollTo(Scroll.END)

                toolbar { onEnterName(name).onLongClickAdd() }
                openRenameDialog(name, p = 0) { onCloseSoft() }

                itemCancel(p = 0)

                toolbar { onEnterName(name).onLongClickAdd() }
                openRenameDialog(name, p = 0)
            }
        }
    }

    @Test fun enterAddEnd() = launch({ db.fillRank() }) {
        val name = nextString()

        mainScreen {
            rankScreen {
                toolbar { onEnterName(name).onClickAdd() }
                openRenameDialog(name, p = count - 1) { onCloseSoft() }

                itemCancel(p = count - 1)

                toolbar { onEnterName(name).onClickAdd() }
                openRenameDialog(name, p = count - 1)
            }
        }
    }


    @Test fun updateOnRename() = db.insertRank().let {
        val newName = nextString()

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