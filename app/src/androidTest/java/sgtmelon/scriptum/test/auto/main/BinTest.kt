package sgtmelon.scriptum.test.auto.main


import androidx.test.espresso.Espresso.pressBack
import org.junit.Test
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.screen.view.main.BinFragment
import sgtmelon.scriptum.test.ParentTest

/**
 * Тест работы [BinFragment]
 *
 * @author SerjantArbuz
 */
class BinTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        iPreferenceRepo.firstStart = false
    }

    @Test fun contentEmpty() = launch({ testData.clear() }) {
        mainScreen { openBinPage { assert { onDisplayContent(empty = true) } } }
    }

    @Test fun contentList() = launch({ testData.clear().fillBin() }) {
        mainScreen { openBinPage { assert { onDisplayContent(empty = false) } } }
    }

    @Test fun listScroll() = launch({ testData.clear().fillBin(times = 20) }) {
        mainScreen {
            openBinPage {
                onScroll(Scroll.END, time = 4)
                onScroll(Scroll.START, time = 4)
            }
        }
    }

    @Test fun textNoteOpen() = launch({ testData.clear().insertTextToBin() }) {
        mainScreen {
            openBinPage {
                assert { onDisplayContent(empty = false) }
                openTextNote { pressBack() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun rollNoteOpen() = launch({ testData.clear().insertRollToBin() }) {
        mainScreen {
            openBinPage {
                assert { onDisplayContent(empty = false) }
                openRollNote() { pressBack() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }


    @Test fun clearDialogOpen() = launch({ testData.clear().fillBin(times = 20) }) {
        mainScreen { openBinPage { openClearDialog { assert { onDisplayContent() } } } }
    }

    @Test fun clearDialogCloseSoft() = launch({ testData.clear().fillBin(times = 20) }) {
        mainScreen {
            openBinPage {
                openClearDialog { onCloseSoft() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun clearDialogCloseCancel() = launch({ testData.clear().fillBin(times = 20) }) {
        mainScreen {
            openBinPage {
                openClearDialog { onClickNo() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun clearDialogWork() = launch({ testData.clear().fillBin(times = 20) }) {
        mainScreen {
            openBinPage {
                assert { onDisplayContent(empty = false) }
                openClearDialog { onClickYes() }
                assert { onDisplayContent(empty = true) }
            }
        }
    }

    @Test fun clearDialogWorkWithHideNotes() {
        val rankList = testData.clear().insertRankToBin()

        launch({testData.insertTextToBin()}) {
            mainScreen {
                openRankPage { onClickVisible(rankList[0].name) }

                openBinPage {
                    assert { onDisplayContent(empty = false) }
                    openClearDialog { onClickYes() }
                    assert { onDisplayContent(empty = true) }
                }

                openRankPage { onClickVisible(rankList[0].name) }

                openBinPage { assert { onDisplayContent(empty = false) } }
            }
        }
    }


    @Test fun textNoteDialogOpen() {
        val noteEntity = testData.clear().insertTextToBin()

        launch { mainScreen { openBinPage { openNoteDialog(noteEntity) } } }
    }

    @Test fun textNoteDialogClose() {
        val noteEntity = testData.clear().insertTextToBin()

        launch {
            mainScreen {
                openBinPage {
                    openNoteDialog(noteEntity) { onCloseSoft() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun textNoteDialogRestore() {
        val noteEntity = testData.clear().insertTextToBin()

        launch {
            mainScreen {
                openNotesPage { assert { onDisplayContent(empty = true) } }

                openBinPage {
                    assert { onDisplayContent(empty = false) }
                    openNoteDialog(noteEntity) { onClickRestore() }
                    assert { onDisplayContent(empty = true) }
                }

                openNotesPage { assert { onDisplayContent(empty = false) } }
            }
        }
    }

    @Test fun textNoteDialogClear() {
        val noteEntity = testData.clear().insertTextToBin()

        launch {
            mainScreen {
                openBinPage {
                    assert { onDisplayContent(empty = false) }
                    openNoteDialog(noteEntity) { onClickClear() }
                    assert { onDisplayContent(empty = true) }
                }

                openNotesPage { assert { onDisplayContent(empty = true) } }
            }
        }
    }


    @Test fun rollNoteDialogOpen() {
        val noteEntity = testData.clear().insertRollToBin()

        launch { mainScreen { openBinPage { openNoteDialog(noteEntity) } } }
    }

    @Test fun rollNoteDialogClose() {
        val noteEntity = testData.clear().insertRollToBin()

        launch {
            mainScreen {
                openBinPage {
                    openNoteDialog(noteEntity) { onCloseSoft() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun rollNoteDialogRestore() {
        val noteEntity = testData.clear().insertRollToBin()

        launch {
            mainScreen {
                openNotesPage { assert { onDisplayContent(empty = true) } }

                openBinPage {
                    assert { onDisplayContent(empty = false) }
                    openNoteDialog(noteEntity) { onClickRestore() }
                    assert { onDisplayContent(empty = true) }
                }

                openNotesPage { assert { onDisplayContent(empty = false) } }
            }
        }
    }

    @Test fun rollNoteDialogClear() {
        val noteEntity = testData.clear().insertRollToBin()

        launch {
            mainScreen {
                openBinPage {
                    assert { onDisplayContent(empty = false) }
                    openNoteDialog(noteEntity) { onClickClear() }
                    assert { onDisplayContent(empty = true) }
                }

                openNotesPage { assert { onDisplayContent(empty = true) } }
            }
        }
    }

}