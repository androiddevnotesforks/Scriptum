package sgtmelon.scriptum.tests.ui.auto.main.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.source.ui.screen.main.NotesScreen
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchNotesItem

/**
 * Test of [NotesFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class NotesRotationTest : ParentUiRotationTest() {

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