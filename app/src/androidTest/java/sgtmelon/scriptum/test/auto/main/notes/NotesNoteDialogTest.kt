package sgtmelon.scriptum.test.auto.main.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.main.NotesFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test note dialog for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesNoteDialogTest : ParentUiTest() {

    @Test fun textDialogClose() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onCloseSoft() }.assert(empty = false) }
            }
        }
    }

    @Test fun textDialogBind() = data.insertText().let {
        launch {
            mainScreen { notesScreen { openNoteDialog(it) { onBind() }.onAssertItem(it) } }
        }
    }

    @Test fun textDialogUnbind() = data.insertText(data.textNote.copy(isStatus = true)).let {
        launch {
            mainScreen { notesScreen { openNoteDialog(it) { onBind() }.onAssertItem(it) } }
        }
    }

    @Test fun textDialogUnbindOnDelete() = with(data) {
        insertText(textNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onDelete() } }
                binScreen { openNoteDialog(it) { onRestore() } }
                notesScreen { onAssertItem(it) }
            }
        }
    }

    @Test fun textDialogConvert() = with(data) {
        insertRoll(rollNote.copy(change = DATE_2))
        return@with insertText(textNote.copy(change = DATE_1))
    }.let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it, p = 1) { onConvert() }.onAssertItem(it, p = 0) }
            }
        }
    }

    @Test fun textDialogDelete() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onDelete() }.assert(empty = true) }
                binScreen()
            }
        }
    }


    @Test fun rollDialogClose() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onCloseSoft() }.assert(empty = false) }
            }
        }
    }

    @Test fun rollDialogBind() = data.insertRoll().let {
        launch {
            mainScreen { notesScreen { openNoteDialog(it) { onBind() }.onAssertItem(it) } }
        }
    }

    @Test fun rollDialogUnbind() = data.insertRoll(data.rollNote.copy(isStatus = true)).let {
        launch {
            mainScreen { notesScreen { openNoteDialog(it) { onBind() }.onAssertItem(it) } }
        }
    }

    @Test fun rollDialogUnbindOnDelete() = with(data) {
        insertRoll(rollNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onDelete() } }
                binScreen { openNoteDialog(it) { onRestore() } }
                notesScreen { onAssertItem(it) }
            }
        }
    }

    @Test fun rollDialogConvert() = with(data) {
        insertText(textNote.copy(change = DATE_2))
        return@with insertRoll(rollNote.copy(change = DATE_1))
    }.let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it, p = 1) { onConvert() }.onAssertItem(it, p = 0) }
            }
        }
    }

    @Test fun rollDialogDelete() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onDelete() }.assert(empty = true) }
                binScreen()
            }
        }
    }

}