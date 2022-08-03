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

    @Test fun textDialogUntitled() = db.insertTextToBin(db.textNote.apply { name = "" }).let {
        launch { mainScreen { binScreen { openNoteDialog(it) } } }
    }

    @Test fun textDialogClose() = db.insertTextToBin().let {
        launch {
            mainScreen { binScreen { openNoteDialog(it) { onCloseSoft() }.assert(isEmpty = false) } }
        }
    }

    @Test fun textDialogRestore() = db.insertTextToBin().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)
                binScreen { openNoteDialog(it) { onRestore() }.assert(isEmpty = true) }
                notesScreen()
            }
        }
    }

    @Test fun textDialogClear() = db.insertTextToBin().let {
        launch {
            mainScreen {
                binScreen { openNoteDialog(it) { onClear() }.assert(isEmpty = true) }
                notesScreen(isEmpty = true)
            }
        }
    }


    @Test fun rollDialogUntitled() = db.insertRollToBin(db.rollNote.apply { name = "" }).let {
        launch { mainScreen { binScreen { openNoteDialog(it) } } }
    }

    @Test fun rollDialogClose() = db.insertRollToBin().let {
        launch {
            mainScreen { binScreen { openNoteDialog(it) { onCloseSoft() }.assert(isEmpty = false) } }
        }
    }

    @Test fun rollDialogRestore() = db.insertRollToBin().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)
                binScreen { openNoteDialog(it) { onRestore() }.assert(isEmpty = true) }
                notesScreen()
            }
        }
    }

    @Test fun rollDialogClear() = db.insertRollToBin().let {
        launch {
            mainScreen {
                binScreen { openNoteDialog(it) { onClear() }.assert(isEmpty = true) }
                notesScreen(isEmpty = true)
            }
        }
    }

}