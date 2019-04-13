package sgtmelon.scriptum.test.main

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.view.main.NotesFragment
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

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

    @Test fun openPreference() = launch {
        MainScreen {
            notesScreen {
                onClickPreference()
                preferenceScreen { assert { onDisplayContent() } }
            }
        }
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

                onClickItem(position = 0)
                TextNoteScreen {
                    assert { onDisplayContent(State.READ) }
                    pressBack()
                }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun rollNoteOpen() {
        beforeLaunch { testData.apply { clearAllData() }.insertRoll() }

        MainScreen {
            notesScreen {
                assert { onDisplayContent(empty = false) }

                onClickItem(position = 0)
                RollNoteScreen {
                    assert { onDisplayContent(State.READ) }
                    pressBack()
                }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun textNoteCreateAndReturn() {
        beforeLaunch { testData.clearAllData() }

        MainScreen {
            notesScreen {
                assert { onDisplayContent(empty = true) }

                onClickFab()
                addDialog {
                    assert { onDisplayContent() }
                    onClickItem(NoteType.TEXT)
                }

                TextNoteScreen {
                    assert { onDisplayContent(State.NEW) }
                    closeSoftKeyboard()
                    pressBack()
                }

                assert { onDisplayContent(empty = true) }
            }
        }
    }

    @Test fun rollNoteCreateAndReturn() {
        beforeLaunch { testData.clearAllData() }

        MainScreen {
            notesScreen {
                assert { onDisplayContent(empty = true) }

                onClickFab()
                addDialog {
                    assert { onDisplayContent() }
                    onClickItem(NoteType.ROLL)
                }

                RollNoteScreen {
                    assert { onDisplayContent(State.NEW) }
                    closeSoftKeyboard()
                    pressBack()
                }

                assert { onDisplayContent(empty = true) }
            }
        }
    }

    @Test fun textNoteCreateAndReturnWithSave() {
        beforeLaunch { testData.clearAllData() }

        MainScreen {
            notesScreen {
                assert { onDisplayContent(empty = true) }

                onClickFab()
                addDialog {
                    assert { onDisplayContent() }
                    onClickItem(NoteType.TEXT)
                }

                TextNoteScreen {
                    assert { onDisplayContent(State.NEW) }

                    testData.insertText()

                    closeSoftKeyboard()
                    pressBack()
                }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun rollNoteCreateAndReturnWithSave() {
        beforeLaunch { testData.clearAllData() }

        MainScreen {
            notesScreen {
                assert { onDisplayContent(empty = true) }

                onClickFab()
                addDialog {
                    assert { onDisplayContent() }
                    onClickItem(NoteType.ROLL)
                }

                RollNoteScreen {
                    assert { onDisplayContent(State.NEW) }

                    testData.insertRoll()

                    closeSoftKeyboard()
                    pressBack()
                }

                assert { onDisplayContent(empty = false) }
            }
        }
    }


    @Test fun textNoteDialogOpen() {
        val noteItem = testData.apply { clearAllData() }.insertText()

        launch()

        MainScreen {
            notesScreen {
                onLongClickItem(position = 0)
                noteDialog { assert { onDisplayContent(noteItem) } }
            }
        }
    }

    @Test fun textNoteDialogClose() {
        beforeLaunch { testData.apply { clearAllData() }.insertText() }

        MainScreen {
            notesScreen {
                onLongClickItem(position = 0)
                noteDialog { onCloseSoft() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun textNoteDialogBind() {
        val noteItem = testData.apply { clearAllData() }.insertText()

        launch()

        MainScreen {
            notesScreen {
                onLongClickItem(position = 0)
                noteDialog {
                    assert { onDisplayContent(noteItem) }
                    onClickBind()
                }

                noteItem.isStatus = true

                onLongClickItem(position = 0)
                noteDialog { assert { onDisplayContent(noteItem) } }
            }
        }
    }

    @Test fun textNoteDialogUnbind() {
        val noteItem = with(testData) {
            clearAllData()
            return@with insertText(textNote.apply { isStatus = true })
        }

        launch()

        MainScreen {
            notesScreen {
                onLongClickItem(position = 0)
                noteDialog {
                    assert { onDisplayContent(noteItem) }
                    onClickUnbind()
                }

                noteItem.isStatus = false

                onLongClickItem(position = 0)
                noteDialog { assert { onDisplayContent(noteItem) } }
            }
        }
    }

    @Test fun textNoteDialogConvert() {
        val noteItem = testData.apply { clearAllData() }.insertText()

        launch()

        MainScreen {
            notesScreen {
                onClickItem(position = 0)
                TextNoteScreen {
                    assert { onDisplayContent(State.READ) }
                    pressBack()
                }

                onLongClickItem(position = 0)
                noteDialog {
                    assert { onDisplayContent(noteItem) }
                    onClickConvert(noteItem.type)
                }

                noteItem.type = NoteType.ROLL

                onClickItem(position = 0)
                RollNoteScreen {
                    assert { onDisplayContent(State.READ) }
                    pressBack()
                }

                onLongClickItem(position = 0)
                noteDialog { assert { onDisplayContent(noteItem) } }
            }
        }
    }

    @Test fun textNoteDialogDelete() {
        beforeLaunch { testData.apply { clearAllData() }.insertText() }

        MainScreen {
            notesScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem(position = 0)
                noteDialog { onClickDelete() }

                assert { onDisplayContent(empty = true) }
            }

            navigateTo(MainPage.BIN)

            binScreen { assert { onDisplayContent(empty = false) } }
        }
    }


    @Test fun rollNoteDialogOpen() {
        val noteItem = testData.apply { clearAllData() }.insertRoll()

        launch()

        MainScreen {
            notesScreen {
                onLongClickItem(position = 0)
                noteDialog { assert { onDisplayContent(noteItem) } }
            }
        }
    }

    @Test fun rollNoteDialogClose() {
        beforeLaunch { testData.apply { clearAllData() }.insertRoll() }

        MainScreen {
            notesScreen {
                onLongClickItem(position = 0)
                noteDialog { onCloseSoft() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun rollNoteDialogCheckAllFromEmpty() {
        val listRoll = testData.listRoll.apply { forEach { it.isCheck = false } }
        val noteItem = testData.apply { clearAllData() }.insertRoll(listRoll = listRoll)

        launch()

        MainScreen {
            notesScreen {
                onLongClickItem(position = 0)
                noteDialog {
                    assert { onDisplayContent(noteItem) }
                    onClickCheckAll()
                }

                noteItem.setCompleteText(listRoll.size, listRoll.size)

                onLongClickItem(position = 0)
                noteDialog { assert { onDisplayContent(noteItem) } }
            }
        }
    }

    @Test fun rollNoteDialogCheckAll() {
        val listRoll = testData.listRoll.apply {
            forEach { it.isCheck = false }
            get(0).isCheck = true
        }

        val noteItem = testData.apply { clearAllData() }.insertRoll(listRoll = listRoll)

        launch()

        MainScreen {
            notesScreen {
                onLongClickItem(position = 0)
                noteDialog {
                    assert { onDisplayContent(noteItem) }
                    onClickCheckAll()
                }

                noteItem.setCompleteText(listRoll.size, listRoll.size)

                onLongClickItem(position = 0)
                noteDialog { assert { onDisplayContent(noteItem) } }
            }
        }
    }

    @Test fun rollNoteDialogUncheckAll() {
        val listRoll = testData.listRoll.apply { forEach { it.isCheck = true } }
        val noteItem = testData.apply { clearAllData() }.insertRoll(listRoll = listRoll)

        launch()

        MainScreen {
            notesScreen {
                onLongClickItem(position = 0)
                noteDialog {
                    assert { onDisplayContent(noteItem) }
                    onClickUncheck()
                }

                noteItem.setCompleteText(0, listRoll.size)

                onLongClickItem(position = 0)
                noteDialog { assert { onDisplayContent(noteItem) } }
            }
        }
    }

    @Test fun rollNoteDialogBind() {
        val noteItem = testData.apply { clearAllData() }.insertRoll()

        launch()

        MainScreen {
            notesScreen {
                onLongClickItem(position = 0)
                noteDialog {
                    assert { onDisplayContent(noteItem) }
                    onClickBind()
                }

                noteItem.isStatus = true

                onLongClickItem(position = 0)
                noteDialog { assert { onDisplayContent(noteItem) } }
            }
        }
    }

    @Test fun rollNoteDialogUnbind() {
        val noteItem = with(testData) {
            clearAllData()
            return@with insertRoll(rollNote.apply { isStatus = true })
        }

        launch()

        MainScreen {
            notesScreen {
                onLongClickItem(position = 0)
                noteDialog {
                    assert { onDisplayContent(noteItem) }
                    onClickUnbind()
                }

                noteItem.isStatus = false

                onLongClickItem(position = 0)
                noteDialog { assert { onDisplayContent(noteItem) } }
            }
        }
    }

    @Test fun rollNoteDialogConvert() {
        val noteItem = testData.apply { clearAllData() }.insertRoll()

        launch()

        MainScreen {
            notesScreen {
                onClickItem(position = 0)
                RollNoteScreen {
                    assert { onDisplayContent(State.READ) }
                    pressBack()
                }

                onLongClickItem(position = 0)
                noteDialog {
                    assert { onDisplayContent(noteItem) }
                    onClickConvert(noteItem.type)
                }

                noteItem.type = NoteType.TEXT

                onClickItem(position = 0)
                TextNoteScreen {
                    assert { onDisplayContent(State.READ) }
                    pressBack()
                }

                onLongClickItem(position = 0)
                noteDialog { assert { onDisplayContent(noteItem) } }
            }
        }
    }

    @Test fun rollNoteDialogDelete() {
        testData.apply { clearAllData() }.insertRoll()

        launch()

        MainScreen {
            notesScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem(position = 0)
                noteDialog { onClickDelete() }

                assert { onDisplayContent(empty = true) }
            }

            navigateTo(MainPage.BIN)

            binScreen { assert { onDisplayContent(empty = false) } }
        }

    }

}