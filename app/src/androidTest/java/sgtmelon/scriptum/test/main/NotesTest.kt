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
import sgtmelon.scriptum.ui.dialog.AddDialog
import sgtmelon.scriptum.ui.dialog.NoteDialog
import sgtmelon.scriptum.ui.screen.PreferenceScreen
import sgtmelon.scriptum.ui.screen.main.BinScreen
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.main.NotesScreen
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

        MainScreen { NotesScreen { assert { onDisplayContent(empty = true) } } }
    }

    @Test fun contentList() {
        beforeLaunch { testData.apply { clearAllData() }.fillNotes() }

        MainScreen { NotesScreen { assert { onDisplayContent(empty = false) } } }
    }

    @Test fun openPreference() = launch {
        MainScreen {
            NotesScreen {
                onClickPreference()
                PreferenceScreen { assert { onDisplayContent() } }
            }
        }
    }

    @Test fun addFabVisibleOnScroll() {
        beforeLaunch { testData.apply { clearAllData() }.fillNotes(times = 20) }

        MainScreen {
            assert { onDisplayFab(visible = true) }

            NotesScreen { onScroll(Scroll.END, time = 4) }

            assert { onDisplayFab(visible = false) }

            NotesScreen { onScroll(Scroll.START, time = 4) }

            assert { onDisplayFab(visible = true) }
        }
    }

    @Test fun textNoteOpen() {
        beforeLaunch { testData.apply { clearAllData() }.insertText() }

        MainScreen {
            NotesScreen {
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
            NotesScreen {
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
            NotesScreen {
                assert { onDisplayContent(empty = true) }

                onClickFab()
                AddDialog {
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
            NotesScreen {
                assert { onDisplayContent(empty = true) }

                onClickFab()
                AddDialog {
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
            NotesScreen {
                assert { onDisplayContent(empty = true) }

                onClickFab()
                AddDialog {
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
            NotesScreen {
                assert { onDisplayContent(empty = true) }

                onClickFab()
                AddDialog {
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
            NotesScreen {
                onLongClickItem(position = 0)
                NoteDialog {
                    assert { onDisplayContent(noteItem) }
                    pressBack()
                }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun textNoteDialogBind() {
        TODO("not create")
    }

    @Test fun textNoteDialogUnbind() {
        TODO("not create")
    }

    @Test fun textNoteDialogConvert() {
        val noteItem = testData.apply { clearAllData() }.insertText()

        launch()

        MainScreen {
            NotesScreen {
                onClickItem(position = 0)
                TextNoteScreen {
                    assert { onDisplayContent(State.READ) }
                    pressBack()
                }

                onLongClickItem(position = 0)
                NoteDialog {
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
                NoteDialog { assert { onDisplayContent(noteItem) } }
            }
        }
    }

    @Test fun textNoteDialogCopy() {
        TODO("not create")
    }

    @Test fun textNoteDialogDelete() {
        beforeLaunch { testData.apply { clearAllData() }.insertText() }

        MainScreen {
            NotesScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem(position = 0)
                NoteDialog { onClickDelete() }

                assert { onDisplayContent(empty = true) }
            }

            navigateTo(MainPage.Name.BIN)

            BinScreen { assert { onDisplayContent(empty = false) } }
        }
    }


    @Test fun rollNoteDialogOpen() {
        val noteItem = testData.apply { clearAllData() }.insertRoll()

        launch()

        MainScreen {
            NotesScreen {
                onLongClickItem(position = 0)
                NoteDialog {
                    assert { onDisplayContent(noteItem) }
                    pressBack()
                }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun rollNoteDialogCheckAll() {
        TODO("not create")
    }

    @Test fun rollNoteDialogUncheckAll() {
        TODO("not create")
    }

    @Test fun rollNoteDialogBind() {
        TODO("not create")
    }

    @Test fun rollNoteDialogUnbind() {
        TODO("not create")
    }

    @Test fun rollNoteDialogConvert() {
        val noteItem = testData.apply { clearAllData() }.insertRoll()

        launch()

        MainScreen {
            NotesScreen {
                onClickItem(position = 0)
                RollNoteScreen {
                    assert { onDisplayContent(State.READ) }
                    pressBack()
                }

                onLongClickItem(position = 0)
                NoteDialog {
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
                NoteDialog { assert { onDisplayContent(noteItem) } }
            }
        }
    }

    @Test fun rollNoteDialogCopy() {
        TODO("not create")
    }

    @Test fun rollNoteDialogDelete() {
        testData.apply { clearAllData() }.insertRoll()

        launch()

        MainScreen {
            NotesScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem(position = 0)
                NoteDialog { onClickDelete() }

                assert { onDisplayContent(empty = true) }
            }

            navigateTo(MainPage.Name.BIN)

            BinScreen { assert { onDisplayContent(empty = false) } }
        }

    }

}