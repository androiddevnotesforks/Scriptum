package sgtmelon.scriptum.tests.ui.control.anim.info

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchRank
import sgtmelon.scriptum.source.ui.tests.launchRankItem
import sgtmelon.test.common.nextString

/**
 * Test of animation info about empty list for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankInfoAnimTest : ParentUiTest() {

    @Test fun onScreen_add_cancel() = launchRank(isEmpty = true) {
        repeat(SWITCH_REPEAT) {
            toolbar { enter(nextString()).addToEnd() }
            assert(isEmpty = false)
            itemCancel()
            assert(isEmpty = true)
        }
    }

    @Test fun onScreen_cancel_restore() = launchRankItem(db.insertRank()) {
        repeat(SWITCH_REPEAT) {
            itemCancel()
            assert(isEmpty = true)
            snackbar { action() }
            assert(isEmpty = false)
        }
    }

    companion object {
        private const val SWITCH_REPEAT = 3
    }
}