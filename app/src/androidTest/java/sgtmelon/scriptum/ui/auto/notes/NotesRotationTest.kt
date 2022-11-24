package sgtmelon.scriptum.ui.auto.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.parent.ui.screen.main.NotesScreen

import sgtmelon.scriptum.parent.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.ui.cases.list.ListContentCase

/**
 * Test of [NotesFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class NotesRotationTest : ParentUiRotationTest(),
    ListContentCase {

    @Test override fun contentEmpty() = launch {
        mainScreen {
            openNotes(isEmpty = true) {
                rotate.toSide()
                assert(isEmpty = true)
            }
            assert(isFabVisible = true)
        }
    }

    @Test override fun contentList() = db.fillNotes().let {
        launch {
            mainScreen {
                openNotes {
                    rotate.toSide()
                    assert(isEmpty = false)
                    assertList(it)
                }
                assert(isFabVisible = true)
            }
        }
    }

    @Test fun textNoteDialog() = startNotesItemTest(db.insertText()) {
        openNoteDialog(it) {
            rotate.toSide()
            assert()
        }
    }

    @Test fun rollNoteDialog() = startNotesItemTest(db.insertRoll()) {
        openNoteDialog(it) {
            rotate.toSide()
            assert()
        }
    }

    @Test fun dateDialog() = startNotesItemTest(db.insertNote()) {
        checkDateRotate(it)
    }

    @Test fun dateDialogReset() = startNotesItemTest(db.insertNotification()) {
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

    @Test fun timeDialog() = startNotesItemTest(db.insertNote()) {
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