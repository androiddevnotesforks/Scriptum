package sgtmelon.scriptum.tests.ui.auto.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchMain
import sgtmelon.scriptum.source.ui.tests.launchRankItem
import sgtmelon.scriptum.source.cases.list.ListContentCase
import sgtmelon.test.common.nextString

/**
 * Test of [RankFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class RankRotationTest : ParentUiRotationTest(),
    ListContentCase {

    @Test override fun contentEmpty() = launchMain {
        openRank(isEmpty = true) {
            rotate.toSide()
            assert(isEmpty = true)
        }
        assert(isFabVisible = false)
    }

    @Test override fun contentList() = db.fillRank().let {
        launchMain {
            openRank {
                rotate.toSide()
                assert(isEmpty = false)
                assertList(it)
            }
            assert(isFabVisible = false)
        }
    }

    @Test fun renameDialog() = launchRankItem(db.insertRank()) {
        val newName = nextString()
        openRenameDialog(it.name) {
            enter(newName)
            rotate.toSide()
            assert(newName)
        }
    }

    @Test fun snackbar() = launchRankItem(db.insertRank()) {
        repeat(times = 3) {
            itemCancel()
            rotate.switch()
            snackbar { action() }
            assert(isEmpty = false)
        }
    }
}