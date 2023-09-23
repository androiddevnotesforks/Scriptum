package sgtmelon.scriptum.tests.ui.control.anim.info

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchBinItem
import sgtmelon.scriptum.source.ui.tests.launchBinList
import sgtmelon.scriptum.source.ui.tests.launchMain

/**
 * Test of animation info about empty list.
 */
@RunWith(AndroidJUnit4::class)
class BinInfoAnimTest : ParentUiTest() {

    @Test fun onScreen_clearDialog() = launchBinList {
        openClearDialog { positive() }
        assert(isEmpty = true)
    }

    @Test fun onScreen_noteDialog() = launchBinItem(db.insertNoteToBin()) {
        openNoteDialog(it) { clear() }
        assert(isEmpty = true)
    }

    @Test fun onSide_note_restore() = launchBinItem(db.insertNoteToBin()) {
        when (it) {
            is NoteItem.Text -> openText(it) { controlPanel { onRestore() } }
            is NoteItem.Roll -> openRoll(it) { controlPanel { onRestore() } }
        }
        assert(isEmpty = true)
    }

    @Test fun onSide_note_restoreOpen() = launchBinItem(db.insertNoteToBin()) {
        when (it) {
            is NoteItem.Text -> openText(it) {
                controlPanel { onRestoreOpen() }
                clickClose()
            }
            is NoteItem.Roll -> openRoll(it) {
                controlPanel { onRestoreOpen() }
                clickClose()
            }
        }
        assert(isEmpty = true)
    }

    @Test fun onSide_note_deleteForever() = launchBinItem(db.insertNoteToBin()) {
        when (it) {
            is NoteItem.Text -> openText(it) { controlPanel { onClear() } }
            is NoteItem.Roll -> openRoll(it) { controlPanel { onClear() } }
        }
        assert(isEmpty = true)
    }

    @Test fun onSide_note_delete() {
        val item = db.insertNote()

        launchMain {
            openBin(isEmpty = true)
            openNotes(isEmpty = false) {
                when (item) {
                    is NoteItem.Text -> openText(item) { controlPanel { onDelete() } }
                    is NoteItem.Roll -> openRoll(item) { controlPanel { onDelete() } }
                }
            }
            openBin(isEmpty = false)
        }
    }

    @Test fun onSide_notes_delete() {
        val item = db.insertNote()

        launchMain {
            openBin(isEmpty = true)
            openNotes(isEmpty = false) { openNoteDialog(item) { delete() } }
            openBin(isEmpty = false)
        }
    }
}