package sgtmelon.scriptum.tests.ui.control.anim.info

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchMain
import sgtmelon.scriptum.source.ui.tests.launchNotesItem
import sgtmelon.test.common.nextString

/**
 * Test of animation info about empty list for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesInfoAnimTest : ParentUiTest() {

    @Test fun onScreen_noteDialog() = launchNotesItem(db.insertNote()) {
        openNoteDialog(it) { delete() }
        assert(isEmpty = true)
    }

    @Test fun onSide_bin_noteDialog_restore() {
        val item = db.insertNoteToBin()

        launchMain {
            openNotes(isEmpty = true)
            openBin(isEmpty = false) { openNoteDialog(item) { restore() } }
            openNotes(isEmpty = false)
        }
    }

    @Test fun onSide_note_restore() {
        val item = db.insertNoteToBin()

        launchMain {
            openNotes(isEmpty = true)
            openBin {
                when (item) {
                    is NoteItem.Text -> openText(item) { controlPanel { onRestore() } }
                    is NoteItem.Roll -> openRoll(item) { controlPanel { onRestore() } }
                }
                assert(isEmpty = true)
            }
            openNotes(isEmpty = false)
        }
    }

    @Test fun onSide_note_restoreOpen() {
        val item = db.insertNoteToBin()

        launchMain {
            openNotes(isEmpty = true)
            openBin {
                when (item) {
                    is NoteItem.Text -> openText(item) {
                        controlPanel { onRestoreOpen() }
                        clickClose()
                    }

                    is NoteItem.Roll -> openRoll(item) {
                        controlPanel { onRestoreOpen() }
                        clickClose()
                    }
                }
                assert(isEmpty = true)
            }
            openNotes(isEmpty = false)
        }
    }

    @Test fun onSide_note_delete() = launchNotesItem(db.insertNote()) {
        when (it) {
            is NoteItem.Text -> openText(it) { controlPanel { onDelete() } }
            is NoteItem.Roll -> openRoll(it) { controlPanel { onDelete() } }
        }
        assert(isEmpty = true)
    }

    @Test fun onSide_note_create() = launchMain {
        openNotes(isEmpty = true) {
            openAddDialog {
                createText({ db.createText() }) {
                    onEnterText(nextString())
                    controlPanel { onSave() }
                    clickClose()
                }
            }
            assert(isEmpty = false)
        }
    }

    @Test fun onSide_rank_makeVisible() = launchMain(
        before = { db.insertRankForNotes(db.rankEntity.apply { isVisible = false }) }
    ) {
        openNotes(isEmpty = true, isHide = true)
        openRank { itemVisible() }
        openNotes(isEmpty = false)
    }

    @Test fun onSide_rank_makeInvisible() = launchMain(
        before = { db.insertRankForNotes(db.rankEntity.apply { isVisible = true }) }
    ) {
        openNotes(isEmpty = false)
        openRank { itemVisible() }
        openNotes(isEmpty = true, isHide = true)
    }

    @Test fun onSide_rank_deleteRestore() = launchMain(
        before = { db.insertRankForNotes(db.rankEntity.apply { isVisible = false }) }
    ) {
        openNotes(isEmpty = true, isHide = true)
        openRank { itemCancel() }
        openNotes(isEmpty = false)
        openRank(isEmpty = true) { snackbar { action() } }
        openNotes(isEmpty = true, isHide = true)
    }
}