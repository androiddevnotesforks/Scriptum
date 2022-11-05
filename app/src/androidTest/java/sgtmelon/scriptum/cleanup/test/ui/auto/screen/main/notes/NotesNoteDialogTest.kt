package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_1
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_2
import sgtmelon.scriptum.ui.testing.parent.ParentUiTest

/**
 * Test note dialog for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesNoteDialogTest : ParentUiTest() {

    @Test fun textDialogUntitled() = db.insertText(db.textNote.apply { name = "" }).let {
        launch { mainScreen { notesScreen { openNoteDialog(it) } } }
    }

    @Test fun textDialogClose() = db.insertText().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onCloseSoft() }.assert(isEmpty = false) }
            }
        }
    }

    @Test fun textDialogBind() = db.insertText().let {
        launch {
            mainScreen { notesScreen { openNoteDialog(it) { onBind() }.onAssertItem(it) } }
        }
    }

    @Test fun textDialogUnbind() = db.insertText(db.textNote.copy(isStatus = true)).let {
        launch {
            mainScreen { notesScreen { openNoteDialog(it) { onBind() }.onAssertItem(it) } }
        }
    }

    @Test fun textDialogUnbindOnDelete() = with(db) {
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

    @Test fun textDialogConvert() = with(db) {
        insertRoll(rollNote.copy(change = DATE_2))
        return@with insertText(textNote.copy(change = DATE_1))
    }.let {
        launch {
            mainScreen {
                notesScreen {
                    var convertItem: NoteItem? = null
                    openNoteDialog(it, p = 1) { convertItem = onConvert() }
                    onAssertItem(convertItem!!, p = 0)
                }
            }
        }
    }

    @Test fun textDialogDelete() = db.insertText().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onDelete() }.assert(isEmpty = true) }
                binScreen()
            }
        }
    }


    @Test fun rollDialogUntitled() = db.insertRoll(db.rollNote.apply { name = "" }).let {
        launch { mainScreen { notesScreen { openNoteDialog(it) } } }
    }

    @Test fun rollDialogClose() = db.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onCloseSoft() }.assert(isEmpty = false) }
            }
        }
    }

    @Test fun rollDialogBind() = db.insertRoll().let {
        launch {
            mainScreen { notesScreen { openNoteDialog(it) { onBind() }.onAssertItem(it) } }
        }
    }

    @Test fun rollDialogUnbind() = db.insertRoll(db.rollNote.copy(isStatus = true)).let {
        launch {
            mainScreen { notesScreen { openNoteDialog(it) { onBind() }.onAssertItem(it) } }
        }
    }

    @Test fun rollDialogUnbindOnDelete() = with(db) {
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

    @Test fun rollDialogConvert() = with(db) {
        insertText(textNote.copy(change = DATE_2))
        return@with insertRoll(rollNote.copy(change = DATE_1))
    }.let {
        launch {
            mainScreen {
                notesScreen {
                    var convertItem: NoteItem? = null
                    openNoteDialog(it, p = 1) { convertItem = onConvert() }
                    onAssertItem(convertItem!!, p = 0)
                }
            }
        }
    }

    @Test fun rollDialogDelete() = db.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onDelete() }.assert(isEmpty = true) }
                binScreen()
            }
        }
    }

}