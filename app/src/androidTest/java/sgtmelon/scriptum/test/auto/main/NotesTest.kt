package sgtmelon.scriptum.test.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for[NotesFragment]
 */
@RunWith(AndroidJUnit4::class)
class NotesTest : ParentUiTest() {

    @Test fun contentEmpty() = launch { mainScreen { openNotesPage(empty = true) } }

    @Test fun contentList() = launch({ data.fillNotes() }) { mainScreen { openNotesPage() } }

    @Test fun openPreference() = launch {
        mainScreen { openNotesPage(empty = true) { openPreference() } }
    }

    @Test fun listScroll() = launch({ data.fillNotes() }) {
        mainScreen { openNotesPage { onScrollThrough() } }
    }

    @Test fun addFabVisibleOnScroll() = launch({ data.fillNotes() }) {
        mainScreen {
            openNotesPage { onScroll(Scroll.END, time = 1) }
            assert { onDisplayFab(visible = false) }
            openNotesPage { onScroll(Scroll.START, time = 1) }
            assert { onDisplayFab(visible = true) }
        }
    }

    @Test fun textNoteOpen() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) { onPressBack() }
                    assert(empty = false)
                }
            }
        }
    }

    @Test fun rollNoteOpen() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(it) { onPressBack() }
                    assert(empty = false)
                }
            }
        }
    }

    @Test fun textNoteCreateAndReturn() = data.createText().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)
                openAddDialog { createTextNote(it) { onPressBack() } }
                openNotesPage(empty = true)
            }
        }
    }

    @Test fun rollNoteCreateAndReturn() = data.createRoll().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)
                openAddDialog { createRollNote(it) { onPressBack() } }
                openNotesPage(empty = true)
            }
        }
    }

    @Test fun textNoteCreateAndReturnWithSave() = data.createText().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openAddDialog {
                    createTextNote(it) {
                        data.insertText()
                        toolbar { onClickBack() }
                    }
                }

                openNotesPage()
            }
        }
    }

    @Test fun rollNoteCreateAndReturnWithSave() = data.createRoll().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openAddDialog {
                    createRollNote(it) {
                        data.insertRoll()
                        toolbar { onClickBack() }
                    }
                }

                openNotesPage()
            }
        }
    }


    @Test fun textNoteDialogOpen() = data.insertText().let {
        launch { mainScreen { openNotesPage { openNoteDialog(it) } } }
    }

    @Test fun textNoteDialogClose() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onCloseSoft() }
                    assert(empty = false)
                }
            }
        }
    }

    @Test fun textNoteDialogBind() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickBind() }
                    openNoteDialog(it)
                }
            }
        }
    }

    @Test fun textNoteDialogUnbind() = with(data) {
        insertText(textNote.apply { isStatus = true })
    }.let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickUnbind() }
                    openNoteDialog(it)
                }
            }
        }
    }

    @Test fun textNoteDialogUnbindOnDelete() = with(data) {
        insertText(textNote.apply { isStatus = true })
    }.let {
        launch {
            mainScreen {
                openNotesPage { openNoteDialog(it) { onClickDelete() } }
                openBinPage { openNoteDialog(it) { onClickRestore() } }
                openNotesPage { openNoteDialog(it) }
            }
        }
    }

    @Test fun textNoteDialogConvert() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickConvert() }
                    openNoteDialog(it)
                }
            }
        }
    }

    @Test fun textNoteDialogDelete() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickDelete() }
                    assert(empty = true)
                }

                openBinPage()
            }
        }
    }


    @Test fun rollNoteDialogOpen() = data.insertRoll().let {
        launch { mainScreen { openNotesPage { openNoteDialog(it) } } }
    }

    @Test fun rollNoteDialogClose() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onCloseSoft() }
                    assert(empty = false)
                }
            }
        }
    }

    @Test fun rollNoteDialogBind() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickBind() }
                    openNoteDialog(it)
                }
            }
        }
    }

    @Test fun rollNoteDialogUnbind() = with(data) {
        insertRoll(rollNote.apply { isStatus = true })
    }.let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickUnbind() }
                    openNoteDialog(it)
                }
            }
        }
    }

    @Test fun rollNoteDialogUnbindOnDelete() = with(data) {
        insertRoll(rollNote.apply { isStatus = true })
    }.let {
        launch {
            mainScreen {
                openNotesPage { openNoteDialog(it) { onClickDelete() } }
                openBinPage { openNoteDialog(it) { onClickRestore() } }
                openNotesPage { openNoteDialog(it) }
            }
        }
    }

    @Test fun rollNoteDialogConvert() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickConvert() }
                    openNoteDialog(it)
                }
            }
        }
    }

    @Test fun rollNoteDialogDelete() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickDelete() }
                    assert(empty = true)
                }

                openBinPage()
            }
        }
    }

}