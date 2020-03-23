package sgtmelon.scriptum.test.control.rotation

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.test.ParentRotationTest

/**
 * Test of [MainActivity] work with phone rotation
 */
@RunWith(AndroidJUnit4::class)
class MainRotationTest : ParentRotationTest() {

    @Test fun addDialog() = launch { mainScreen { openAddDialog { onRotate { assert() } } } }

    /**
     * RankScreen
     */

    @Test fun rankContentEmpty() = launch {
        mainScreen {
            rankScreen(empty = true) { onRotate { assert(empty = true) } }
            assert(fabVisible = false)
        }
    }

    @Test fun rankContentList() = launch({ data.fillRank() }) {
        mainScreen { rankScreen { onRotate { assert(empty = false) } }.assert(fabVisible = false) }
    }

    @Test fun rankRenameDialog() = data.insertRank().let {
        val newName = data.uniqueString

        launch {
            mainScreen {
                rankScreen {
                    openRenameDialog(it.name) {
                        onEnter(newName)
                        onRotate { assert(newName) }
                    }
                }
            }
        }
    }

    /**
     * NotesScreen
     */

    @Test fun notesContentEmpty() = launch {
        mainScreen {
            notesScreen(empty = true) { onRotate { assert(empty = true) } }
            assert(fabVisible = true)
        }
    }

    @Test fun notesContentList() = launch({ data.fillNotes() }) {
        mainScreen { notesScreen { onRotate { assert(empty = false) } }.assert(fabVisible = true) }
    }

    @Test fun notesTextNoteDialog() = data.insertText().let {
        launch { mainScreen { notesScreen { openNoteDialog(it) { onRotate { assert() } } } } }
    }

    @Test fun notesRollNoteDialog() = data.insertRoll().let {
        launch { mainScreen { notesScreen { openNoteDialog(it) { onRotate { assert() } } } } }
    }

    @Test fun dateDialog() = data.insertNote().let { startDateDialogTest(it) }

    @Test fun dateDialogReset() = data.insertNotification(data.insertNote()).let {
        startDateDialogTest(it)
    }

    private fun startDateDialogTest(noteItem: NoteItem) {
        launch {
            mainScreen {
                notesScreen {
                    openNoteDialog(noteItem) { onNotification { onRotate { assert() } } }
                }
            }
        }
    }

    @Test fun timeDialog() = data.insertNote().let {
        launch {
            mainScreen {
                notesScreen {
                    openNoteDialog(it) {
                        onNotification { onClickApply { onRotate { assert() } } }
                    }
                }
            }
        }
    }

    /**
     * BinScreen
     */

    @Test fun binContentEmpty() = launch {
        mainScreen {
            binScreen(empty = true) { onRotate { assert(empty = true) } }
            assert(fabVisible = false)
        }
    }

    @Test fun binContentList() = launch({ data.fillBin() }) {
        mainScreen { binScreen { onRotate { assert(empty = false) } }.assert(fabVisible = false) }
    }

    @Test fun binClearDialog() = launch({ data.fillBin() }) {
        mainScreen { binScreen { clearDialog { onRotate { assert() } } } }
    }

    @Test fun binTextNoteDialog() = data.insertTextToBin().let {
        launch { mainScreen { binScreen { openNoteDialog(it) { onRotate { assert() } } } } }
    }

    @Test fun binRollNoteDialog() = data.insertRollToBin().let {
        launch { mainScreen { binScreen { openNoteDialog(it) { onRotate { assert() } } } } }
    }

}