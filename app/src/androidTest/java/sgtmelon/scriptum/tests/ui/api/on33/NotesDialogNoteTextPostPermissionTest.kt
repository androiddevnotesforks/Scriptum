package sgtmelon.scriptum.tests.ui.api.on33

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment

/**
 * Test note dialog permissions for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesDialogNoteTextPostPermissionTest : NotesDialogPostPermissionCase(NoteType.TEXT) {

    override fun insert(): NoteItem = db.insertText()

    @Test override fun allow() = super.allow()

    @Test override fun denyInfo() = super.denyInfo()

    @Test override fun denyInfoClose() = super.denyInfoClose()

    @Test override fun denyInfoRotateClose() = super.denyInfoRotateClose()

    @Test override fun denyInfoRotateWork() = super.denyInfoRotateWork()

}