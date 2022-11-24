package sgtmelon.scriptum.ui.auto.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.ui.cases.list.ListContentCase
import sgtmelon.test.common.nextString

/**
 * Test of [RankFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class RankRotationTest : ParentUiRotationTest(),
    ListContentCase {

    @Test override fun contentEmpty() = launch {
        mainScreen {
            openRank(isEmpty = true) {
                rotate.toSide()
                assert(isEmpty = true)
            }
            assert(isFabVisible = false)
        }
    }

    @Test override fun contentList() = db.fillRank().let {
        launch {
            mainScreen {
                openRank {
                    rotate.toSide()
                    assert(isEmpty = false)
                    assertList(it)
                }
                assert(isFabVisible = false)
            }
        }
    }

    @Test fun renameDialog() = startRankItemTest(db.insertRank()) {
        val newName = nextString()
        openRenameDialog(it.name) {
            enter(newName)
            rotate.toSide()
            assert(newName)
        }
    }

    @Test fun snackbar() = startRankItemTest(db.insertRank()) {
        repeat(times = 3) {
            itemCancel()
            rotate.switch()
            snackbar { action() }
            assert(isEmpty = false)
        }
    }
}