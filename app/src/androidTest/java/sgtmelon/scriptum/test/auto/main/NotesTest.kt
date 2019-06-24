package sgtmelon.scriptum.test.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.screen.view.main.NotesFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Тест работы [NotesFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class NotesTest : ParentUiTest() {

    @Test fun contentEmpty() = launch { mainScreen { openNotesPage(empty = true) } }

    @Test fun contentList() = launch({ testData.fillNotes() }) { mainScreen { openNotesPage() } }

    @Test fun openPreference() = launch {
        mainScreen { openNotesPage(empty = true) { openPreference() } }
    }

    @Test fun listScroll() = launch({ testData.fillNotes() }) {
        mainScreen { openNotesPage { onScrollThrough() } }
    }

    @Test fun addFabVisibleOnScroll() = launch({ testData.fillNotes() }) {
        mainScreen {
            openNotesPage { onScroll(Scroll.END, time = 1) }
            assert { onDisplayFab(visible = false) }
            openNotesPage { onScroll(Scroll.START, time = 1) }
            assert { onDisplayFab(visible = true) }
        }
    }

    @Test fun textNoteOpen() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) { onPressBack() }
                    assert(empty = false)
                }
            }
        }
    }

    @Test fun rollNoteOpen() = testData.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(it) { onPressBack() }
                    assert(empty = false)
                }
            }
        }
    }

    @Test fun textNoteCreateAndReturn() = testData.createText().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)
                openAddDialog { createTextNote(it) { onPressBack() } }
                openNotesPage(empty = true)
            }
        }
    }

    @Test fun rollNoteCreateAndReturn() = testData.createRoll().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)
                openAddDialog { createRollNote(it) { onPressBack() } }
                openNotesPage(empty = true)
            }
        }
    }

    @Test fun textNoteCreateAndReturnWithSave() = testData.createText().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openAddDialog {
                    createTextNote(it) {
                        testData.insertText()
                        toolbar { onClickBack() }
                    }
                }

                openNotesPage()
            }
        }
    }

    @Test fun rollNoteCreateAndReturnWithSave() = testData.createRoll().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openAddDialog {
                    createRollNote(it) {
                        testData.insertRoll()
                        toolbar { onClickBack() }
                    }
                }

                openNotesPage()
            }
        }
    }


    @Test fun textNoteDialogOpen() = testData.insertText().let {
        launch { mainScreen { openNotesPage { openNoteDialog(it) } } }
    }

    @Test fun textNoteDialogClose() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onCloseSoft() }
                    assert(empty = false)
                }
            }
        }
    }

    @Test fun textNoteDialogBind() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickBind() }
                    openNoteDialog(it)
                }
            }
        }
    }

    @Test fun textNoteDialogUnbind() = with(testData) {
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

    @Test fun textNoteDialogUnbindOnDelete() = with(testData) {
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

    @Test fun textNoteDialogConvert() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickConvert() }
                    openNoteDialog(it)
                }
            }
        }
    }

    @Test fun textNoteDialogDelete() = testData.insertText().let {
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


    @Test fun rollNoteDialogOpen() = testData.insertRoll().let {
        launch { mainScreen { openNotesPage { openNoteDialog(it) } } }
    }

    @Test fun rollNoteDialogClose() = testData.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onCloseSoft() }
                    assert(empty = false)
                }
            }
        }
    }

    @Test fun rollNoteDialogBind() = testData.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickBind() }
                    openNoteDialog(it)
                }
            }
        }
    }

    @Test fun rollNoteDialogUnbind() = with(testData) {
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

    @Test fun rollNoteDialogUnbindOnDelete() = with(testData) {
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

    @Test fun rollNoteDialogConvert() = testData.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickConvert() }
                    openNoteDialog(it)
                }
            }
        }
    }

    @Test fun rollNoteDialogDelete() = testData.insertRoll().let {
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