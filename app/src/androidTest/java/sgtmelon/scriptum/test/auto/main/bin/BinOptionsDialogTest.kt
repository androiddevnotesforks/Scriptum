package sgtmelon.scriptum.test.auto.main.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test dialog options for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinOptionsDialogTest : ParentUiTest() {

    @Test fun textDialogClose() = data.insertTextToBin().let {
        launch {
            mainScreen { binScreen { openNoteDialog(it) { onCloseSoft() }.assert(empty = false) } }
        }
    }

    @Test fun textDialogRestore() = data.insertTextToBin().let {
        launch {
            mainScreen {
                notesScreen(empty = true)
                binScreen { openNoteDialog(it) { onRestore() }.assert(empty = true) }
                notesScreen()
            }
        }
    }

    @Test fun textDialogClear() = data.insertTextToBin().let {
        launch {
            mainScreen {
                binScreen { openNoteDialog(it) { onClear() }.assert(empty = true) }
                notesScreen(empty = true)
            }
        }
    }


    @Test fun rollDialogClose() = data.insertRollToBin().let {
        launch {
            mainScreen { binScreen { openNoteDialog(it) { onCloseSoft() }.assert(empty = false) } }
        }
    }

    @Test fun rollDialogRestore() = data.insertRollToBin().let {
        launch {
            mainScreen {
                notesScreen(empty = true)
                binScreen { openNoteDialog(it) { onRestore() }.assert(empty = true) }
                notesScreen()
            }
        }
    }

    @Test fun rollDialogClear() = data.insertRollToBin().let {
        launch {
            mainScreen {
                binScreen { openNoteDialog(it) { onClear() }.assert(empty = true) }
                notesScreen(empty = true)
            }
        }
    }

}