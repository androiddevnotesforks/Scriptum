package sgtmelon.scriptum.test.main

import android.content.Intent
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.dialog.AddDialog
import sgtmelon.scriptum.ui.dialog.NoteDialog
import sgtmelon.scriptum.ui.screen.PreferenceScreen
import sgtmelon.scriptum.ui.screen.main.BinScreen
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.main.NotesScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

@RunWith(AndroidJUnit4::class)
class NotesTest : ParentTest() {

    // TODO extension launchActivity()

    override fun setUp() {
        super.setUp()

        preference.firstStart = false
    }

    @Test fun contentEmpty() {
        testData.clearAllData()

        testRule.launchActivity(Intent())

        MainScreen { NotesScreen { assert { onDisplayContent(empty = true) } } }
    }

    @Test fun contentList() {
        testData.apply { clearAllData() }.fillNotes()

        testRule.launchActivity(Intent())

        MainScreen { NotesScreen { assert { onDisplayContent(empty = false) } } }
    }

    @Test fun openPreference() {
        testRule.launchActivity(Intent())

        MainScreen {
            NotesScreen {
                onClickPreference()
                PreferenceScreen { assert { onDisplayContent() } }
            }
        }
    }

    @Test fun addFabVisibilityOnScroll() {
        testData.apply { clearAllData() }.fillNotes(times = 20)

        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayFab(visible = true) }

            NotesScreen { onScroll(Scroll.END, time = 4) }

            assert { onDisplayFab(visible = false) }

            NotesScreen { onScroll(Scroll.START, time = 4) }

            assert { onDisplayFab(visible = true) }
        }
    }

    @Test fun openTextNote() {
        testData.apply { clearAllData() }.insertText()

        testRule.launchActivity(Intent())

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

    @Test fun openRollNote() {
        testData.apply { clearAllData() }.insertRoll()

        testRule.launchActivity(Intent())

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

    @Test fun createTextNoteAndReturnWithoutSave() {
        testData.clearAllData()

        testRule.launchActivity(Intent())

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

    @Test fun createRollNoteAndReturnWithoutSave() {
        testData.clearAllData()

        testRule.launchActivity(Intent())

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

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun createTextNoteAndReturnWithSave() {
        testData.clearAllData()

        testRule.launchActivity(Intent())

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

    @Test fun createRollNoteAndReturnWithSave() {
        testData.clearAllData()

        testRule.launchActivity(Intent())

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

    @Test fun openTextNoteDialog() {
        val noteItem = testData.apply { clearAllData() }.insertText()

        testRule.launchActivity(Intent())

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

    @Test fun openRollNoteDialog() {
        val noteItem = testData.apply { clearAllData() }.insertRoll()

        testRule.launchActivity(Intent())

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

    @Test fun checkAllRollNote() {
        TODO("not create")
    }

    @Test fun uncheckAllRollNote() {
        TODO("not create")
    }

    @Test fun bindTextNote() {
        TODO("not create")
    }

    @Test fun unbindTextNote() {
        TODO("not create")
    }

    @Test fun bindRollNote() {
        TODO("not create")
    }

    @Test fun unbindRollNote() {
        TODO("not create")
    }

    @Test fun convertTextNoteToRoll() {
        val noteItem = testData.apply { clearAllData() }.insertText()

        testRule.launchActivity(Intent())

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

    @Test fun convertRollNoteToText() {
        val noteItem = testData.apply { clearAllData() }.insertRoll()

        testRule.launchActivity(Intent())

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

    @Test fun copyTextNote() {
        TODO("not create")
    }

    @Test fun copyRollNote() {
        TODO("not create")
    }

    @Test fun deleteTextNote() {
        testData.apply { clearAllData() }.insertText()

        testRule.launchActivity(Intent())

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

    @Test fun deleteRollNote() {
        testData.apply { clearAllData() }.insertRoll()

        testRule.launchActivity(Intent())

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