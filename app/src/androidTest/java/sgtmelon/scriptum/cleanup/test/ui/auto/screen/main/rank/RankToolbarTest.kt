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
            openRank(isEmpty = true) { toolbar { enter(name = " ", isGood = false) } }
        }
    }

    @Test fun enterAddFromList() = db.insertRank().let {
        launch {
            mainScreen { openRank { toolbar { enter(it.name, isGood = false) } } }
        }
    }

    @Test fun enterAddEnabled() = launch {
        val name = nextString()
        mainScreen { openRank(isEmpty = true) { toolbar { enter(name) } } }
    }

    @Test fun enterClear() = launch {
        val name = nextString()

        mainScreen {
            openRank(isEmpty = true) {
                toolbar {
                    enter(nextString()).clear()
                    enter(name).addToEnd()
                }
                openRenameDialog(name) { softClose() }
            }
        }
    }


    @Test fun enterAddOnEmpty() = launch {
        val name = nextString()

        mainScreen {
            openRank(isEmpty = true) {
                toolbar { enter(name).addToEnd() }
                openRenameDialog(name, p = 0) { softClose() }

                itemCancel(p = 0)

                toolbar { enter(name).addToStart() }
                openRenameDialog(name, p = 0)
            }
        }
    }

    @Test fun enterAddStart() = launch({ db.fillRank() }) {
        val name = nextString()

        mainScreen {
            openRank {
                scrollTo(Scroll.END)

                toolbar { enter(name).addToStart() }
                openRenameDialog(name, p = 0) { softClose() }

                itemCancel(p = 0)

                toolbar { enter(name).addToStart() }
                openRenameDialog(name, p = 0)
            }
        }
    }

    @Test fun enterAddEnd() = launch({ db.fillRank() }) {
        val name = nextString()

        mainScreen {
            openRank {
                toolbar { enter(name).addToEnd() }
                openRenameDialog(name, p = count - 1) { softClose() }

                itemCancel(p = count - 1)

                toolbar { enter(name).addToEnd() }
                openRenameDialog(name, p = count - 1)
            }
        }
    }


    @Test fun updateOnRename() = db.insertRank().let {
        val newName = nextString()

        launch {
            mainScreen {
                openRank {
                    toolbar {
                        enter(newName)
                        openRenameDialog(it.name) { enter(newName).accept() }
                        assert(isAddEnabled = false)
                    }
                }
            }
        }
    }

}