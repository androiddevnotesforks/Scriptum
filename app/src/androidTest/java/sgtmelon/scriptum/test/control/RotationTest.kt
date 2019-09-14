package sgtmelon.scriptum.test.control

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.waitAfter

/**
 * Test of application work with phone rotation
 */
@RunWith(AndroidJUnit4::class)
class RotationTest : ParentUiTest() {

    @Test fun addDialog() = launch { mainScreen { openAddDialog { onRotate { assert() } } } }


    @Test fun rankScreenContentEmpty() = launch {
        mainScreen {
            openRankPage(empty = true) { onRotate { assert(empty = true) } }
            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun rankScreenContentList() = launch({ data.fillRank() }) {
        mainScreen {
            openRankPage { onRotate { assert(empty = false) } }
            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun rankScreenRenameDialog() = data.insertRank().let {
        val newName = data.uniqueString

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

    @Test fun notesScreenContentList() = launch({ data.fillNotes() }) {
        mainScreen {
            openNotesPage { onRotate { assert(empty = false) } }
            assert { onDisplayFab(visible = true) }
        }
    }

    @Test fun notesScreenTextNoteDialog() = data.insertText().let {
        launch { mainScreen { openNotesPage { openNoteDialog(it) { onRotate { assert() } } } } }
    }

    @Test fun notesScreenRollNoteDialog() = data.insertRoll().let {
        launch { mainScreen { openNotesPage { openNoteDialog(it) { onRotate { assert() } } } } }
    }


    @Test fun binScreenContentEmpty() = launch {
        mainScreen {
            openBinPage(empty = true) { onRotate { assert(empty = true) } }
            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun binScreenContentList() = launch({ data.fillBin() }) {
        mainScreen {
            openBinPage { onRotate { assert(empty = false) } }
            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun binScreenClearDialog() = launch({ data.fillBin() }) {
        mainScreen { openBinPage { openClearDialog { onRotate { assert() } } } }
    }

    @Test fun binScreenTextNoteDialog() = data.insertTextToBin().let {
        launch { mainScreen { openBinPage { openNoteDialog(it) { onRotate { assert() } } } } }
    }

    @Test fun binScreenRollNoteDialog() = data.insertRollToBin().let {
        launch { mainScreen { openBinPage { openNoteDialog(it) { onRotate { assert() } } } } }
    }


    @Test fun notificationScreenContentEmpty() = launch {
        mainScreen {
            openNotesPage(empty = true) {
                openNotification(empty = true) { onRotate { assert(empty = true) } }
            }
        }
    }

    @Test fun notificationScreenContentList() = launch({ data.fillNotification() }) {
        mainScreen { openNotesPage { openNotification { onRotate { assert(empty = false) } } } }
    }


    @Test fun alarmScreenContent() = data.insertText().let {
        launchAlarm(it) { openAlarm(it) { onRotate { assert() } } }
    }


    private fun onRotate(func: () -> Unit) = waitAfter(TIME) {
        func()
        testRule.activity?.runOnUiThread { context.showToast(text = "ROTATE NOW!") }
    }

    private companion object {
        const val TIME = 5000L
    }

}