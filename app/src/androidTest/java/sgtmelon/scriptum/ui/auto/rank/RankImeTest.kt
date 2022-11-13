package sgtmelon.scriptum.ui.auto.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test keyboard ime click for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankImeTest : ParentUiTest() {

    @Test fun toolbarImeAdd() = startRankListTest {
        val name = nextString()

        /** Check ime action with wrong input data (empty name). */
        toolbar { enter(name = " ", isGood = false).imeClick(isSuccess = false) }

        /** Add new item with "name" */
        toolbar { enter(name).imeClick() }
        openRenameDialog(name, last) { softClose() }

        /** Check ime action with wrong input data (name from list). */
        toolbar { enter(name, isGood = false).imeClick(isSuccess = false) }

        /** Remove added item and add it again (check list contains cleanup). */
        closeKeyboard()
        itemCancel(last)
        toolbar { enter(name).imeClick() }
        openRenameDialog(name, last)
    }

    @Test fun renameImeResult() = startRankItemTest(db.insertRank()) {
        val newName = nextString()

        /** Check ime action with wrong input data (empty name). */
        openRenameDialog(it.name) {
            enter(name = " ", isEnabled = false).imeClick(isSuccess = false)
            softClose()
        }

        /** Check ime action with wrong input data (name from list). */
        openRenameDialog(it.name) {
            enter(it.name, isEnabled = false).imeClick(isSuccess = false)
            softClose()
        }

        openRenameDialog(it.name) { enter(newName).imeClick() }
        it.name = newName
        assertItem(it)
    }
}