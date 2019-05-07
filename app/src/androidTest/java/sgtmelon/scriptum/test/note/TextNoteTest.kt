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

    @Test fun toolbarCreate() = afterLaunch {
        MainScreen {
            addDialogUi {
                textNoteScreen { toolbar { assert { onDisplayName(State.NEW, name = "") } } }
            }
        }
    }

    @Test fun toolbarWithoutName() {
        val noteItem = testData.insertText(testData.textNote.apply { name = "" })

        afterLaunch {
            MainScreen {
                notesScreen {
                    textNoteScreen {
                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { assert { onDisplayName(State.EDIT, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun toolbarFromBinWithoutName() {
        val noteItem = testData.insertTextToBin(testData.textNote.apply { name = "" })

        afterLaunch {
            MainScreen {
                binScreen {
                    textNoteScreen {
                        toolbar { assert { onDisplayName(State.BIN, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun toolbarWithName() {
        val noteItem = testData.insertText()

        afterLaunch {
            MainScreen {
                notesScreen {
                    textNoteScreen {
                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { assert { onDisplayName(State.EDIT, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun toolbarFromBinWithName() {
        val noteItem = testData.insertTextToBin()

        afterLaunch {
            MainScreen {
                binScreen {
                    textNoteScreen {
                        toolbar { assert { onDisplayName(State.BIN, noteItem.name) } }
                    }
                }
            }
        }
    }


    @Test fun toolbarSaveAfterCreateByControl() = afterLaunch {
        val noteItem = testData.textNote

        MainScreen {
            addDialogUi {
                textNoteScreen {
                    toolbar { onEnterName(noteItem.name) }
                    onEnterText(noteItem.text)

                    toolbar { assert { onDisplayName(State.EDIT, noteItem.name) } }
                    controlPanel { onClickSave() }
                    toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                }
            }
        }
    }

    @Test fun toolbarSaveAfterCreateByBackPress() = afterLaunch {
        val noteItem = testData.textNote

        MainScreen {
            addDialogUi {
                textNoteScreen {
                    toolbar { onEnterName(noteItem.name) }
                    onEnterText(noteItem.text)

                    toolbar { assert { onDisplayName(State.EDIT, noteItem.name) } }
                    onPressBack()
                    toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                }
            }
        }
    }

    @Test fun toolbarSaveAfterEditByControl() {
        val noteItem = testData.insertText()
        val newName = testData.uniqueString

        afterLaunch {
            MainScreen {
                notesScreen {
                    textNoteScreen {
                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }

                        controlPanel { onClickEdit() }
                        toolbar { onEnterName(newName) }
                        controlPanel { onClickSave() }

                        toolbar { assert { onDisplayName(State.READ, newName) } }
                    }
                }
            }
        }
    }

    @Test fun toolbarSaveAfterEditByBackPress() {
        val noteItem = testData.insertText()
        val newName = testData.uniqueString

        afterLaunch {
            MainScreen {
                notesScreen {
                    textNoteScreen {
                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }

                        controlPanel { onClickEdit() }
                        toolbar { onEnterName(newName) }
                        onPressBack()

                        toolbar { assert { onDisplayName(State.READ, newName) } }
                    }
                }
            }
        }
    }

    @Test fun toolbarRestoreOpen() {
        val noteItem = testData.insertTextToBin()

        afterLaunch {
            MainScreen {
                binScreen {
                    textNoteScreen {
                        toolbar { assert { onDisplayName(State.BIN, noteItem.name) } }
                        controlPanel { onClickRestoreOpen() }
                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                    }
                }
            }
        }
    }


    @Test fun toolbarCancelAfterCreate() = afterLaunch {
        MainScreen {
            addDialogUi { textNoteScreen { toolbar { onClickBack() } } }
            assert { onDisplayContent() }
            addDialogUi { textNoteScreen { onPressBack() } }
            assert { onDisplayContent() }
        }
    }

    @Test fun toolbarCancelAfterEdit() {
        val noteItem = testData.insertText()
        val newName = testData.uniqueString

        afterLaunch {
            MainScreen {
                notesScreen {
                    textNoteScreen {
                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }

                        controlPanel { onClickEdit() }
                        toolbar {
                            onEnterName(newName)
                            onClickBack()
                        }

                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                    }
                }
            }
        }
    }


    @Test fun contentCreate() = afterLaunch {
        MainScreen {
            addDialogUi {
                textNoteScreen { assert { onDisplayText(State.EDIT, text = "") } }
            }
        }
    }

    @Test fun contentWithText() {
        val noteItem = testData.insertText()

        afterLaunch {
            MainScreen {
                notesScreen {
                    textNoteScreen {
                        assert { onDisplayText(State.READ, noteItem.text) }
                        controlPanel { onClickEdit() }
                        assert { onDisplayText(State.EDIT, noteItem.text) }
                    }
                }
            }
        }
    }

    @Test fun contentFromBinWithText() {
        val noteItem = testData.insertTextToBin()

        afterLaunch {
            MainScreen {
                binScreen { textNoteScreen { assert { onDisplayText(State.BIN, noteItem.text) } } }
            }
        }
    }


    @Test fun contentSaveAfterCreateByControl() = afterLaunch {
        val noteItem = testData.textNote

        MainScreen {
            addDialogUi {
                textNoteScreen {
                    onEnterText(noteItem.text)

                    assert { onDisplayText(State.EDIT, noteItem.text) }
                    controlPanel { onClickSave() }
                    assert { onDisplayText(State.READ, noteItem.text) }
                }
            }
        }
    }

    @Test fun contentSaveAfterCreateByBackPress() = afterLaunch {
        val noteItem = testData.textNote

        MainScreen {
            addDialogUi {
                textNoteScreen {
                    onEnterText(noteItem.text)

                    assert { onDisplayText(State.EDIT, noteItem.text) }
                    onPressBack()
                    assert { onDisplayText(State.READ, noteItem.text) }
                }
            }
        }
    }

    @Test fun contentSaveAfterEditByControl() {
        val noteItem = testData.insertText()
        val newText = testData.uniqueString

        afterLaunch {
            MainScreen {
                notesScreen {
                    textNoteScreen {
                        assert { onDisplayText(State.READ, noteItem.text) }

                        controlPanel { onClickEdit() }
                        onEnterText(newText)
                        controlPanel { onClickSave() }

                        assert { onDisplayText(State.READ, newText) }
                    }
                }
            }
        }
    }

    @Test fun contentSaveAfterEditByBackPress() {
        val noteItem = testData.insertText()
        val newText = testData.uniqueString

        afterLaunch {
            MainScreen {
                notesScreen {
                    textNoteScreen {
                        assert { onDisplayText(State.READ, noteItem.text) }

                        controlPanel { onClickEdit() }
                        onEnterText(newText)
                        onPressBack()

                        assert { onDisplayText(State.READ, newText) }
                    }
                }
            }
        }
    }

    @Test fun contentRestoreOpen() {
        val noteItem = testData.insertTextToBin()

        afterLaunch {
            MainScreen {
                binScreen {
                    textNoteScreen {
                        assert { onDisplayText(State.BIN, noteItem.text) }
                        controlPanel { onClickRestoreOpen() }
                        assert { onDisplayText(State.READ, noteItem.text) }
                    }
                }
            }
        }
    }


    @Test fun contentCancelAfterEdit() {
        val noteItem = testData.insertText()
        val newText = testData.uniqueString

        afterLaunch {
            MainScreen {
                notesScreen {
                    textNoteScreen {
                        assert { onDisplayText(State.READ, noteItem.text) }

                        controlPanel { onClickEdit() }
                        onEnterText(newText)
                        toolbar { onClickBack() }

                        assert { onDisplayText(State.READ, noteItem.text) }
                    }
                }
            }
        }
    }


}