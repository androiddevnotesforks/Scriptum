package sgtmelon.scriptum.test.note.roll

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
class RollNoteToolbarTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        preference.firstStart = false
        testData.clear()
    }

    @Test fun closeByToolbarOnCreate() = afterLaunch {
        MainScreen {
            openAddDialog { createRollNote { toolbar { onClickBack() } } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByBackPressOnCreate() = afterLaunch {
        MainScreen {
            openAddDialog { createRollNote { onPressBack() } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByToolbarOnOpen() {
        beforeLaunch { testData.insertRoll() }

        MainScreen {
            openNotesPage { openRollNote { toolbar { onClickBack() } } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByBackPressOnOpen() {
        beforeLaunch { testData.insertRoll() }

        MainScreen {
            openNotesPage { openRollNote { onPressBack() } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByToolbarOnOpenFromBin() {
        beforeLaunch { testData.insertRollToBin() }

        MainScreen {
            openBinPage { openRollNote { toolbar { onClickBack() } } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByBackPressOnOpenFromBin() {
        beforeLaunch { testData.insertRollToBin() }

        MainScreen {
            openBinPage { openRollNote { onPressBack() } }
            assert { onDisplayContent() }
        }
    }


    @Test fun contentEmptyOnCreate() = afterLaunch {
        MainScreen {
            openAddDialog {
                createRollNote { toolbar { assert { onDisplayName(State.NEW, name = "") } } }
            }
        }
    }

    @Test fun contentEmptyOnOpen() {
        val noteItem = testData.insertRoll(testData.rollNote.apply { name = "" })

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openRollNote {
                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { assert { onDisplayName(State.EDIT, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun contentFillOnOpen() {
        val noteItem = testData.insertRoll()

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openRollNote {
                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { assert { onDisplayName(State.EDIT, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun contentEmptyOnOpenFromBin() {
        val noteItem = testData.insertRollToBin(testData.rollNote.apply { name = "" })

        afterLaunch {
            MainScreen {
                openBinPage {
                    openRollNote {
                        toolbar { assert { onDisplayName(State.BIN, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun contentFillOnOpenFromBin() {
        val noteItem = testData.insertRollToBin()

        afterLaunch {
            MainScreen {
                openBinPage {
                    openRollNote {
                        toolbar { assert { onDisplayName(State.BIN, noteItem.name) } }
                    }
                }
            }
        }
    }

    @Test fun contentFillOnRestoreOpen() {
        val noteItem = testData.insertRollToBin()

        afterLaunch {
            MainScreen {
                openBinPage {
                    openRollNote {
                        toolbar { assert { onDisplayName(State.BIN, noteItem.name) } }
                        controlPanel { onClickRestoreOpen() }
                        toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                    }
                }
            }
        }
    }


    @Test fun saveByControlOnCreate() = afterLaunch {
        val noteItem = testData.rollNote
        val listRoll = testData.listRoll

        MainScreen {
            openAddDialog {
                createRollNote {
                    toolbar { onEnterName(noteItem.name) }
                    enterPanel { onAddRoll(listRoll[0].text) }


                    toolbar { assert { onDisplayName(State.EDIT, noteItem.name) } }
                    controlPanel { onClickSave() }
                    toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                }
            }
        }
    }

    @Test fun saveByBackPressOnCreate() = afterLaunch {
        val noteItem = testData.rollNote
        val listRoll = testData.listRoll

        MainScreen {
            openAddDialog {
                createRollNote {
                    toolbar { onEnterName(noteItem.name) }
                    enterPanel { onAddRoll(listRoll[0].text) }

                    toolbar { assert { onDisplayName(State.EDIT, noteItem.name) } }
                    onPressBack()
                    toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                }
            }
        }
    }

    @Test fun saveByControlOnEdit() {
        val noteItem = testData.insertRoll()
        val newName = testData.uniqueString

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openRollNote {
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

    @Test fun saveByBackPressOnEdit() {
        val noteItem = testData.insertRoll()
        val newName = testData.uniqueString

        afterLaunch {
            MainScreen {
                openNotesPage {
                    openRollNote {
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


    @Test fun cancelOnEditByToolbar() = afterLaunch {
        MainScreen {
            openAddDialog { createRollNote { toolbar { onClickBack() } } }
            assert { onDisplayContent() }
            openAddDialog { createRollNote { onPressBack() } }
            assert { onDisplayContent() }
        }
    }

}