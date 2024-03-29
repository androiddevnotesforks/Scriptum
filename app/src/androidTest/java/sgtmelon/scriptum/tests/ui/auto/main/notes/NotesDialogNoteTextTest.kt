package sgtmelon.scriptum.tests.ui.auto.main.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.source.cases.dialog.NotesDialogNoteCase

/**
 * Test note dialog for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesDialogNoteTextTest : NotesDialogNoteCase(NoteType.TEXT) {

    override fun insert(): NoteItem = db.insertText()

    override fun insert(entity: NoteEntity): NoteItem = db.insertText(entity)

    @Test override fun close() = super.close()

    @Test override fun untitled() = super.untitled()

    @Test override fun notification() = super.notification()

    @Test override fun bind() = super.bind()

    @Test override fun unbind() = super.unbind()

    @Test override fun copy() = super.copy()

    @Test override fun delete() = super.delete()

    @Test override fun rotateClose() = super.rotateClose()

    @Test override fun rotateWork() = super.rotateWork()

}