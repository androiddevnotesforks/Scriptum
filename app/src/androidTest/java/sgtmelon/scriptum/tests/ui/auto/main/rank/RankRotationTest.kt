package sgtmelon.scriptum.tests.ui.auto.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchRankItem
import sgtmelon.test.common.nextString

/**
 * Test of [RankFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class RankRotationTest : ParentUiRotationTest() {


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