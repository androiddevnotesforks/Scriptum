package sgtmelon.scriptum.test.auto.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Тест для [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class RollNoteTest : ParentUiTest() {

    @Test fun binContentWithoutName() = testData.insertRollToBin(
            testData.rollNote.apply { name = "" }
    ).let {
        launch { mainScreen { openBinPage { openRollNote(it) } } }
    }

    @Test fun binContentWithName() = testData.insertRollToBin().let {
        launch { mainScreen { openBinPage { openRollNote(it) } } }
    }

    @Test fun binClose()  = testData.insertRollToBin().let {
        launch {
            mainScreen {
                openBinPage { openRollNote(it) { toolbar { onClickBack() } } }
                assert()
                openBinPage { openRollNote(it) { onPressBack() } }
                assert()
            }
        }
    }

    @Test fun binActionRestore()  = testData.insertRollToBin().let {
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

    @Test fun binActionRestoreOpen()  = testData.insertRollToBin().let {
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

    @Test fun binActionClear() = testData.insertRollToBin().let {
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

}