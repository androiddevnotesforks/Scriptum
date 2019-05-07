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
        beforeLaunch { testData.clearAllData() }

        MainScreen { binScreen { assert { onDisplayContent(empty = true) } } }
    }

    @Test fun contentList() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin() }

        MainScreen { binScreen { assert { onDisplayContent(empty = false) } } }
    }

    @Test fun listScroll() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin(times = 20) }

        MainScreen {
            binScreen {
                onScroll(Scroll.END, time = 4)
                onScroll(Scroll.START, time = 4)
            }
        }
    }

    @Test fun textNoteOpen() {
        beforeLaunch { testData.apply { clearAllData() }.insertTextToBin() }

        MainScreen {
            binScreen {
                assert { onDisplayContent(empty = false) }
                textNoteScreen { pressBack() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun rollNoteOpen() {
        beforeLaunch { testData.apply { clearAllData() }.insertRollToBin() }

        MainScreen {
            binScreen {
                assert { onDisplayContent(empty = false) }
                rollNoteScreen() { pressBack() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }


    @Test fun clearDialogOpen() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin(times = 20) }

        MainScreen { binScreen { clearDialogUi { assert { onDisplayContent() } } } }
    }

    @Test fun clearDialogCloseSoft() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin(times = 20) }

        MainScreen {
            binScreen {
                clearDialogUi { onCloseSoft() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun clearDialogCloseCancel() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin(times = 20) }

        MainScreen {
            binScreen {
                clearDialogUi { onClickNo() }
                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun clearDialogWork() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin(times = 20) }

        MainScreen {
            binScreen {
                assert { onDisplayContent(empty = false) }
                clearDialogUi { onClickYes() }
                assert { onDisplayContent(empty = true) }
            }
        }
    }


    @Test fun textNoteDialogOpen() {
        val noteItem = testData.apply { clearAllData() }.insertTextToBin()

        afterLaunch { MainScreen { binScreen { noteDialogUi(noteItem) } } }
    }

    @Test fun textNoteDialogClose() {
        val noteItem = testData.apply { clearAllData() }.insertTextToBin()

        afterLaunch {
            MainScreen {
                binScreen {
                    noteDialogUi(noteItem) { onCloseSoft() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun textNoteDialogRestore() {
        val noteItem = testData.apply { clearAllData() }.insertTextToBin()

        afterLaunch {
            MainScreen {
                notesScreen { assert { onDisplayContent(empty = true) } }

                binScreen {
                    assert { onDisplayContent(empty = false) }
                    noteDialogUi(noteItem) { onClickRestore() }
                    assert { onDisplayContent(empty = true) }
                }

                notesScreen { assert { onDisplayContent(empty = false) } }
            }
        }
    }

    @Test fun textNoteDialogClear() {
        val noteItem = testData.apply { clearAllData() }.insertTextToBin()

        afterLaunch {
            MainScreen {
                binScreen {
                    assert { onDisplayContent(empty = false) }
                    noteDialogUi(noteItem) { onClickClear() }
                    assert { onDisplayContent(empty = true) }
                }

                notesScreen { assert { onDisplayContent(empty = true) } }
            }
        }
    }


    @Test fun rollNoteDialogOpen() {
        val noteItem = testData.apply { clearAllData() }.insertRollToBin()

        afterLaunch { MainScreen { binScreen { noteDialogUi(noteItem) } } }
    }

    @Test fun rollNoteDialogClose() {
        val noteItem = testData.apply { clearAllData() }.insertRollToBin()

        afterLaunch {
            MainScreen {
                binScreen {
                    noteDialogUi(noteItem) { onCloseSoft() }
                    assert { onDisplayContent(empty = false) }
                }
            }
        }
    }

    @Test fun rollNoteDialogRestore() {
        val noteItem = testData.apply { clearAllData() }.insertRollToBin()

        afterLaunch {
            MainScreen {
                notesScreen { assert { onDisplayContent(empty = true) } }

                binScreen {
                    assert { onDisplayContent(empty = false) }
                    noteDialogUi(noteItem) { onClickRestore() }
                    assert { onDisplayContent(empty = true) }
                }

                notesScreen { assert { onDisplayContent(empty = false) } }
            }
        }
    }

    @Test fun rollNoteDialogClear() {
        val noteItem = testData.apply { clearAllData() }.insertRollToBin()

        afterLaunch {
            MainScreen {
                binScreen {
                    assert { onDisplayContent(empty = false) }
                    noteDialogUi(noteItem) { onClickClear() }
                    assert { onDisplayContent(empty = true) }
                }

                notesScreen { assert { onDisplayContent(empty = true) } }
            }
        }
    }

}