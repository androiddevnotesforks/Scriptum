package sgtmelon.scriptum.tests.ui.auto.main.rank

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.dialog.DialogRotateCase
import sgtmelon.scriptum.source.ui.screen.dialogs.RenameDialogUi
import sgtmelon.scriptum.source.ui.screen.main.RankScreen
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchRankItem
import sgtmelon.scriptum.source.ui.tests.launchRankList
import sgtmelon.test.common.nextString

/**
 * Test rename dialog for [RankFragment].
 */
@RunWith(AndroidJUnit4::class)
class RankRenameDialogTest : ParentUiRotationTest(),
    DialogCloseCase,
    DialogRotateCase {

    @Test override fun close() = launchRankItem(db.insertRank()) {
        openRenameDialog(it.name) { softClose() }
        assertItem(it)

        // TODO посмотреть как будет работать после добавления - скрытие клавы до нажатия на кнопку
        //      28.08.23 получил ошибку, что не было произведено нажатие на клавишу cancel.
        openRenameDialog(it.name) { cancel() }
        assertItem(it)
    }

    @Test fun applyEmpty() = launchRankItem(db.insertRank()) {
        openRenameDialog(it.name) { enter(name = "", isEnabled = false) }
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

    @Test override fun rotateClose() = launchRankItem(db.insertRank()) {
        assertRotationClose(it) { softClose() }
        assertRotationClose(it) { cancel() }
    }

    /** Allow to [closeDialog] in different ways. */
    private fun RankScreen.assertRotationClose(it: RankItem, closeDialog: RenameDialogUi.() -> Unit) {
        openRenameDialog(it.name) {
            rotate.switch()
            assert(it.name)
            closeDialog(this)
        }
        assertItem(it)
    }

    @Test override fun rotateWork() = launchRankItem(db.insertRank()) {
        val newName = nextString()
        openRenameDialog(it.name) {
            enter(newName)
            rotate.switch()
            assert(newName)
            apply()
        }
        it.name = newName
        assertItem(it)
    }

    @Test fun rotateSelection() = launchRankItem(db.insertRank()) {
        val newName = nextString()

        openRenameDialog(it.name) {
            /** Check initial selection. */
            assertSelection(it.name)

            /** Check selection and content after rotation (selected all text). */
            rotate.switch()
            assert(it.name)
            assertSelection(it.name)

            /** Enter new name and check selection after rotation (cursor at the end). */
            enter(newName)
            assertSelectionEnd(newName)
            rotate.switch()
            assertSelectionEnd(newName)
        }
    }
}