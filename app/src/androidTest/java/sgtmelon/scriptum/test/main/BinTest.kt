package sgtmelon.scriptum.test.main


import androidx.test.espresso.Espresso.pressBack
import org.junit.Test
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.screen.view.main.BinFragment
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.screen.main.MainScreen

/**
 * Тест работы [BinFragment]
 *
 * @author SerjantArbuz
 */
class BinTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        preference.firstStart = false
    }

    @Test fun contentEmpty() {
        beforeLaunch { testData.clear() }

        MainScreen { openBinPage { assert { onDisplayContent(empty = true) } } }
    }

    @Test fun contentList() {
        beforeLaunch { testData.clear().fillBin() }

        MainScreen { openBinPage { assert { onDisplayContent(empty = false) } } }
    }

    @Test fun listScroll() {
        beforeLaunch { testData.clear().fillBin(times = 20) }

        MainScreen {
            openBinPage {
                onScroll(Scroll.END, time = 4)
                onScroll(Scroll.START, time = 4)
            }
        }
    }

    @Test fun textNoteOpen() {
        beforeLaunch { testData.clear().insertTextToBin() }

        MainScreen {
            openBinPage {
                assert { onDisplayContent(empty = false) }
                openTextNote { pressBack() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun rollNoteOpen() {
        beforeLaunch { testData.clear().insertRollToBin() }

        MainScreen {
            openBinPage {
                assert { onDisplayContent(empty = false) }
                openRollNote() { pressBack() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }


    @Test fun clearDialogOpen() {
        beforeLaunch { testData.clear().fillBin(times = 20) }

        MainScreen { openBinPage { openClearDialog { assert { onDisplayContent() } } } }
    }

    @Test fun clearDialogCloseSoft() {
        beforeLaunch { testData.clear().fillBin(times = 20) }

        MainScreen {
            openBinPage {
                openClearDialog { onCloseSoft() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun clearDialogCloseCancel() {
        beforeLaunch { testData.clear().fillBin(times = 20) }

        MainScreen {
            openBinPage {
                openClearDialog { onClickNo() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun clearDialogWork() {
        beforeLaunch { testData.clear().fillBin(times = 20) }

        MainScreen {
            openBinPage {
                assert { onDisplayContent(empty = false) }
                openClearDialog { onClickYes() }
                assert { onDisplayContent(empty = true) }
            }
        }
    }


    @Test fun textNoteDialogOpen() {
        val noteItem = testData.clear().insertTextToBin()

        afterLaunch { MainScreen { openBinPage { openNoteDialog(noteItem) } } }
    }

    @Test fun textNoteDialogClose() {
        val noteItem = testData.clear().insertTextToBin()

        afterLaunch {
            MainScreen {
                openBinPage {
                    openNoteDialog(noteItem) { onCloseSoft() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun textNoteDialogRestore() {
        val noteItem = testData.clear().insertTextToBin()

        afterLaunch {
            MainScreen {
                openNotesPage { assert { onDisplayContent(empty = true) } }

                openBinPage {
                    assert { onDisplayContent(empty = false) }
                    openNoteDialog(noteItem) { onClickRestore() }
                    assert { onDisplayContent(empty = true) }
                }

                openNotesPage { assert { onDisplayContent(empty = false) } }
            }
        }
    }

    @Test fun textNoteDialogClear() {
        val noteItem = testData.clear().insertTextToBin()

        afterLaunch {
            MainScreen {
                openBinPage {
                    assert { onDisplayContent(empty = false) }
                    openNoteDialog(noteItem) { onClickClear() }
                    assert { onDisplayContent(empty = true) }
                }

                openNotesPage { assert { onDisplayContent(empty = true) } }
            }
        }
    }


    @Test fun rollNoteDialogOpen() {
        val noteItem = testData.clear().insertRollToBin()

        afterLaunch { MainScreen { openBinPage { openNoteDialog(noteItem) } } }
    }

    @Test fun rollNoteDialogClose() {
        val noteItem = testData.clear().insertRollToBin()

        afterLaunch {
            MainScreen {
                openBinPage {
                    openNoteDialog(noteItem) { onCloseSoft() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun rollNoteDialogRestore() {
        val noteItem = testData.clear().insertRollToBin()

        afterLaunch {
            MainScreen {
                openNotesPage { assert { onDisplayContent(empty = true) } }

                openBinPage {
                    assert { onDisplayContent(empty = false) }
                    openNoteDialog(noteItem) { onClickRestore() }
                    assert { onDisplayContent(empty = true) }
                }

                openNotesPage { assert { onDisplayContent(empty = false) } }
            }
        }
    }

    @Test fun rollNoteDialogClear() {
        val noteItem = testData.clear().insertRollToBin()

        afterLaunch {
            MainScreen {
                openBinPage {
                    assert { onDisplayContent(empty = false) }
                    openNoteDialog(noteItem) { onClickClear() }
                    assert { onDisplayContent(empty = true) }
                }

                openNotesPage { assert { onDisplayContent(empty = true) } }
            }
        }
    }

}