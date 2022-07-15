package sgtmelon.scriptum.test.ui.auto.screen.main.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test note dialog for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesNoteDialogTest : ParentUiTest() {

    @Test fun textDialogUntitled() = data.insertText(data.textNote.apply { name = "" }).let {
        launch { mainScreen { notesScreen { openNoteDialog(it) } } }
    }

    @Test fun textDialogClose() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onCloseSoft() }.assert(isEmpty = false) }
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
                notesScreen {
                    var convertItem: NoteItem? = null
                    openNoteDialog(it, p = 1) { convertItem = onConvert() }
                    onAssertItem(convertItem!!, p = 0)
                }
            }
        }
    }

    @Test fun textDialogDelete() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onDelete() }.assert(isEmpty = true) }
                binScreen()
            }
        }
    }


    @Test fun rollDialogUntitled() = data.insertRoll(data.rollNote.apply { name = "" }).let {
        launch { mainScreen { notesScreen { openNoteDialog(it) } } }
    }

    @Test fun rollDialogClose() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onCloseSoft() }.assert(isEmpty = false) }
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
                notesScreen {
                    var convertItem: NoteItem? = null
                    openNoteDialog(it, p = 1) { convertItem = onConvert() }
                    onAssertItem(convertItem!!, p = 0)
                }
            }
        }
    }

    @Test fun rollDialogDelete() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onDelete() }.assert(isEmpty = true) }
                binScreen()
            }
        }
    }

}