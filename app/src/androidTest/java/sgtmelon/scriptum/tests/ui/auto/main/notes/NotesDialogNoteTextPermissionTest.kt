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
class NotesDialogNoteTextPermissionTest : NotesDialogNotePermissionCase(NoteType.TEXT) {

    override fun insert(): NoteItem = db.insertText()

    @Test override fun allow() = super.allow()

    @Test override fun deny() = super.deny()

    @Test override fun denyInfo() = super.denyInfo()

    @Test override fun close() = super.close()

    @Test override fun rotateClose() = super.rotateClose()

    @Test override fun rotateWork() = super.rotateWork()

}