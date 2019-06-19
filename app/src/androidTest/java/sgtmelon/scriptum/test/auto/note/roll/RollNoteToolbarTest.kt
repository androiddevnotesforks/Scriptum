package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.screen.view.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Тест для [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class RollNoteToolbarTest : ParentUiTest() {

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
        val noteEntity = testData.insertRoll(testData.rollNote.apply { name = "" })

        launch {
            mainScreen {
                openNotesPage {
                    openRollNote {
                        toolbar { assert { onDisplayName(State.READ, noteEntity.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { assert { onDisplayName(State.EDIT, noteEntity.name) } }
                    }
                }
            }
        }
    }

    @Test fun contentFillOnOpen() {
        val noteEntity = testData.insertRoll()

        launch {
            mainScreen {
                openNotesPage {
                    openRollNote {
                        toolbar { assert { onDisplayName(State.READ, noteEntity.name) } }
                        controlPanel { onClickEdit() }
                        toolbar { assert { onDisplayName(State.EDIT, noteEntity.name) } }
                    }
                }
            }
        }
    }

    @Test fun contentEmptyOnOpenFromBin() {
        val noteEntity = testData.insertRollToBin(testData.rollNote.apply { name = "" })

        launch {
            mainScreen {
                openBinPage {
                    openRollNote {
                        toolbar { assert { onDisplayName(State.BIN, noteEntity.name) } }
                    }
                }
            }
        }
    }

    @Test fun contentFillOnOpenFromBin() {
        val noteEntity = testData.insertRollToBin()

        launch {
            mainScreen {
                openBinPage {
                    openRollNote {
                        toolbar { assert { onDisplayName(State.BIN, noteEntity.name) } }
                    }
                }
            }
        }
    }

    @Test fun contentFillOnRestoreOpen() {
        val noteEntity = testData.insertRollToBin()

        launch {
            mainScreen {
                openBinPage {
                    openRollNote {
                        toolbar { assert { onDisplayName(State.BIN, noteEntity.name) } }
                        controlPanel { onClickRestoreOpen() }
                        toolbar { assert { onDisplayName(State.READ, noteEntity.name) } }
                    }
                }
            }
        }
    }


    @Test fun saveByControlOnCreate() = launch {
        val noteEntity = testData.rollNote
        val rollList = testData.rollList

        mainScreen {
            openAddDialog {
                createRollNote {
                    toolbar { onEnterName(noteEntity.name) }
                    enterPanel { onAddRoll(rollList[0].text) }


                    toolbar { assert { onDisplayName(State.EDIT, noteEntity.name) } }
                    controlPanel { onClickSave() }
                    toolbar { assert { onDisplayName(State.READ, noteEntity.name) } }
                }
            }
        }
    }

    @Test fun saveByBackPressOnCreate() = launch {
        val noteEntity = testData.rollNote
        val rollList = testData.rollList

        mainScreen {
            openAddDialog {
                createRollNote {
                    toolbar { onEnterName(noteEntity.name) }
                    enterPanel { onAddRoll(rollList[0].text) }

                    toolbar { assert { onDisplayName(State.EDIT, noteEntity.name) } }
                    onPressBack()
                    toolbar { assert { onDisplayName(State.READ, noteEntity.name) } }
                }
            }
        }
    }

    @Test fun saveByControlOnEdit() {
        val noteEntity = testData.insertRoll()
        val newName = testData.uniqueString

        launch {
            mainScreen {
                openNotesPage {
                    openRollNote {
                        toolbar { assert { onDisplayName(State.READ, noteEntity.name) } }

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
        val noteEntity = testData.insertRoll()
        val newName = testData.uniqueString

        launch {
            mainScreen {
                openNotesPage {
                    openRollNote {
                        toolbar { assert { onDisplayName(State.READ, noteEntity.name) } }

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