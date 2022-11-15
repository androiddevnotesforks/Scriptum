package sgtmelon.scriptum.ui.auto.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_1
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_2

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test note dialog for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesNoteDialogTextTest : ParentUiTest() {

    @Test fun textDialogUntitled() = db.insertText(db.textNote.apply { name = "" }).let {
        launch { mainScreen { openNotes { openNoteDialog(it) } } }
    }

    @Test fun textDialogClose() = db.insertText().let {
        launch {
            mainScreen {
                openNotes {
                    openNoteDialog(it) { softClose() }
                    assert(isEmpty = false)
                }
            }
        }
    }

    @Test fun textDialogBind() = db.insertText().let {
        launch {
            mainScreen {
                openNotes {
                    openNoteDialog(it) { bind() }
                    assertItem(it)
                }
            }
        }
    }

    @Test fun textDialogUnbind() = db.insertText(db.textNote.copy(isStatus = true)).let {
        launch {
            mainScreen {
                openNotes {
                    openNoteDialog(it) { bind() }
                    assertItem(it)
                }
            }
        }
    }

    @Test fun textDialogUnbindOnDelete() = with(db) {
        insertText(textNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                openNotes { openNoteDialog(it) { delete() } }
                openBin { openNoteDialog(it) { restore() } }
                openNotes { assertItem(it) }
            }
        }
    }

    @Test fun textDialogConvert() = with(db) {
        insertRoll(rollNote.copy(change = DATE_2))
        return@with insertText(textNote.copy(change = DATE_1))
    }.let {
        launch {
            mainScreen {
                openNotes {
                    var convertItem: NoteItem? = null
                    openNoteDialog(it, p = 1) { convertItem = convert() }
                    assertItem(convertItem!!, p = 0)
                }
            }
        }
    }

    @Test fun textDialogDelete() = db.insertText().let {
        launch {
            mainScreen {
                openNotes {
                    openNoteDialog(it) { delete() }
                    assert(isEmpty = true)
                }
                openBin()
            }
        }
    }


    @Test fun rollDialogUntitled() = db.insertRoll(db.rollNote.apply { name = "" }).let {
        launch { mainScreen { openNotes { openNoteDialog(it) } } }
    }

    @Test fun rollDialogClose() = db.insertRoll().let {
        launch {
            mainScreen {
                openNotes {
                    openNoteDialog(it) { softClose() }
                    assert(isEmpty = false)
                }
            }
        }
    }

    @Test fun rollDialogBind() = db.insertRoll().let {
        launch {
            mainScreen {
                openNotes {
                    openNoteDialog(it) { bind() }
                    assertItem(it)
                }
            }
        }
    }

    @Test fun rollDialogUnbind() = db.insertRoll(db.rollNote.copy(isStatus = true)).let {
        launch {
            mainScreen {
                openNotes {
                    openNoteDialog(it) { bind() }
                    assertItem(it)
                }
            }
        }
    }

    @Test fun rollDialogUnbindOnDelete() = with(db) {
        insertRoll(rollNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                openNotes { openNoteDialog(it) { delete() } }
                openBin { openNoteDialog(it) { restore() } }
                openNotes { assertItem(it) }
            }
        }
    }

    @Test fun rollDialogConvert() = with(db) {
        insertText(textNote.copy(change = DATE_2))
        return@with insertRoll(rollNote.copy(change = DATE_1))
    }.let {
        launch {
            mainScreen {
                openNotes {
                    var convertItem: NoteItem? = null
                    openNoteDialog(it, p = 1) { convertItem = convert() }
                    assertItem(convertItem!!, p = 0)
                }
            }
        }
    }

    @Test fun rollDialogDelete() = db.insertRoll().let {
        launch {
            mainScreen {
                openNotes {
                    openNoteDialog(it) { delete() }
                    assert(isEmpty = true)
                }
                openBin()
            }
        }
    }
}