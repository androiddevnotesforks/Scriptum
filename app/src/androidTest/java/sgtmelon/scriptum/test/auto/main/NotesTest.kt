package sgtmelon.scriptum.test.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.view.main.NotesFragment
import sgtmelon.scriptum.test.ParentTest

/**
 * Тест работы [NotesFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class NotesTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        preference.firstStart = false
    }

    @Test fun contentEmpty() = launch({ testData.clear() }) {
        mainScreen { openNotesPage { assert { onDisplayContent(empty = true) } } }
    }

    @Test fun contentList() = launch({ testData.clear().fillNotes() }) {
        mainScreen { openNotesPage { assert { onDisplayContent(empty = false) } } }
    }

    @Test fun openPreference() = launch {
        mainScreen { openNotesPage { openPreference { assert { onDisplayContent() } } } }
    }

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

    @Test fun textNoteOpen() = launch({ testData.clear().insertText() }) {
        mainScreen {
            openNotesPage {
                assert { onDisplayContent(empty = false) }
                openTextNote { onPressBack() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun rollNoteOpen() = launch({ testData.clear().insertRoll() }) {
        mainScreen {
            openNotesPage {
                assert { onDisplayContent(empty = false) }
                openRollNote() { onPressBack() }
                assert { onDisplayContent(empty = false) }
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
        val noteItem = testData.clear().insertText()

        launch { mainScreen { openNotesPage { openNoteDialog(noteItem) } } }
    }

    @Test fun textNoteDialogClose() {
        val noteItem = testData.clear().insertText()

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onCloseSoft() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun textNoteDialogBind() {
        val noteItem = testData.clear().insertText()

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onClickBind() }
                    noteItem.isStatus = true
                    openNoteDialog(noteItem)
                }
            }
        }
    }

    @Test fun textNoteDialogUnbind() {
        val noteItem = with(testData.clear()) { insertText(textNote.apply { isStatus = true }) }

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onClickUnbind() }
                    noteItem.isStatus = false
                    openNoteDialog(noteItem)
                }
            }
        }
    }

    @Test fun textNoteDialogUnbindOnDelete() {
        val noteItem = with(testData.clear()) { insertText(textNote.apply { isStatus = true }) }

        launch {
            mainScreen {
                openNotesPage { openNoteDialog(noteItem) { onClickDelete() } }

                noteItem.apply {
                    isStatus = false
                    isBin = true
                }

                openBinPage { openNoteDialog(noteItem) { onClickRestore() } }

                noteItem.isBin = false

                openNotesPage { openNoteDialog(noteItem) }
            }
        }
    }

    @Test fun textNoteDialogConvert() {
        val noteItem = testData.clear().insertText()

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onClickConvert(noteItem.type) }
                    noteItem.type = NoteType.ROLL
                    openNoteDialog(noteItem)
                }
            }
        }
    }

    @Test fun textNoteDialogDelete() {
        val noteItem = testData.clear().insertText()

        launch {
            mainScreen {
                openNotesPage {
                    assert { onDisplayContent(empty = false) }
                    openNoteDialog(noteItem) { onClickDelete() }
                    assert { onDisplayContent(empty = true) }
                }

                openBinPage { assert { onDisplayContent(empty = false) } }
            }
        }
    }


    @Test fun rollNoteDialogOpen() {
        val noteItem = testData.clear().insertRoll()

        launch { mainScreen { openNotesPage { openNoteDialog(noteItem) } } }
    }

    @Test fun rollNoteDialogClose() {
        val noteItem = testData.clear().insertRoll()

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onCloseSoft() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun rollNoteDialogCheckAllFromEmpty() {
        val rollList = testData.rollList.apply { forEach { it.isCheck = false } }
        val noteItem = testData.clear().insertRoll(rollList = rollList)

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onClickCheckAll() }
                    noteItem.setCompleteText(rollList.size, rollList.size)
                    openNoteDialog(noteItem)
                }
            }
        }
    }

    @Test fun rollNoteDialogCheckAll() {
        val rollList = testData.rollList.apply {
            forEach { it.isCheck = false }
            get(0).isCheck = true
        }

        val noteItem = testData.clear().insertRoll(rollList = rollList)

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onClickCheckAll() }
                    noteItem.setCompleteText(rollList.size, rollList.size)
                    openNoteDialog(noteItem)
                }
            }
        }
    }

    @Test fun rollNoteDialogUncheckAll() {
        val rollList = testData.rollList.apply { forEach { it.isCheck = true } }
        val noteItem = testData.clear().insertRoll(rollList = rollList)

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onClickUncheck() }
                    noteItem.setCompleteText(0, rollList.size)
                    openNoteDialog(noteItem)
                }
            }
        }
    }

    @Test fun rollNoteDialogBind() {
        val noteItem = testData.clear().insertRoll()

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onClickBind() }
                    noteItem.isStatus = true
                    openNoteDialog(noteItem)
                }
            }
        }
    }

    @Test fun rollNoteDialogUnbind() {
        val noteItem = with(testData) {
            clear()
            return@with insertRoll(rollNote.apply { isStatus = true })
        }

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onClickUnbind() }
                    noteItem.isStatus = false
                    openNoteDialog(noteItem)
                }
            }
        }
    }

    @Test fun rollNoteDialogUnbindOnDelete() {
        val noteItem = with(testData) {
            clear()
            return@with insertRoll(rollNote.apply { isStatus = true })
        }

        launch {
            mainScreen {
                openNotesPage { openNoteDialog(noteItem) { onClickDelete() } }

                noteItem.apply {
                    isStatus = false
                    isBin = true
                }

                openBinPage { openNoteDialog(noteItem) { onClickRestore() } }

                noteItem.isBin = false

                openNotesPage { openNoteDialog(noteItem) }
            }
        }
    }

    @Test fun rollNoteDialogConvert() {
        val noteItem = testData.clear().insertRoll()

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onClickConvert(noteItem.type) }
                    noteItem.type = NoteType.TEXT
                    openNoteDialog(noteItem)
                }
            }
        }
    }

    @Test fun rollNoteDialogDelete() {
        val noteItem = testData.clear().insertRoll()

        launch {
            mainScreen {
                openNotesPage {
                    assert { onDisplayContent(empty = false) }
                    openNoteDialog(noteItem) { onClickDelete() }
                    assert { onDisplayContent(empty = true) }
                }

                openBinPage { assert { onDisplayContent(empty = false) } }
            }
        }
    }

}