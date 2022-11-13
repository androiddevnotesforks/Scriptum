package sgtmelon.scriptum.ui.auto.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment
import sgtmelon.scriptum.ui.cases.BinNoteDialogCase

/**
 * Test note dialog for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinNoteDialogTextTest : BinNoteDialogCase(NoteType.TEXT) {

    @Test override fun untitled() = super.untitled()

    @Test override fun close() = super.close()

    @Test override fun restore() = super.restore()

    @Test override fun copy() = super.copy()

    @Test override fun clear() = super.clear()

}