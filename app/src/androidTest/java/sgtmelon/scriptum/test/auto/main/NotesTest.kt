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
    }

    @Test fun contentEmpty() = launch({ testData.clear() }) {
        mainScreen { openNotesPage { assert { onDisplayContent(empty = true) } } }
    }

    @Test fun contentList() = launch({ testData.clear().fillNotes() }) {
        mainScreen { openNotesPage { assert { onDisplayContent(empty = false) } } }
    }

    @Test fun openPreference() = launch { mainScreen { openNotesPage { openPreference() } } }

    @Test fun listScroll() = launch({ testData.clear().fillNotes(times = 20) }) {
        mainScreen {
            openNotesPage {
                onScroll(Scroll.END, time = 4)
                onScroll(Scroll.START, time = 4)
            }
        }
    }

    @Test fun addFabVisibleOnScroll() = launch({ testData.clear().fillNotes(times = 20) }) {
        mainScreen {
            assert { onDisplayFab(visible = true) }
            openNotesPage { onScroll(Scroll.END) }
            assert { onDisplayFab(visible = false) }
            openNotesPage { onScroll(Scroll.START) }
            assert { onDisplayFab(visible = true) }
        }
    }

    @Test fun textNoteOpen() {
        val noteEntity = testData.clear().insertText()

        launch {
            mainScreen {
                openNotesPage {
                    assert { onDisplayContent(empty = false) }
                    openTextNote(noteEntity) { onPressBack() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun rollNoteOpen() {
        val noteEntity = testData.clear().insertRoll()

        launch {
            mainScreen {
                openNotesPage {
                    assert { onDisplayContent(empty = false) }
                    openRollNote(noteEntity) { onPressBack() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun textNoteCreateAndReturn() = launch({ testData.clear() }) {
        mainScreen {
            openNotesPage { assert { onDisplayContent(empty = true) } }
            openAddDialog { createTextNote { onPressBack() } }
            openNotesPage { assert { onDisplayContent(empty = true) } }
        }
    }

    @Test fun rollNoteCreateAndReturn() = launch({ testData.clear() }) {
        mainScreen {
            openNotesPage { assert { onDisplayContent(empty = true) } }
            openAddDialog { createRollNote { onPressBack() } }
            openNotesPage { assert { onDisplayContent(empty = true) } }
        }
    }

    @Test fun textNoteCreateAndReturnWithSave() = launch({ testData.clear() }) {
        mainScreen {
            openNotesPage { assert { onDisplayContent(empty = true) } }

            openAddDialog {
                createTextNote {
                    testData.insertText()
                    toolbar { onClickBack() }
                }
            }

            openNotesPage { assert { onDisplayContent(empty = false) } }
        }
    }

    @Test fun rollNoteCreateAndReturnWithSave() = launch({ testData.clear() }) {
        mainScreen {
            openNotesPage { assert { onDisplayContent(empty = true) } }

            openAddDialog {
                createRollNote {
                    testData.insertRoll()
                    toolbar { onClickBack() }
                }
            }

            openNotesPage { assert { onDisplayContent(empty = false) } }
        }
    }


    @Test fun textNoteDialogOpen() {
        val noteEntity = testData.clear().insertText()

        launch { mainScreen { openNotesPage { openNoteDialog(noteEntity) } } }
    }

    @Test fun textNoteDialogClose() {
        val noteEntity = testData.clear().insertText()

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteEntity) { onCloseSoft() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun textNoteDialogBind() {
        val noteEntity = testData.clear().insertText()

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteEntity) { onClickBind() }
                    noteEntity.isStatus = true
                    openNoteDialog(noteEntity)
                }
            }
        }
    }

    @Test fun textNoteDialogUnbind() {
        val noteEntity = with(testData.clear()) { insertText(textNote.apply { isStatus = true }) }

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteEntity) { onClickUnbind() }
                    noteEntity.isStatus = false
                    openNoteDialog(noteEntity)
                }
            }
        }
    }

    @Test fun textNoteDialogUnbindOnDelete() {
        val noteEntity = with(testData.clear()) { insertText(textNote.apply { isStatus = true }) }

        launch {
            mainScreen {
                openNotesPage { openNoteDialog(noteEntity) { onClickDelete() } }

                noteEntity.apply {
                    isStatus = false
                    isBin = true
                }

                openBinPage { openNoteDialog(noteEntity) { onClickRestore() } }

                noteEntity.isBin = false

                openNotesPage { openNoteDialog(noteEntity) }
            }
        }
    }

    @Test fun textNoteDialogConvert() {
        val noteEntity = testData.clear().insertText()

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteEntity) { onClickConvert(noteEntity.type) }
                    noteEntity.type = NoteType.ROLL
                    openNoteDialog(noteEntity)
                }
            }
        }
    }

    @Test fun textNoteDialogDelete() {
        val noteEntity = testData.clear().insertText()

        launch {
            mainScreen {
                openNotesPage {
                    assert { onDisplayContent(empty = false) }
                    openNoteDialog(noteEntity) { onClickDelete() }
                    assert { onDisplayContent(empty = true) }
                }

                openBinPage { assert { onDisplayContent(empty = false) } }
            }
        }
    }


    @Test fun rollNoteDialogOpen() {
        val noteEntity = testData.clear().insertRoll()

        launch { mainScreen { openNotesPage { openNoteDialog(noteEntity) } } }
    }

    @Test fun rollNoteDialogClose() {
        val noteEntity = testData.clear().insertRoll()

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteEntity) { onCloseSoft() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun rollNoteDialogBind() {
        val noteEntity = testData.clear().insertRoll()

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteEntity) { onClickBind() }
                    noteEntity.isStatus = true
                    openNoteDialog(noteEntity)
                }
            }
        }
    }

    @Test fun rollNoteDialogUnbind() {
        val noteEntity = with(testData) {
            clear()
            return@with insertRoll(rollNote.apply { isStatus = true })
        }

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteEntity) { onClickUnbind() }
                    noteEntity.isStatus = false
                    openNoteDialog(noteEntity)
                }
            }
        }
    }

    @Test fun rollNoteDialogUnbindOnDelete() {
        val noteEntity = with(testData) {
            clear()
            return@with insertRoll(rollNote.apply { isStatus = true })
        }

        launch {
            mainScreen {
                openNotesPage { openNoteDialog(noteEntity) { onClickDelete() } }

                noteEntity.apply {
                    isStatus = false
                    isBin = true
                }

                openBinPage { openNoteDialog(noteEntity) { onClickRestore() } }

                noteEntity.isBin = false

                openNotesPage { openNoteDialog(noteEntity) }
            }
        }
    }

    @Test fun rollNoteDialogConvert() {
        val noteEntity = testData.clear().insertRoll()

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteEntity) { onClickConvert(noteEntity.type) }
                    noteEntity.type = NoteType.TEXT
                    openNoteDialog(noteEntity)
                }
            }
        }
    }

    @Test fun rollNoteDialogDelete() {
        val noteEntity = testData.clear().insertRoll()

        launch {
            mainScreen {
                openNotesPage {
                    assert { onDisplayContent(empty = false) }
                    openNoteDialog(noteEntity) { onClickDelete() }
                    assert { onDisplayContent(empty = true) }
                }

                openBinPage { assert { onDisplayContent(empty = false) } }
            }
        }
    }

}