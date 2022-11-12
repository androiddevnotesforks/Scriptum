package sgtmelon.scriptum.ui.auto.rank

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

    @Test fun addEmpty() = launch {
        mainScreen {
            openRank(isEmpty = true) { toolbar { enter(name = " ", isGood = false) } }
        }
    }

    @Test fun addFromList() = db.insertRank().let {
        launch {
            mainScreen { openRank { toolbar { enter(it.name, isGood = false) } } }
        }
    }

    @Test fun addRegister() = db.insertRank().let {
        launch {
            mainScreen { openRank { toolbar { enter(it.name.uppercase(), isGood = false) } } }
        }
    }

    @Test fun addEnabled() = launch {
        val name = nextString()
        mainScreen { openRank(isEmpty = true) { toolbar { enter(name) } } }
    }

    @Test fun clear() = launch {
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


    @Test fun addOnEmpty() = launch {
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

    @Test fun addStart() = launch({ db.fillRank() }) {
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

    @Test fun addEnd() = launch({ db.fillRank() }) {
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
                    toolbar { enter(newName) }
                    openRenameDialog(it.name) { enter(newName).apply() }
                    toolbar { assert() }
                }
            }
        }
    }
}