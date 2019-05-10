package sgtmelon.scriptum.test.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.view.main.NotesFragment
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.screen.main.MainScreen

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

    @Test fun contentEmpty() {
        beforeLaunch { testData.clearAllData() }

        MainScreen { openNotesPage { assert { onDisplayContent(empty = true) } } }
    }

    @Test fun contentList() {
        beforeLaunch { testData.apply { clearAllData() }.fillNotes() }

        MainScreen { openNotesPage { assert { onDisplayContent(empty = false) } } }
    }

    @Test fun openPreference() = afterLaunch {
        MainScreen { openNotesPage { openPreference { assert { onDisplayContent() } } } }
    }

    @Test fun listScroll() {
        beforeLaunch { testData.apply { clearAllData() }.fillNotes(times = 20) }

        MainScreen {
            openNotesPage {
                onScroll(Scroll.END, time = 4)
                onScroll(Scroll.START, time = 4)
            }
        }
    }

    @Test fun addFabVisibleOnScroll() {
        beforeLaunch { testData.apply { clearAllData() }.fillNotes(times = 20) }

        MainScreen {
            assert { onDisplayFab(visible = true) }

            openNotesPage { onScroll(Scroll.END) }

            assert { onDisplayFab(visible = false) }

            openNotesPage { onScroll(Scroll.START) }

            assert { onDisplayFab(visible = true) }
        }
    }

    @Test fun textNoteOpen() {
        beforeLaunch { testData.apply { clearAllData() }.insertText() }

        MainScreen {
            openNotesPage {
                assert { onDisplayContent(empty = false) }
                openTextNote { onPressBack() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun rollNoteOpen() {
        beforeLaunch { testData.apply { clearAllData() }.insertRoll() }

        MainScreen {
            openNotesPage {
                assert { onDisplayContent(empty = false) }
                openRollNote() { onPressBack() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun textNoteCreateAndReturn() {
        beforeLaunch { testData.clearAllData() }

        MainScreen {
            openNotesPage { assert { onDisplayContent(empty = true) } }
            openAddDialog { createTextNote { onPressBack() } }
            openNotesPage { assert { onDisplayContent(empty = true) } }
        }
    }

    @Test fun rollNoteCreateAndReturn() {
        beforeLaunch { testData.clearAllData() }

        MainScreen {
            openNotesPage { assert { onDisplayContent(empty = true) } }
            openAddDialog { createRollNote { onPressBack() } }
            openNotesPage { assert { onDisplayContent(empty = true) } }
        }
    }

    @Test fun textNoteCreateAndReturnWithSave() {
        beforeLaunch { testData.clearAllData() }

        MainScreen {
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

    @Test fun rollNoteCreateAndReturnWithSave() {
        beforeLaunch { testData.clearAllData() }

        MainScreen {
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
        val noteItem = testData.apply { clearAllData() }.insertText()

        afterLaunch { MainScreen { openNotesPage { openNoteDialog(noteItem) } } }
    }

    @Test fun textNoteDialogClose() {
        val noteItem = testData.apply { clearAllData() }.insertText()

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onCloseSoft() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun textNoteDialogBind() {
        val noteItem = testData.apply { clearAllData() }.insertText()

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onClickBind() }
                    noteItem.isStatus = true
                    openNoteDialog(noteItem)
                }
            }
        }
    }

    @Test fun textNoteDialogUnbind() {
        val noteItem = with(testData) {
            clearAllData()
            return@with insertText(textNote.apply { isStatus = true })
        }

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onClickUnbind() }
                    noteItem.isStatus = false
                    openNoteDialog(noteItem)
                }
            }
        }
    }

    @Test fun textNoteDialogUnbindOnDelete() {
        val noteItem = with(testData) {
            clearAllData()
            return@with insertText(textNote.apply { isStatus = true })
        }

        afterLaunch {
            MainScreen {
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
        val noteItem = testData.apply { clearAllData() }.insertText()

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onClickConvert(noteItem.type) }
                    noteItem.type = NoteType.ROLL
                    openNoteDialog(noteItem)
                }
            }
        }
    }

    @Test fun textNoteDialogDelete() {
        val noteItem = testData.apply { clearAllData() }.insertText()

        afterLaunch {
            MainScreen {
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
        val noteItem = testData.apply { clearAllData() }.insertRoll()

        afterLaunch { MainScreen { openNotesPage { openNoteDialog(noteItem) } } }
    }

    @Test fun rollNoteDialogClose() {
        val noteItem = testData.apply { clearAllData() }.insertRoll()

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onCloseSoft() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun rollNoteDialogCheckAllFromEmpty() {
        val listRoll = testData.listRoll.apply { forEach { it.isCheck = false } }
        val noteItem = testData.apply { clearAllData() }.insertRoll(listRoll = listRoll)

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onClickCheckAll() }
                    noteItem.setCompleteText(listRoll.size, listRoll.size)
                    openNoteDialog(noteItem)
                }
            }
        }
    }

    @Test fun rollNoteDialogCheckAll() {
        val listRoll = testData.listRoll.apply {
            forEach { it.isCheck = false }
            get(0).isCheck = true
        }

        val noteItem = testData.apply { clearAllData() }.insertRoll(listRoll = listRoll)

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onClickCheckAll() }
                    noteItem.setCompleteText(listRoll.size, listRoll.size)
                    openNoteDialog(noteItem)
                }
            }
        }
    }

    @Test fun rollNoteDialogUncheckAll() {
        val listRoll = testData.listRoll.apply { forEach { it.isCheck = true } }
        val noteItem = testData.apply { clearAllData() }.insertRoll(listRoll = listRoll)

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onClickUncheck() }
                    noteItem.setCompleteText(0, listRoll.size)
                    openNoteDialog(noteItem)
                }
            }
        }
    }

    @Test fun rollNoteDialogBind() {
        val noteItem = testData.apply { clearAllData() }.insertRoll()

        afterLaunch {
            MainScreen {
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
            clearAllData()
            return@with insertRoll(rollNote.apply { isStatus = true })
        }

        afterLaunch {
            MainScreen {
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
            clearAllData()
            return@with insertRoll(rollNote.apply { isStatus = true })
        }

        afterLaunch {
            MainScreen {
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
        val noteItem = testData.apply { clearAllData() }.insertRoll()

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openNoteDialog(noteItem) { onClickConvert(noteItem.type) }
                    noteItem.type = NoteType.TEXT
                    openNoteDialog(noteItem)
                }
            }
        }
    }

    @Test fun rollNoteDialogDelete() {
        val noteItem = testData.apply { clearAllData() }.insertRoll()

        afterLaunch {
            MainScreen {
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