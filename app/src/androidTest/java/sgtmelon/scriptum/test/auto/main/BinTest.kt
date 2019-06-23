package sgtmelon.scriptum.test.auto.main


import org.junit.Test
import sgtmelon.scriptum.screen.view.main.BinFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Тест работы [BinFragment]
 *
 * @author SerjantArbuz
 */
class BinTest : ParentUiTest() {

    override fun setUp() {
        super.setUp()

        iPreferenceRepo.firstStart = false
        testData.clear()
    }

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
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun rollNoteOpen() = testData.insertRollToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openRollNote(it) { onPressBack() }
                    assert { onDisplayContent(empty = false) }
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
                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun clearDialogCloseCancel() = launch({ testData.fillBin() }) {
        mainScreen {
            openBinPage {
                openClearDialog { onClickNo() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun clearDialogWork() = launch({ testData.fillBin() }) {
        mainScreen {
            openBinPage {
                openClearDialog { onClickYes() }
                assert { onDisplayContent(empty = true) }
            }
        }
    }

    @Test fun clearDialogWorkWithHideNotes() = testData.fillRankForBin().let {
        launch({ testData.fillBin(count = 5) }) {
            mainScreen {
                openRankPage { onClickVisible(it[0]) }

                openBinPage {
                    openClearDialog { onClickYes() }
                    assert { onDisplayContent(empty = true) }
                }

                openRankPage { onClickVisible(it[0]) }
                openBinPage()
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
                    assert { onDisplayContent(empty = false) }
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
                    assert { onDisplayContent(empty = true) }
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
                    assert { onDisplayContent(empty = true) }
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
                    assert { onDisplayContent(empty = false) }
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
                    assert { onDisplayContent(empty = true) }
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
                    assert { onDisplayContent(empty = true) }
                }

                openNotesPage(empty = true)
            }
        }
    }

}