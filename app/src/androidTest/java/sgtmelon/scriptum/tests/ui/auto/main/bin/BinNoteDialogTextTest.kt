package sgtmelon.scriptum.tests.ui.auto.main.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment
import sgtmelon.scriptum.source.cases.dialog.BinNoteDialogCase

/**
 * Test note dialog for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinNoteDialogTextTest : BinNoteDialogCase(NoteType.TEXT) {

    override fun insert(): NoteItem = db.insertTextToBin()

    override fun insert(entity: NoteEntity): NoteItem = db.insertTextToBin(entity)

    @Test override fun close() = super.close()

    @Test override fun untitled() = super.untitled()

    @Test override fun restore() = super.restore()

    @Test override fun todo_copy() = super.todo_copy()

    @Test override fun clear() = super.clear()

}