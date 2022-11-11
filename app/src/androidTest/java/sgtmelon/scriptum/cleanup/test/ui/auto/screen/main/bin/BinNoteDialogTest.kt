package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test note dialog for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinNoteDialogTest : ParentUiTest() {

    @Test fun textDialogUntitled() = db.insertTextToBin(db.textNote.apply { name = "" }).let {
        launch { mainScreen { openBin { openNoteDialog(it) } } }
    }

    @Test fun textDialogClose() = db.insertTextToBin().let {
        launch {
            mainScreen {
                openBin {
                    openNoteDialog(it) { softClose() }
                    assert(isEmpty = false)
                }
            }
        }
    }

    @Test fun textDialogRestore() = db.insertTextToBin().let {
        launch {
            mainScreen {
                openNotes(isEmpty = true)
                openBin {
                    openNoteDialog(it) { onRestore() }
                    assert(isEmpty = true)
                }
                openNotes()
            }
        }
    }

    @Test fun textDialogClear() = db.insertTextToBin().let {
        launch {
            mainScreen {
                openBin {
                    openNoteDialog(it) { onClear() }
                    assert(isEmpty = true)
                }
                openNotes(isEmpty = true)
            }
        }
    }


    @Test fun rollDialogUntitled() = db.insertRollToBin(db.rollNote.apply { name = "" }).let {
        launch { mainScreen { openBin { openNoteDialog(it) } } }
    }

    @Test fun rollDialogClose() = db.insertRollToBin().let {
        launch {
            mainScreen {
                openBin {
                    openNoteDialog(it) { softClose() }
                    assert(isEmpty = false)
                }
            }
        }
    }

    @Test fun rollDialogRestore() = db.insertRollToBin().let {
        launch {
            mainScreen {
                openNotes(isEmpty = true)
                openBin {
                    openNoteDialog(it) { onRestore() }
                    assert(isEmpty = true)
                }
                openNotes()
            }
        }
    }

    @Test fun rollDialogClear() = db.insertRollToBin().let {
        launch {
            mainScreen {
                openBin {
                    openNoteDialog(it) { onClear() }
                    assert(isEmpty = true)
                }
                openNotes(isEmpty = true)
            }
        }
    }

}