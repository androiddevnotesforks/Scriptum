package sgtmelon.scriptum.test.auto.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
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

    @Test fun closeByToolbarOnCreate() = testData.createRollNote().let {
        launch {
            mainScreen {
                openAddDialog { createRollNote(it) { toolbar { onClickBack() } } }
                assert { onDisplayContent() }
            }
        }
    }

    @Test fun closeByBackPressOnCreate() = testData.createRollNote().let {
        launch {
            mainScreen {
                openAddDialog { createRollNote(it) { onPressBack() } }
                assert { onDisplayContent() }
            }
        }
    }

    @Test fun closeByToolbarOnOpen() = testData.insertRollNote().let {
        launch {
            mainScreen {
                openNotesPage { openRollNote(it) { toolbar { onClickBack() } } }
                assert { onDisplayContent() }
            }
        }
    }

    @Test fun closeByBackPressOnOpen() = testData.insertRollNote().let {
        launch {
            mainScreen {
                openNotesPage { openRollNote(it) { onPressBack() } }
                assert { onDisplayContent() }
            }
        }
    }

    @Test fun closeByToolbarOnOpenFromBin() = testData.insertRollNoteToBin().let {
        launch {
            mainScreen {
                openBinPage { openRollNote(it) { toolbar { onClickBack() } } }
                assert { onDisplayContent() }
            }
        }
    }

    @Test fun closeByBackPressOnOpenFromBin() = testData.insertRollNoteToBin().let {
        launch {
            mainScreen {
                openBinPage { openRollNote(it) { onPressBack() } }
                assert { onDisplayContent() }
            }
        }
    }


    @Test fun contentEmptyOnCreate() = testData.createRollNote().let {
        launch { mainScreen { openAddDialog { createRollNote(it) } } }
    }

    @Test fun contentEmptyOnOpen() = testData.insertRollNote(
            testData.rollNote.apply { name = "" }
    ).let {
        launch {
            mainScreen { openNotesPage { openRollNote(it) { controlPanel { onClickEdit() } } } }
        }
    }

    @Test fun contentFillOnOpen() = testData.insertRollNote().let {
        launch {
            mainScreen { openNotesPage { openRollNote(it) { controlPanel { onClickEdit() } } } }
        }
    }

    @Test fun contentEmptyOnOpenFromBin() = testData.insertRollNoteToBin(
            testData.rollNote.apply { name = "" }
    ).let { launch { mainScreen { openBinPage { openRollNote(it) } } } }

    @Test fun contentFillOnOpenFromBin() = testData.insertRollNoteToBin().let {
        launch { mainScreen { openBinPage { openRollNote(it) } } }
    }

    @Test fun contentFillOnRestoreOpen() = testData.insertRollNoteToBin().let {
        launch {
            mainScreen {
                openBinPage { openRollNote(it) { controlPanel { onClickRestoreOpen() } } }
            }
        }
    }


    @Test fun saveByControlOnCreate() = testData.createRollNote().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRollNote(it) {
                        toolbar { onEnterName(testData.uniqueString) }
                        enterPanel { onAddRoll(testData.uniqueString) }
                        controlPanel { onClickSave() }
                    }
                }
            }
        }
    }

    @Test fun saveByBackPressOnCreate() = testData.createRollNote().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRollNote(it) {
                        toolbar { onEnterName(testData.uniqueString) }
                        enterPanel { onAddRoll(testData.uniqueString) }
                        onPressBack()
                    }
                }
            }
        }
    }

    @Test fun saveByControlOnEdit() = testData.insertRollNote().let {
        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(it) {
                        controlPanel { onClickEdit() }
                        toolbar { onEnterName(testData.uniqueString) }
                        controlPanel { onClickSave() }
                    }
                }
            }
        }
    }

    @Test fun saveByBackPressOnEdit() = testData.insertRollNote().let {
        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(it) {
                        controlPanel { onClickEdit() }
                        toolbar { onEnterName(testData.uniqueString) }
                        onPressBack()
                    }
                }
            }
        }
    }


    @Test fun cancelOnEdit() = testData.createRollNote().let {
        launch {
            mainScreen {
                openAddDialog { createRollNote(it) { toolbar { onClickBack() } } }
                assert { onDisplayContent() }
                openAddDialog { createRollNote(it) { onPressBack() } }
                assert { onDisplayContent() }
            }
        }
    }

}