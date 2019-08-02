package sgtmelon.scriptum.test.auto.main


import org.junit.Test
import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Тест работы [BinFragment]
 *
 * @author SerjantArbuz
 */
class BinTest : ParentUiTest() {

    @Test fun contentEmpty() = launch { mainScreen { openBinPage(empty = true) } }

    @Test fun contentList() = launch({ testData.fillBin() }) { mainScreen { openBinPage() } }

    @Test fun listScroll() = launch({ testData.fillBin() }) {
        mainScreen { openBinPage { onScrollThrough() } }
    }

    @Test fun textNoteOpen() = testData.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openTextNote(it) { onPressBack() }
                    assert(empty = false)
                }
            }
        }
    }

    @Test fun rollNoteOpen() = testData.insertRollToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openRollNote(it) { onPressBack() }
                    assert(empty = false)
                }
            }
        }
    }


    @Test fun clearDialogOpen() = launch({ testData.fillBin() }) {
        mainScreen { openBinPage { openClearDialog() } }
    }

    @Test fun clearDialogCloseSoft() = launch({ testData.fillBin() }) {
        mainScreen {
            openBinPage {
                openClearDialog { onCloseSoft() }
                assert(empty = false)
            }
        }
    }

    @Test fun clearDialogCloseCancel() = launch({ testData.fillBin() }) {
        mainScreen {
            openBinPage {
                openClearDialog { onClickNo() }
                assert(empty = false)
            }
        }
    }

    @Test fun clearDialogWork() = launch({ testData.fillBin() }) {
        mainScreen {
            openBinPage {
                openClearDialog { onClickYes() }
                assert(empty = true)
            }
        }
    }

    @Test fun clearDialogWorkWithHideNotes() = testData.insertRankForBin().let {
        launch({ testData.fillBin(count = 5) }) {
            mainScreen {
                openBinPage()
                openRankPage { onClickVisible(it) }

                openBinPage() {
                    openClearDialog { onClickYes() }
                    assert(empty = true)
                }

                openRankPage { onClickVisible(it) }
                openBinPage(empty = true)
            }
        }
    }


    @Test fun textNoteDialogOpen() = testData.insertTextToBin().let {
        launch { mainScreen { openBinPage { openNoteDialog(it) } } }
    }

    @Test fun textNoteDialogClose() = testData.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openNoteDialog(it) { onCloseSoft() }
                    assert(empty = false)
                }
            }
        }
    }

    @Test fun textNoteDialogRestore() = testData.insertTextToBin().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openBinPage {
                    openNoteDialog(it) { onClickRestore() }
                    assert(empty = true)
                }

                openNotesPage()
            }
        }
    }

    @Test fun textNoteDialogClear() = testData.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openNoteDialog(it) { onClickClear() }
                    assert(empty = true)
                }

                openNotesPage(empty = true)
            }
        }
    }


    @Test fun rollNoteDialogOpen() = testData.insertRollToBin().let {
        launch { mainScreen { openBinPage { openNoteDialog(it) } } }
    }

    @Test fun rollNoteDialogClose() = testData.insertRollToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openNoteDialog(it) { onCloseSoft() }
                    assert(empty = false)
                }
            }
        }
    }

    @Test fun rollNoteDialogRestore() = testData.insertRollToBin().let {
        launch {
            mainScreen {
                openNotesPage(empty = true)

                openBinPage {
                    openNoteDialog(it) { onClickRestore() }
                    assert(empty = true)
                }

                openNotesPage()
            }
        }
    }

    @Test fun rollNoteDialogClear() = testData.insertRollToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openNoteDialog(it) { onClickClear() }
                    assert(empty = true)
                }

                openNotesPage(empty = true)
            }
        }
    }

}