package sgtmelon.scriptum.tests.ui.auto.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.source.ui.screen.main.NotesScreen

import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchMain
import sgtmelon.scriptum.source.ui.tests.launchNotesItem
import sgtmelon.scriptum.source.cases.list.ListContentCase

/**
 * Test of [NotesFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class NotesRotationTest : ParentUiRotationTest(),
    ListContentCase {

    @Test override fun contentEmpty() = launchMain {
        openNotes(isEmpty = true) {
            rotate.toSide()
            assert(isEmpty = true)
        }
        assert(isFabVisible = true)
    }

    @Test override fun contentList() = db.fillNotes().let {
        launchMain {
            openNotes {
                rotate.toSide()
                assert(isEmpty = false)
                assertList(it)
            }
            assert(isFabVisible = true)
        }
    }

    @Test fun textNoteDialog() = launchNotesItem(db.insertText()) {
        openNoteDialog(it) {
            rotate.toSide()
            assert()
        }
    }

    @Test fun rollNoteDialog() = launchNotesItem(db.insertRoll()) {
        openNoteDialog(it) {
            rotate.toSide()
            assert()
        }
    }

    @Test fun dateDialog() = launchNotesItem(db.insertNote()) {
        checkDateRotate(it)
    }

    @Test fun dateDialogReset() = launchNotesItem(db.insertNotification()) {
        checkDateRotate(it)
    }

    private fun NotesScreen.checkDateRotate(it: NoteItem) {
        openNoteDialog(it) {
            notification {
                rotate.toSide()
                assert()
            }
        }
    }

    @Test fun timeDialog() = launchNotesItem(db.insertNote()) {
        openNoteDialog(it) {
            notification {
                applyDate {
                    rotate.toSide()
                    assert()
                }
            }
        }
    }
}