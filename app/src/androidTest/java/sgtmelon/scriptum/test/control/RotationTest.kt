package sgtmelon.scriptum.test.control

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.waitAfter

/**
 * Тест работы приложения при повороте экрана
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class RotationTest : ParentUiTest() {

    // TODO запускать handler + toast для уведомления о повороте
    // TODO заменить вызов assert на более короткий

    @Test fun addDialog() = launch {
        mainScreen { openAddDialog { waitAfter(TIME) { assert { onDisplayContent() } } } }
    }


    @Test fun rankScreenContentEmpty() = launch {
        mainScreen {
            openRankPage(empty = true) { onRotate { assert(empty = true) } }
            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun rankScreenContentList() = launch({ testData.fillRank() }) {
        mainScreen {
            openRankPage { onRotate { assert(empty = false) } }
            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun rankScreenRenameDialog() = testData.insertRank().let {
        val newName = testData.uniqueString

        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(it.name) {
                        onEnterName(newName, enabled = true)
                        onRotate { assert(newName, enabled = true) }
                    }
                }
            }
        }
    }


    @Test fun notesScreenContentEmpty() = launch {
        mainScreen {
            openNotesPage(empty = true) { onRotate { assert(empty = true) } }
            assert { onDisplayFab(visible = true) }
        }
    }

    @Test fun notesScreenContentList() = launch({ testData.fillNotes() }) {
        mainScreen {
            openNotesPage { onRotate { assert(empty = false) } }
            assert { onDisplayFab(visible = true) }
        }
    }

    @Test fun notesScreenTextNoteDialog() = testData.insertText().let {
        launch { mainScreen { openNotesPage { openNoteDialog(it) { onRotate { assert() } } } } }
    }

    @Test fun notesScreenRollNoteDialog() = testData.insertRoll().let {
        launch { mainScreen { openNotesPage { openNoteDialog(it) { onRotate { assert() } } } } }
    }


    @Test fun binScreenContentEmpty() = launch {
        mainScreen {
            openBinPage(empty = true) { onRotate { assert(empty = true) } }
            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun binScreenContentList() = launch({ testData.fillBin() }) {
        mainScreen {
            openBinPage { onRotate { assert(empty = false) } }
            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun binScreenClearDialog() = launch({ testData.fillBin() }) {
        mainScreen { openBinPage { openClearDialog { onRotate { assert() } } } }
    }

    @Test fun binScreenTextNoteDialog() = testData.insertTextToBin().let {
        launch { mainScreen { openBinPage { openNoteDialog(it) { onRotate { assert() } } } } }
    }

    @Test fun binScreenRollNoteDialog() = testData.insertRollToBin().let {
        launch { mainScreen { openBinPage { openNoteDialog(it) { onRotate { assert() } } } } }
    }


    @Test fun notificationScreenContentEmpty() = launch {
        mainScreen {
            openNotesPage(empty = true) {
                openNotification(empty = true) { onRotate { assert(empty = true) } }
            }
        }
    }

    @Test fun notificationScreenContentList() = launch({ testData.fillNotification() }) {
        mainScreen { openNotesPage { openNotification { onRotate { assert(empty = false) } } } }
    }


    private companion object {
        const val TIME = 5000L
    }

}