package sgtmelon.scriptum.test.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.screen.view.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.screen.main.MainScreen

/**
 * Тест для [TextNoteFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class TextNoteTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        preference.firstStart = false
        testData.clearAllData()
    }

    @Test fun toolbarNoteCreate() = afterLaunch {
        MainScreen {
            addDialogUi {
                textNoteScreen { toolbar { assert { onDisplayState(State.NEW, name = "") } } }
            }
        }
    }

    @Test fun toolbarNoteWithoutName() {
        val noteItem = testData.insertText(testData.textNote.apply { name = "" })

        afterLaunch {
            MainScreen {
                notesScreen {
                    textNoteScreen() {
                        toolbar { assert { onDisplayState(State.READ, noteItem.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { assert { onDisplayState(State.EDIT, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun toolbarNoteFromBinWithoutName() {
        val noteItem = testData.insertTextToBin(testData.textNote.apply { name = "" })

        afterLaunch {
            MainScreen {
                binScreen {
                    textNoteScreen() {
                        toolbar { assert { onDisplayState(State.BIN, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun toolbarNoteWithName() {
        val noteItem = testData.insertText()

        afterLaunch {
            MainScreen {
                notesScreen {
                    textNoteScreen() {
                        toolbar { assert { onDisplayState(State.READ, noteItem.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { assert { onDisplayState(State.EDIT, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun toolbarNoteFromBinWithName() {
        val noteItem = testData.insertTextToBin()

        afterLaunch {
            MainScreen {
                binScreen {
                    textNoteScreen() {
                        toolbar { assert { onDisplayState(State.BIN, noteItem.name) } }
                    }
                }
            }
        }
    }


    @Test fun toolbarNoteSaveAfterCreate() = afterLaunch {
        val noteItem = testData.textNote

        MainScreen {
            addDialogUi {
                textNoteScreen {
                    toolbar { onEnterName(noteItem.name) }
                    onEnterText(noteItem.text)
                    controlPanel { onClickSave() }
                    toolbar { assert { onDisplayState(State.READ, noteItem.name) } }
                }
            }
        }
    }

    @Test fun toolbarNoteSaveAfterEdit() {
        val noteItem = testData.insertText()
        val newName = testData.uniqueString

        afterLaunch {
            MainScreen {
                notesScreen {
                    textNoteScreen() {
                        toolbar { assert { onDisplayState(State.READ, noteItem.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { onEnterName(newName) }
                        controlPanel { onClickSave() }
                        toolbar { assert { onDisplayState(State.READ, newName) } }
                    }
                }
            }
        }
    }

    @Test fun toolbarNoteSaveAfterEditByBack() {
        val noteItem = testData.insertText()
        val newName = testData.uniqueString

        afterLaunch {
            MainScreen {
                notesScreen {
                    textNoteScreen() {
                        toolbar { assert { onDisplayState(State.READ, noteItem.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { onEnterName(newName) }
                        onPressBack()
                        toolbar { assert { onDisplayState(State.READ, newName) } }
                    }
                }
            }
        }
    }


    @Test fun toolbarNoteCancelAfterCreate() = afterLaunch {
        MainScreen {
            repeat(times = 3) {
                addDialogUi { textNoteScreen { onCloseNote() } }
                assert { onDisplayContent() }
            }
        }
    }

    @Test fun toolbarNoteCancelAfterEdit() {
        val noteItem = testData.insertText()
        val newName = testData.uniqueString

        afterLaunch {
            MainScreen {
                notesScreen {
                    textNoteScreen() {
                        toolbar { assert { onDisplayState(State.READ, noteItem.name) } }
                        controlPanel { onClickEdit() }
                        toolbar {
                            onEnterName(newName)
                            onClickBack()
                        }
                        toolbar { assert { onDisplayState(State.READ, noteItem.name) } }
                    }
                }
            }
        }
    }
}