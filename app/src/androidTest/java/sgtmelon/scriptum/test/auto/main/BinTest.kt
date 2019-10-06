package sgtmelon.scriptum.test.auto.main


import org.junit.Test
import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [BinFragment]
 */
class BinTest : ParentUiTest() {

    @Test fun contentEmpty() = launch { mainScreen { openBinPage(empty = true) } }

    @Test fun contentList() = launch({ data.fillBin() }) { mainScreen { openBinPage() } }

    @Test fun listScroll() = launch({ data.fillBin() }) {
        mainScreen { openBinPage { onScrollThrough() } }
    }

    @Test fun textNoteOpen() = data.insertTextToBin().let {
        launch {
            mainScreen { openBinPage { openTextNote(it) { onPressBack() }.assert(empty = false) } }
        }
    }

    @Test fun rollNoteOpen() = data.insertRollToBin().let {
        launch {
            mainScreen { openBinPage { openRollNote(it) { onPressBack() }.assert(empty = false) } }
        }
    }


    @Test fun clearDialogOpen() = launch({ data.fillBin() }) {
        mainScreen { openBinPage { openClearDialog() } }
    }

    @Test fun clearDialogCloseAndWork() = launch({ data.fillBin() }) {
        mainScreen {
            openBinPage {
                openClearDialog { onCloseSoft() }.assert(empty = false)
                openClearDialog { onClickNo() }.assert(empty = false)
                openClearDialog { onClickYes() }.assert(empty = true)
            }
        }
    }

    @Test fun clearDialogWorkWithHideNotes() = data.insertRankForBin().let {
        launch({ data.fillBin(count = 5) }) {
            mainScreen {
                openBinPage()

                openRankPage { onClickVisible() }
                openBinPage { openClearDialog { onClickYes() }.assert(empty = true) }

                openRankPage { onClickVisible() }
                openBinPage(empty = true)
            }
        }
    }


    @Test fun textNoteDialogOpen() = data.insertTextToBin().let {
        launch { mainScreen { openBinPage { openNoteDialog(it) } } }
    }

    @Test fun textNoteDialogClose() = data.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage { openNoteDialog(it) { onCloseSoft() }.assert(empty = false) }
            }
        }
    }

    @Test fun textNoteDialogRestore() = data.insertTextToBin().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)
                openBinPage { openNoteDialog(it) { onClickRestore() }.assert(empty = true) }
                openNotesPage()
            }
        }
    }

    @Test fun textNoteDialogClear() = data.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage { openNoteDialog(it) { onClickClear() }.assert(empty = true) }
                openNotesPage(empty = true)
            }
        }
    }


    @Test fun rollNoteDialogOpen() = data.insertRollToBin().let {
        launch { mainScreen { openBinPage { openNoteDialog(it) } } }
    }

    @Test fun rollNoteDialogClose() = data.insertRollToBin().let {
        launch {
            mainScreen {
                openBinPage { openNoteDialog(it) { onCloseSoft() }.assert(empty = false) }
            }
        }
    }

    @Test fun rollNoteDialogRestore() = data.insertRollToBin().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)
                openBinPage { openNoteDialog(it) { onClickRestore() }.assert(empty = true) }
                openNotesPage()
            }
        }
    }

    @Test fun rollNoteDialogClear() = data.insertRollToBin().let {
        launch {
            mainScreen {
                openBinPage { openNoteDialog(it) { onClickClear() }.assert(empty = true) }
                openNotesPage(empty = true)
            }
        }
    }

}