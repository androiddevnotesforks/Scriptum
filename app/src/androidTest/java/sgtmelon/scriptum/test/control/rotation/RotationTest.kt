package sgtmelon.scriptum.test.control.rotation

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.test.ParentRotationTest

/**
 * Test of application work with phone rotation
 */
@RunWith(AndroidJUnit4::class)
class RotationTest : ParentRotationTest() {

    @Test fun addDialog() = launch { mainScreen { openAddDialog { onRotate { assert() } } } }

    /**
     * RankScreen
     */

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
                        onRename(newName, enabled = true)
                        onRotate { assert(newName, enabled = true) }
                    }
                }
            }
        }
    }

    /**
     * NotesScreen
     */

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

    /**
     * BinScreen
     */

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

    /**
     * Notification / alarm
     */

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

}