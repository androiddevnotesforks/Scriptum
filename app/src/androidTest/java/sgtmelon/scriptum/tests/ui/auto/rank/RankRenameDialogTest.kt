package sgtmelon.scriptum.tests.ui.auto.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment

import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchRankItem
import sgtmelon.scriptum.source.ui.tests.launchRankList
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.test.common.nextString

/**
 * Test rename dialog for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankRenameDialogTest : ParentUiTest(),
    DialogCloseCase {

    @Test override fun close() = launchRankItem(db.insertRank()) {
        openRenameDialog(it.name) { softClose() }
        assertItem(it)
        openRenameDialog(it.name) { cancel() }
        assertItem(it)
    }

    @Test fun applySameName() = launchRankItem(db.insertRank()) {
        openRenameDialog(it.name) { enter(it.name, isEnabled = false) }
    }

    @Test fun applyFromList() = launchRankList {
        openRenameDialog(it[0].name, p = 0) { enter(it[1].name, isEnabled = false) }
    }

    @Test fun applyRegisterFromList() = launchRankList {
        openRenameDialog(it[0].name, p = 0) { enter(it[1].name.uppercase(), isEnabled = false) }
    }

    @Test fun applyRegisterSame() = launchRankItem(db.insertRank()) {
        openRenameDialog(it.name) { enter(it.name.uppercase()).apply() }
        it.name = it.name.uppercase()
        assertItem(it)
    }

    @Test fun apply() = launchRankItem(db.insertRank()) {
        val newName = nextString()
        openRenameDialog(it.name) { enter(newName).apply() }
        it.name = newName
        assertItem(it)
    }
}