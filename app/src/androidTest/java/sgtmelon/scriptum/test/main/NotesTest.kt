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

        MainScreen { notesScreen { assert { onDisplayContent(empty = true) } } }
    }

    @Test fun contentList() {
        beforeLaunch { testData.apply { clearAllData() }.fillNotes() }

        MainScreen { notesScreen { assert { onDisplayContent(empty = false) } } }
    }

    @Test fun openPreference() = afterLaunch {
        MainScreen { notesScreen { preferenceScreen { assert { onDisplayContent() } } } }
    }

    @Test fun listScroll() {
        beforeLaunch { testData.apply { clearAllData() }.fillNotes(times = 20) }

        MainScreen {
            notesScreen {
                onScroll(Scroll.END, time = 4)
                onScroll(Scroll.START, time = 4)
            }
        }
    }

    @Test fun addFabVisibleOnScroll() {
        beforeLaunch { testData.apply { clearAllData() }.fillNotes(times = 20) }

        MainScreen {
            assert { onDisplayFab(visible = true) }

            notesScreen { onScroll(Scroll.END) }

            assert { onDisplayFab(visible = false) }

            notesScreen { onScroll(Scroll.START) }

            assert { onDisplayFab(visible = true) }
        }
    }

    @Test fun textNoteOpen() {
        beforeLaunch { testData.apply { clearAllData() }.insertText() }

        MainScreen {
            notesScreen {
                assert { onDisplayContent(empty = false) }
                textNoteScreen { onPressBack() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun rollNoteOpen() {
        beforeLaunch { testData.apply { clearAllData() }.insertRoll() }

        MainScreen {
            notesScreen {
                assert { onDisplayContent(empty = false) }
                rollNoteScreen() { onPressBack() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun textNoteCreateAndReturn() {
        beforeLaunch { testData.clearAllData() }

        MainScreen {
            notesScreen { assert { onDisplayContent(empty = true) } }
            addDialogUi { textNoteScreen { onPressBack() } }
            notesScreen { assert { onDisplayContent(empty = true) } }
        }
    }

    @Test fun rollNoteCreateAndReturn() {
        beforeLaunch { testData.clearAllData() }

        MainScreen {
            notesScreen { assert { onDisplayContent(empty = true) } }
            addDialogUi { rollNoteScreen { onPressBack() } }
            notesScreen { assert { onDisplayContent(empty = true) } }
        }
    }

    @Test fun textNoteCreateAndReturnWithSave() {
        beforeLaunch { testData.clearAllData() }

        MainScreen {
            notesScreen { assert { onDisplayContent(empty = true) } }

            addDialogUi {
                textNoteScreen {
                    testData.insertText()
                    toolbar { onClickBack() }
                }
            }

            notesScreen { assert { onDisplayContent(empty = false) } }
        }
    }

    @Test fun rollNoteCreateAndReturnWithSave() {
        beforeLaunch { testData.clearAllData() }

        MainScreen {
            notesScreen { assert { onDisplayContent(empty = true) } }

            addDialogUi {
                rollNoteScreen {
                    testData.insertRoll()
                    toolbar { onClickBack() }
                }
            }

            notesScreen { assert { onDisplayContent(empty = false) } }
        }
    }


    @Test fun textNoteDialogOpen() {
        val noteItem = testData.apply { clearAllData() }.insertText()

        afterLaunch { MainScreen { notesScreen { noteDialogUi(noteItem) } } }
    }

    @Test fun textNoteDialogClose() {
        val noteItem = testData.apply { clearAllData() }.insertText()

        afterLaunch {
            MainScreen {
                notesScreen {
                    noteDialogUi(noteItem) { onCloseSoft() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun textNoteDialogBind() {
        val noteItem = testData.apply { clearAllData() }.insertText()

        afterLaunch {
            MainScreen {
                notesScreen {
                    noteDialogUi(noteItem) { onClickBind() }
                    noteItem.isStatus = true
                    noteDialogUi(noteItem)
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
                notesScreen {
                    noteDialogUi(noteItem) { onClickUnbind() }
                    noteItem.isStatus = false
                    noteDialogUi(noteItem)
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
                notesScreen { noteDialogUi(noteItem) { onClickDelete() } }

                noteItem.apply {
                    isStatus = false
                    isBin = true
                }

                binScreen { noteDialogUi(noteItem) { onClickRestore() } }

                noteItem.isBin = false

                notesScreen { noteDialogUi(noteItem) }
            }
        }
    }

    @Test fun textNoteDialogConvert() {
        val noteItem = testData.apply { clearAllData() }.insertText()

        afterLaunch {
            MainScreen {
                notesScreen {
                    noteDialogUi(noteItem) { onClickConvert(noteItem.type) }
                    noteItem.type = NoteType.ROLL
                    noteDialogUi(noteItem)
                }
            }
        }
    }

    @Test fun textNoteDialogDelete() {
        val noteItem = testData.apply { clearAllData() }.insertText()

        afterLaunch {
            MainScreen {
                notesScreen {
                    assert { onDisplayContent(empty = false) }
                    noteDialogUi(noteItem) { onClickDelete() }
                    assert { onDisplayContent(empty = true) }
                }

                binScreen { assert { onDisplayContent(empty = false) } }
            }
        }
    }


    @Test fun rollNoteDialogOpen() {
        val noteItem = testData.apply { clearAllData() }.insertRoll()

        afterLaunch { MainScreen { notesScreen { noteDialogUi(noteItem) } } }
    }

    @Test fun rollNoteDialogClose() {
        val noteItem = testData.apply { clearAllData() }.insertRoll()

        afterLaunch {
            MainScreen {
                notesScreen {
                    noteDialogUi(noteItem) { onCloseSoft() }
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
                notesScreen {
                    noteDialogUi(noteItem) { onClickCheckAll() }
                    noteItem.setCompleteText(listRoll.size, listRoll.size)
                    noteDialogUi(noteItem)
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
                notesScreen {
                    noteDialogUi(noteItem) { onClickCheckAll() }
                    noteItem.setCompleteText(listRoll.size, listRoll.size)
                    noteDialogUi(noteItem)
                }
            }
        }
    }

    @Test fun rollNoteDialogUncheckAll() {
        val listRoll = testData.listRoll.apply { forEach { it.isCheck = true } }
        val noteItem = testData.apply { clearAllData() }.insertRoll(listRoll = listRoll)

        afterLaunch {
            MainScreen {
                notesScreen {
                    noteDialogUi(noteItem) { onClickUncheck() }
                    noteItem.setCompleteText(0, listRoll.size)
                    noteDialogUi(noteItem)
                }
            }
        }
    }

    @Test fun rollNoteDialogBind() {
        val noteItem = testData.apply { clearAllData() }.insertRoll()

        afterLaunch {
            MainScreen {
                notesScreen {
                    noteDialogUi(noteItem) { onClickBind() }
                    noteItem.isStatus = true
                    noteDialogUi(noteItem)
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
                notesScreen {
                    noteDialogUi(noteItem) { onClickUnbind() }
                    noteItem.isStatus = false
                    noteDialogUi(noteItem)
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
                notesScreen { noteDialogUi(noteItem) { onClickDelete() } }

                noteItem.apply {
                    isStatus = false
                    isBin = true
                }

                binScreen { noteDialogUi(noteItem) { onClickRestore() } }

                noteItem.isBin = false

                notesScreen { noteDialogUi(noteItem) }
            }
        }
    }

    @Test fun rollNoteDialogConvert() {
        val noteItem = testData.apply { clearAllData() }.insertRoll()

        afterLaunch {
            MainScreen {
                notesScreen {
                    noteDialogUi(noteItem) { onClickConvert(noteItem.type) }
                    noteItem.type = NoteType.TEXT
                    noteDialogUi(noteItem)
                }
            }
        }
    }

    @Test fun rollNoteDialogDelete() {
        val noteItem = testData.apply { clearAllData() }.insertRoll()

        afterLaunch {
            MainScreen {
                notesScreen {
                    assert { onDisplayContent(empty = false) }
                    noteDialogUi(noteItem) { onClickDelete() }
                    assert { onDisplayContent(empty = true) }
                }

                binScreen { assert { onDisplayContent(empty = false) } }
            }
        }
    }

}