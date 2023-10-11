package sgtmelon.scriptum.tests.ui.auto.main.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.source.cases.dialog.NotesDialogNotePermissionCase

/**
 * Test note dialog permissions for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesDialogNoteRollPermissionTest : NotesDialogNotePermissionCase(NoteType.ROLL) {

    override fun insert(): NoteItem = db.insertRoll()

    @Test override fun allow() = super.allow()

    @Test override fun denyInfo() = super.denyInfo()

    @Test override fun denyInfoClose() = super.denyInfoClose()

    @Test override fun denyInfoRotateClose() = super.denyInfoRotateClose()

    @Test override fun denyInfoRotateWork() = super.denyInfoRotateWork()

}