package sgtmelon.scriptum.test.control.rotation

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.main.MainActivity
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
            openRankPage(empty = true) { onRotate { assert(empty = true) } }
            assert(fabVisible = false)
        }
    }

    @Test fun rankContentList() = launch({ data.fillRank() }) {
        mainScreen {
            openRankPage { onRotate { assert(empty = false) } }
            assert(fabVisible = false)
        }
    }

    @Test fun rankRenameDialog() = data.insertRank().let {
        val newName = data.uniqueString

        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(it.name) {
                        onEnter(newName)
                        onRotate { assert(newName, enabled = true) }
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
            openNotesPage(empty = true) { onRotate { assert(empty = true) } }
            assert(fabVisible = true)
        }
    }

    @Test fun notesContentList() = launch({ data.fillNotes() }) {
        mainScreen {
            openNotesPage { onRotate { assert(empty = false) } }
            assert(fabVisible = true)
        }
    }

    @Test fun notesTextNoteDialog() = data.insertText().let {
        launch { mainScreen { openNotesPage { openNoteDialog(it) { onRotate { assert() } } } } }
    }

    @Test fun notesRollNoteDialog() = data.insertRoll().let {
        launch { mainScreen { openNotesPage { openNoteDialog(it) { onRotate { assert() } } } } }
    }

    /**
     * BinScreen
     */

    @Test fun binContentEmpty() = launch {
        mainScreen {
            openBinPage(empty = true) { onRotate { assert(empty = true) } }
            assert(fabVisible = false)
        }
    }

    @Test fun binContentList() = launch({ data.fillBin() }) {
        mainScreen {
            openBinPage { onRotate { assert(empty = false) } }
            assert(fabVisible = false)
        }
    }

    @Test fun binClearDialog() = launch({ data.fillBin() }) {
        mainScreen { openBinPage { openClearDialog { onRotate { assert() } } } }
    }

    @Test fun binTextNoteDialog() = data.insertTextToBin().let {
        launch { mainScreen { openBinPage { openNoteDialog(it) { onRotate { assert() } } } } }
    }

    @Test fun binRollNoteDialog() = data.insertRollToBin().let {
        launch { mainScreen { openBinPage { openNoteDialog(it) { onRotate { assert() } } } } }
    }

}