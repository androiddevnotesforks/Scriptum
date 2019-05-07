package sgtmelon.scriptum.test.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.screen.view.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.screen.main.MainScreen

/**
 * Тест для [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class RollNoteTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        preference.firstStart = false
        testData.clearAllData()
    }

    @Test fun toolbarCreate() = afterLaunch {
        MainScreen {
            addDialogUi {
                rollNoteScreen { toolbar { assert { onDisplayName(State.NEW, name = "") } } }
            }
        }
    }

    @Test fun toolbarWithoutName() {
        val noteItem = testData.insertRoll(testData.rollNote.apply { name = "" })

        afterLaunch {
            MainScreen {
                notesScreen {
                    rollNoteScreen {
                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { assert { onDisplayName(State.EDIT, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun toolbarFromBinWithoutName() {
        val noteItem = testData.insertRollToBin(testData.rollNote.apply { name = "" })

        afterLaunch {
            MainScreen {
                binScreen {
                    rollNoteScreen {
                        toolbar { assert { onDisplayName(State.BIN, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun toolbarWithName() {
        val noteItem = testData.insertRoll()

        afterLaunch {
            MainScreen {
                notesScreen {
                    rollNoteScreen {
                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { assert { onDisplayName(State.EDIT, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun toolbarFromBinWithName() {
        val noteItem = testData.insertRollToBin()

        afterLaunch {
            MainScreen {
                binScreen {
                    rollNoteScreen {
                        toolbar { assert { onDisplayName(State.BIN, noteItem.name) } }
                    }
                }
            }
        }
    }


    @Test fun toolbarSaveAfterCreateByControl() = afterLaunch {
        val noteItem = testData.rollNote
        val listRoll = testData.listRoll

        MainScreen {
            addDialogUi {
                rollNoteScreen {
                    toolbar { onEnterName(noteItem.name) }
                    enterPanel { onAddRoll(listRoll[0].text) }


                    toolbar { assert { onDisplayName(State.EDIT, noteItem.name) } }
                    controlPanel { onClickSave() }
                    toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                }
            }
        }
    }

    @Test fun toolbarSaveAfterCreateByBackPress() = afterLaunch {
        val noteItem = testData.rollNote
        val listRoll = testData.listRoll

        MainScreen {
            addDialogUi {
                rollNoteScreen {
                    toolbar { onEnterName(noteItem.name) }
                    enterPanel { onAddRoll(listRoll[0].text) }

                    toolbar { assert { onDisplayName(State.EDIT, noteItem.name) } }
                    onPressBack()
                    toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                }
            }
        }
    }

    @Test fun toolbarSaveAfterEditByControl() {
        val noteItem = testData.insertRoll()
        val newName = testData.uniqueString

        afterLaunch {
            MainScreen {
                notesScreen {
                    rollNoteScreen {
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
        val noteItem = testData.insertRoll()
        val newName = testData.uniqueString

        afterLaunch {
            MainScreen {
                notesScreen {
                    rollNoteScreen {
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
        val noteItem = testData.insertRollToBin()

        afterLaunch {
            MainScreen {
                binScreen {
                    rollNoteScreen {
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
            addDialogUi { rollNoteScreen { toolbar { onClickBack() } } }
            assert { onDisplayContent() }
            addDialogUi { rollNoteScreen { onPressBack() } }
            assert { onDisplayContent() }
        }
    }

    @Test fun toolbarCancelAfterEdit() {
        val noteItem = testData.insertRoll()
        val newName = testData.uniqueString

        afterLaunch {
            MainScreen {
                notesScreen {
                    rollNoteScreen {
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

}