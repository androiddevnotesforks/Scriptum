package sgtmelon.scriptum.ui.auto.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerItemPart
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.parent.ui.tests.launchRank
import sgtmelon.scriptum.parent.ui.tests.launchRankItem
import sgtmelon.scriptum.parent.ui.tests.launchRankList
import sgtmelon.test.common.nextString

/**
 * Test toolbar for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankToolbarTest : ParentUiTest() {

    @Test fun addEmpty() = launchRank(isEmpty = true) {
        toolbar { enter(name = " ", isGood = false) }
    }

    @Test fun addFromList() = launchRankItem(db.insertRank()) {
        toolbar { enter(it.name, isGood = false) }
    }

    @Test fun addRegister() = launchRankItem(db.insertRank()) {
        toolbar { enter(it.name.uppercase(), isGood = false) }
    }

    @Test fun addEnabled() = launchRank(isEmpty = true) {
        toolbar { enter(nextString()) }
    }

    @Test fun clear() = launchRank(isEmpty = true) {
        val name = nextString()

        toolbar {
            enter(nextString()).clear()
            enter(name).addToEnd()
        }
        assertTrue(count == 1)
        openRenameDialog(name) { softClose() }
    }


    @Test fun addOnEmpty() = launchRank(isEmpty = true) {
        val name = nextString()

        toolbar { enter(name).addToEnd() }
        assertTrue(count == 1)
        openRenameDialog(name, p = 0) { softClose() }

        itemCancel(p = 0)
        assertTrue(count == 0)

        toolbar { enter(name).addToStart() }
        assertTrue(count == 1)
        openRenameDialog(name, p = 0)
    }

    @Test fun addStart() = launchRankList {
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

    @Test fun addEnd() = launchRankList {
        val name = nextString()

        RecyclerItemPart.PREVENT_SCROLL = true
        toolbar { enter(name).addToEnd() }
        openRenameDialog(name, last) { softClose() }

        itemCancel(last)

        RecyclerItemPart.PREVENT_SCROLL = true
        toolbar { enter(name).addToEnd() }
        openRenameDialog(name, last)
    }

    @Test fun addManyAtOnce() = launchRank(isEmpty = true) {
        val list = (1L..5L).map { RankItem(id = it, name = it.toString()) }

        list.forEachIndexed { i, item ->
            toolbar { enter(item.name).addToEnd() }
            assertTrue(count == i + 1)
        }
    }


    @Test fun updateOnRename() = launchRankItem(db.insertRank()) {
        val newName = nextString()

        toolbar {
            enter(newName)
            openRenameDialog(it.name) { enter(newName).apply() }
            assert()
        }
    }
}