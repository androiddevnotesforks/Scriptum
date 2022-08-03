package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.parent.ParentUiTest

/**
 * Test note dialog for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinNoteDialogTest : ParentUiTest() {

    @Test fun textDialogUntitled() = data.insertTextToBin(data.textNote.apply { name = "" }).let {
        launch { mainScreen { binScreen { openNoteDialog(it) } } }
    }

    @Test fun textDialogClose() = data.insertTextToBin().let {
        launch {
            mainScreen { binScreen { openNoteDialog(it) { onCloseSoft() }.assert(isEmpty = false) } }
        }
    }

    @Test fun textDialogRestore() = data.insertTextToBin().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)
                binScreen { openNoteDialog(it) { onRestore() }.assert(isEmpty = true) }
                notesScreen()
            }
        }
    }

    @Test fun textDialogClear() = data.insertTextToBin().let {
        launch {
            mainScreen {
                binScreen { openNoteDialog(it) { onClear() }.assert(isEmpty = true) }
                notesScreen(isEmpty = true)
            }
        }
    }


    @Test fun rollDialogUntitled() = data.insertRollToBin(data.rollNote.apply { name = "" }).let {
        launch { mainScreen { binScreen { openNoteDialog(it) } } }
    }

    @Test fun rollDialogClose() = data.insertRollToBin().let {
        launch {
            mainScreen { binScreen { openNoteDialog(it) { onCloseSoft() }.assert(isEmpty = false) } }
        }
    }

    @Test fun rollDialogRestore() = data.insertRollToBin().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)
                binScreen { openNoteDialog(it) { onRestore() }.assert(isEmpty = true) }
                notesScreen()
            }
        }
    }

    @Test fun rollDialogClear() = data.insertRollToBin().let {
        launch {
            mainScreen {
                binScreen { openNoteDialog(it) { onClear() }.assert(isEmpty = true) }
                notesScreen(isEmpty = true)
            }
        }
    }

}