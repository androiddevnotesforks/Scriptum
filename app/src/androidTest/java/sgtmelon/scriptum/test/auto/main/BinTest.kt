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

    @Test fun textNoteOpen() = testData.insertTextNoteToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openTextNote(it) { onPressBack() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun rollNoteOpen() = testData.insertRollNoteToBin().let {
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

    @Test fun clearDialogWorkWithHideNotes() = testData.insertRankToBin().let {
        launch({ testData.insertTextToBin() }) {
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


    @Test fun textNoteDialogOpen() = testData.insertTextNoteToBin().let {
        launch { mainScreen { openBinPage { openNoteDialog(it) } } }
    }

    @Test fun textNoteDialogClose() = testData.insertTextNoteToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openNoteDialog(it) { onCloseSoft() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun textNoteDialogRestore() = testData.insertTextNoteToBin().let {
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

    @Test fun textNoteDialogClear() = testData.insertTextNoteToBin().let {
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


    @Test fun rollNoteDialogOpen() = testData.insertRollNoteToBin().let {
        launch { mainScreen { openBinPage { openNoteDialog(it) } } }
    }

    @Test fun rollNoteDialogClose() = testData.insertRollNoteToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openNoteDialog(it) { onCloseSoft() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun rollNoteDialogRestore() = testData.insertRollNoteToBin().let {
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

    @Test fun rollNoteDialogClear() = testData.insertRollNoteToBin().let {
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