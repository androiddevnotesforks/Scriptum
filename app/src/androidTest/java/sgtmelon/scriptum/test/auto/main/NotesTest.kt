package sgtmelon.scriptum.test.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [NotesFragment]
 */
@RunWith(AndroidJUnit4::class)
class NotesTest : ParentUiTest() {

    @Test fun contentEmpty() = launch { mainScreen { notesScreen(empty = true) } }

    @Test fun contentList() = launch({ data.fillNotes() }) { mainScreen { notesScreen() } }

    @Test fun openPreference() = launch {
        mainScreen { notesScreen(empty = true) { openPreference() } }
    }

    @Test fun listScroll() = launch({ data.fillNotes() }) {
        mainScreen { notesScreen { onScrollThrough() } }
    }

    @Test fun addFabOnScrollAndPageChange() = launch({ data.fillNotes(count = 45) }) {
        mainScreen {
            listOf(MainPage.RANK, MainPage.BIN).forEach { pageTo ->
                notesScreen { onScroll(Scroll.END, time = 5) }
                assert(fabVisible = false)
                notesScreen { onScroll(Scroll.START, time = 1) }
                assert(fabVisible = true)
                notesScreen { onScroll(Scroll.START, time = 2) }
                onNavigateTo(pageTo)
                assert(fabVisible = false)
                onNavigateTo(MainPage.NOTES)
            }
        }
    }

    @Test fun textNoteOpen() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen { openTextNote(it) { onPressBack() }.assert(empty = false) }
            }
        }
    }

    @Test fun rollNoteOpen() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openRollNote(it) { onPressBack() }.assert(empty = false) }
            }
        }
    }

    @Test fun textNoteCreateAndReturn() = data.createText().let {
        launch {
            mainScreen {
                notesScreen(empty = true)
                addDialog { createText(it) { onPressBack() } }
                notesScreen(empty = true)
            }
        }
    }

    @Test fun rollNoteCreateAndReturn() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(empty = true)
                addDialog { createRoll(it) { onPressBack() } }
                notesScreen(empty = true)
            }
        }
    }

    @Test fun textNoteCreateAndReturnWithSave() = data.createText().let {
        launch {
            mainScreen {
                notesScreen(empty = true)

                addDialog {
                    createText(it) {
                        data.insertText()
                        toolbar { onClickBack() }
                    }
                }

                notesScreen()
            }
        }
    }

    @Test fun rollNoteCreateAndReturnWithSave() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(empty = true)

                addDialog {
                    createRoll(it) {
                        data.insertRoll()
                        toolbar { onClickBack() }
                    }
                }

                notesScreen()
            }
        }
    }


    @Test fun textNoteDialogOpen() = data.insertText().let {
        launch { mainScreen { notesScreen { openNoteDialog(it) } } }
    }

    @Test fun textNoteDialogClose() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onCloseSoft() }.assert(empty = false) }
            }
        }
    }

    @Test fun textNoteDialogBind() = data.insertText().let {
        launch {
            mainScreen { notesScreen { openNoteDialog(it) { onBind() }.onAssertItem(it) } }
        }
    }

    @Test fun textNoteDialogUnbind() = data.insertText(data.textNote.copy(isStatus = true)).let {
        launch {
            mainScreen { notesScreen { openNoteDialog(it) { onBind() }.onAssertItem(it) } }
        }
    }

    @Test fun textNoteDialogUnbindOnDelete() = with(data) {
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

    @Test fun textNoteDialogConvert() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onConvert() }.onAssertItem(it) }
            }
        }
    }

    @Test fun textNoteDialogDelete() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onDelete() }.assert(empty = true) }
                binScreen()
            }
        }
    }


    @Test fun rollNoteDialogOpen() = data.insertRoll().let {
        launch { mainScreen { notesScreen { openNoteDialog(it) } } }
    }

    @Test fun rollNoteDialogClose() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onCloseSoft() }.assert(empty = false) }
            }
        }
    }

    @Test fun rollNoteDialogBind() = data.insertRoll().let {
        launch {
            mainScreen { notesScreen { openNoteDialog(it) { onBind() }.onAssertItem(it) } }
        }
    }

    @Test fun rollNoteDialogUnbind() = data.insertRoll(data.rollNote.copy(isStatus = true)).let {
        launch {
            mainScreen { notesScreen { openNoteDialog(it) { onBind() }.onAssertItem(it) } }
        }
    }

    @Test fun rollNoteDialogUnbindOnDelete() = with(data) {
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

    @Test fun rollNoteDialogConvert() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onConvert() }.onAssertItem(it) }
            }
        }
    }

    @Test fun rollNoteDialogDelete() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onDelete() }.assert(empty = true) }
                binScreen()
            }
        }
    }

}