package sgtmelon.scriptum.test.auto.main


import org.junit.Test
import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [BinFragment]
 */
class BinTest : ParentUiTest() {

    @Test fun contentEmpty() = launch { mainScreen { binScreen(empty = true) } }

    @Test fun contentList() = launch({ data.fillBin() }) { mainScreen { binScreen() } }

    @Test fun listScroll() = launch({ data.fillBin() }) {
        mainScreen { binScreen { onScrollThrough() } }
    }

    @Test fun textNoteOpen() = data.insertTextToBin().let {
        launch {
            mainScreen { binScreen { openTextNote(it) { onPressBack() }.assert(empty = false) } }
        }
    }

    @Test fun rollNoteOpen() = data.insertRollToBin().let {
        launch {
            mainScreen { binScreen { openRollNote(it) { onPressBack() }.assert(empty = false) } }
        }
    }


    @Test fun clearDialogOpen() = launch({ data.fillBin() }) {
        mainScreen { binScreen { clearDialog() } }
    }

    @Test fun clearDialogCloseAndWork() = launch({ data.fillBin() }) {
        mainScreen {
            binScreen {
                clearDialog { onCloseSoft() }.assert(empty = false)
                clearDialog { onClickNo() }.assert(empty = false)
                clearDialog { onClickYes() }.assert(empty = true)
            }
        }
    }

    @Test fun clearDialogWorkWithHideNotes() = data.insertRankForBin().let {
        launch({ data.fillBin(count = 5) }) {
            mainScreen {
                binScreen()

                rankScreen { onClickVisible() }
                binScreen { clearDialog { onClickYes() }.assert(empty = true) }

                rankScreen { onClickVisible() }
                binScreen(empty = true)
            }
        }
    }


    @Test fun textNoteDialogOpen() = data.insertTextToBin().let {
        launch { mainScreen { binScreen { openNoteDialog(it) } } }
    }

    @Test fun textNoteDialogClose() = data.insertTextToBin().let {
        launch {
            mainScreen { binScreen { openNoteDialog(it) { onCloseSoft() }.assert(empty = false) } }
        }
    }

    @Test fun textNoteDialogRestore() = data.insertTextToBin().let {
        launch {
            mainScreen {
                notesScreen(empty = true)
                binScreen { openNoteDialog(it) { onRestore() }.assert(empty = true) }
                notesScreen()
            }
        }
    }

    @Test fun textNoteDialogClear() = data.insertTextToBin().let {
        launch {
            mainScreen {
                binScreen { openNoteDialog(it) { onClear() }.assert(empty = true) }
                notesScreen(empty = true)
            }
        }
    }


    @Test fun rollNoteDialogOpen() = data.insertRollToBin().let {
        launch { mainScreen { binScreen { openNoteDialog(it) } } }
    }

    @Test fun rollNoteDialogClose() = data.insertRollToBin().let {
        launch {
            mainScreen { binScreen { openNoteDialog(it) { onCloseSoft() }.assert(empty = false) } }
        }
    }

    @Test fun rollNoteDialogRestore() = data.insertRollToBin().let {
        launch {
            mainScreen {
                notesScreen(empty = true)
                binScreen { openNoteDialog(it) { onRestore() }.assert(empty = true) }
                notesScreen()
            }
        }
    }

    @Test fun rollNoteDialogClear() = data.insertRollToBin().let {
        launch {
            mainScreen {
                binScreen { openNoteDialog(it) { onClear() }.assert(empty = true) }
                notesScreen(empty = true)
            }
        }
    }

}