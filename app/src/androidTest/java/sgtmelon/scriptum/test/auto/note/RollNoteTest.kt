package sgtmelon.scriptum.test.auto.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [RollNoteFragment]
 */
@RunWith(AndroidJUnit4::class)
class RollNoteTest : ParentUiTest() {

    //region Bin

    @Test fun binContentWithoutName() = data.insertRollToBin(data.rollNote.copy(name = "")).let {
        launch { mainScreen { openBinPage { openRollNote(it) } } }
    }

    @Test fun binContentWithName() = data.insertRollToBin().let {
        launch { mainScreen { openBinPage { openRollNote(it) } } }
    }

    @Test fun binActionRestore()  = data.insertRollToBin().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openBinPage {
                    openRollNote(it) { controlPanel { onClickRestore() } }
                    assert(empty = true)
                }

                openNotesPage()
            }
        }
    }

    @Test fun binActionRestoreOpen()  = data.insertRollToBin().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openBinPage {
                    openRollNote(it) {
                        controlPanel { onClickRestoreOpen() }
                        onPressBack()
                    }
                    assert(empty = true)
                }

                openNotesPage()
            }
        }
    }

    @Test fun binActionClear() = data.insertRollToBin().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openBinPage {
                    openRollNote(it) { controlPanel { onClickClear() } }
                    assert(empty = true)
                }

                openNotesPage(empty = true)
            }
        }
    }

    //endregion

    //region Close

    @Test fun closeOnBin()  = data.insertRollToBin().let {
        launch {
            mainScreen {
                openBinPage { openRollNote(it) { toolbar { onClickBack() } } }
                assert()
                openBinPage { openRollNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun closeOnCreate() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog { createRollNote(it) { toolbar { onClickBack() } } }
                assert()
                openAddDialog { createRollNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun closeOnRead() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage { openRollNote(it) { toolbar { onClickBack() } } }
                assert()
                openNotesPage { openRollNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    //endregion

}