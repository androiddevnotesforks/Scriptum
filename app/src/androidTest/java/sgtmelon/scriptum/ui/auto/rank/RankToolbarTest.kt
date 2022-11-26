package sgtmelon.scriptum.ui.auto.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerItemPart
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test toolbar for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankToolbarTest : ParentUiTest() {

    @Test fun addEmpty() = launchSplash {
        mainScreen {
            openRank(isEmpty = true) { toolbar { enter(name = " ", isGood = false) } }
        }
    }

    @Test fun addFromList() = startRankItemTest(db.insertRank()) {
        toolbar { enter(it.name, isGood = false) }
    }

    @Test fun addRegister() = startRankItemTest(db.insertRank()) {
        toolbar { enter(it.name.uppercase(), isGood = false) }
    }

    @Test fun addEnabled() = launchSplash {
        val name = nextString()
        mainScreen { openRank(isEmpty = true) { toolbar { enter(name) } } }
    }

    @Test fun clear() = launchSplash {
        val name = nextString()

        mainScreen {
            openRank(isEmpty = true) {
                toolbar {
                    enter(nextString()).clear()
                    enter(name).addToEnd()
                }
                assertTrue(count == 1)
                openRenameDialog(name) { softClose() }
            }
        }
    }


    @Test fun addOnEmpty() = launchSplash {
        val name = nextString()

        mainScreen {
            openRank(isEmpty = true) {
                toolbar { enter(name).addToEnd() }
                assertTrue(count == 1)
                openRenameDialog(name, p = 0) { softClose() }

                itemCancel(p = 0)
                assertTrue(count == 0)

                toolbar { enter(name).addToStart() }
                assertTrue(count == 1)
                openRenameDialog(name, p = 0)
            }
        }
    }

    @Test fun addStart() = startRankListTest {
        val name = nextString()

        scrollTo(Scroll.END)

        RecyclerItemPart.PREVENT_SCROLL = true
        toolbar { enter(name).addToStart() }
        openRenameDialog(name, p = 0) { softClose() }

        itemCancel(p = 0)

        RecyclerItemPart.PREVENT_SCROLL = true
        toolbar { enter(name).addToStart() }
        openRenameDialog(name, p = 0)
    }

    @Test fun addEnd() = startRankListTest {
        val name = nextString()

        RecyclerItemPart.PREVENT_SCROLL = true
        toolbar { enter(name).addToEnd() }
        openRenameDialog(name, last) { softClose() }

        itemCancel(last)

        RecyclerItemPart.PREVENT_SCROLL = true
        toolbar { enter(name).addToEnd() }
        openRenameDialog(name, last)
    }


    @Test fun updateOnRename() = startRankItemTest(db.insertRank()) {
        val newName = nextString()

        toolbar {
            enter(newName)
            openRenameDialog(it.name) { enter(newName).apply() }
            assert()
        }
    }
}