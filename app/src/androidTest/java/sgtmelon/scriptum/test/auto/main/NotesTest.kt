package sgtmelon.scriptum.test.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.view.main.NotesFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Тест работы [NotesFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class NotesTest : ParentUiTest() {

    override fun setUp() {
        super.setUp()

        iPreferenceRepo.firstStart = false
        testData.clear()
    }

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

    @Test fun textNoteOpen() = testData.insertTextNote().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) { onPressBack() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun rollNoteOpen() = testData.insertRollNote().let {
        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(it) { onPressBack() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun textNoteCreateAndReturn() = testData.createTextNote().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)
                openAddDialog { createTextNote(it) { onPressBack() } }
                openNotesPage(empty = true)
            }
        }
    }

    @Test fun rollNoteCreateAndReturn() = testData.createRollNote().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)
                openAddDialog { createRollNote(it) { onPressBack() } }
                openNotesPage(empty = true)
            }
        }
    }

    @Test fun textNoteCreateAndReturnWithSave() = testData.createTextNote().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openAddDialog {
                    createTextNote(it) {
                        testData.insertTextNote()
                        toolbar { onClickBack() }
                    }
                }

                openNotesPage()
            }
        }
    }

    @Test fun rollNoteCreateAndReturnWithSave() = testData.createRollNote().let {
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


    @Test fun textNoteDialogOpen() = testData.insertTextNote().let {
        launch { mainScreen { openNotesPage { openNoteDialog(it) } } }
    }

    @Test fun textNoteDialogClose() = testData.insertTextNote().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onCloseSoft() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun textNoteDialogBind() = testData.insertTextNote().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickBind() }
                    openNoteDialog(it.apply { noteEntity.isStatus = true })
                }
            }
        }
    }

    @Test fun textNoteDialogUnbind() = with(testData) {
        insertTextNote(textNote.apply { isStatus = true })
    }.let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickUnbind() }
                    openNoteDialog(it.apply { noteEntity.isStatus = false })
                }
            }
        }
    }

    @Test fun textNoteDialogUnbindOnDelete() = with(testData) {
        insertTextNote(textNote.apply { isStatus = true })
    }.let {
        launch {
            mainScreen {
                openNotesPage { openNoteDialog(it) { onClickDelete() } }

                it.noteEntity.apply {
                    isStatus = false
                    isBin = true
                }

                openBinPage { openNoteDialog(it) { onClickRestore() } }
                openNotesPage { openNoteDialog(it.apply { noteEntity.isBin = false }) }
            }
        }
    }

    @Test fun textNoteDialogConvert() = testData.insertTextNote().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickConvert() }
                    openNoteDialog(it.apply { noteEntity.type = NoteType.ROLL })
                }
            }
        }
    }

    @Test fun textNoteDialogDelete() = testData.insertTextNote().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickDelete() }
                    assert { onDisplayContent(empty = true) }
                }

                openBinPage()
            }
        }
    }


    @Test fun rollNoteDialogOpen() = testData.insertRollNote().let {
        launch { mainScreen { openNotesPage { openNoteDialog(it) } } }
    }

    @Test fun rollNoteDialogClose() = testData.insertRollNote().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onCloseSoft() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun rollNoteDialogBind() = testData.insertRollNote().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickBind() }
                    openNoteDialog(it.apply { noteEntity.isStatus = true })
                }
            }
        }
    }

    @Test fun rollNoteDialogUnbind() = with(testData) {
        insertRollNote(rollNote.apply { isStatus = true })
    }.let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickUnbind() }
                    openNoteDialog(it.apply { noteEntity.isStatus = false })
                }
            }
        }
    }

    @Test fun rollNoteDialogUnbindOnDelete() = with(testData) {
        insertRollNote(rollNote.apply { isStatus = true })
    }.let {
        launch {
            mainScreen {
                openNotesPage { openNoteDialog(it) { onClickDelete() } }

                it.noteEntity.apply {
                    isStatus = false
                    isBin = true
                }

                openBinPage { openNoteDialog(it) { onClickRestore() } }
                openNotesPage { openNoteDialog(it.apply { noteEntity.isBin = false }) }
            }
        }
    }

    @Test fun rollNoteDialogConvert() = testData.insertRollNote().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickConvert() }
                    openNoteDialog(it.apply { noteEntity.type = NoteType.TEXT })
                }
            }
        }
    }

    @Test fun rollNoteDialogDelete() = testData.insertRollNote().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickDelete() }
                    assert { onDisplayContent(empty = true) }
                }

                openBinPage()
            }
        }
    }

}