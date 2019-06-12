package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.screen.view.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentTest

/**
 * Тест для [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class RollNoteToolbarTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        iPreferenceRepo.firstStart = false
        testData.clear()
    }

    @Test fun closeByToolbarOnCreate() = launch {
        mainScreen {
            openAddDialog { createRollNote { toolbar { onClickBack() } } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByBackPressOnCreate() = launch {
        mainScreen {
            openAddDialog { createRollNote { onPressBack() } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByToolbarOnOpen() = launch({ testData.insertRoll() }) {
        mainScreen {
            openNotesPage { openRollNote { toolbar { onClickBack() } } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByBackPressOnOpen() = launch({ testData.insertRoll() }) {
        mainScreen {
            openNotesPage { openRollNote { onPressBack() } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByToolbarOnOpenFromBin() = launch({ testData.insertRollToBin() }) {
        mainScreen {
            openBinPage { openRollNote { toolbar { onClickBack() } } }
            assert { onDisplayContent() }
        }
    }

    @Test fun closeByBackPressOnOpenFromBin() = launch({ testData.insertRollToBin() }) {
        mainScreen {
            openBinPage { openRollNote { onPressBack() } }
            assert { onDisplayContent() }
        }
    }


    @Test fun contentEmptyOnCreate() = launch {
        mainScreen {
            openAddDialog {
                createRollNote { toolbar { assert { onDisplayName(State.NEW, name = "") } } }
            }
        }
    }

    @Test fun contentEmptyOnOpen() {
        val noteItem = testData.insertRoll(testData.rollNote.apply { name = "" })

        launch {
            mainScreen {
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

        launch {
            mainScreen {
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

        launch {
            mainScreen {
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

        launch {
            mainScreen {
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

        launch {
            mainScreen {
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


    @Test fun saveByControlOnCreate() = launch {
        val noteItem = testData.rollNote
        val rollList = testData.rollList

        mainScreen {
            openAddDialog {
                createRollNote {
                    toolbar { onEnterName(noteItem.name) }
                    enterPanel { onAddRoll(rollList[0].text) }


                    toolbar { assert { onDisplayName(State.EDIT, noteItem.name) } }
                    controlPanel { onClickSave() }
                    toolbar { assert { onDisplayName(State.READ, noteItem.name) } }
                }
            }
        }
    }

    @Test fun saveByBackPressOnCreate() = launch {
        val noteItem = testData.rollNote
        val rollList = testData.rollList

        mainScreen {
            openAddDialog {
                createRollNote {
                    toolbar { onEnterName(noteItem.name) }
                    enterPanel { onAddRoll(rollList[0].text) }

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

        launch {
            mainScreen {
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

        launch {
            mainScreen {
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


    @Test fun cancelOnEditByToolbar() = launch {
        mainScreen {
            openAddDialog { createRollNote { toolbar { onClickBack() } } }
            assert { onDisplayContent() }
            openAddDialog { createRollNote { onPressBack() } }
            assert { onDisplayContent() }
        }
    }

}