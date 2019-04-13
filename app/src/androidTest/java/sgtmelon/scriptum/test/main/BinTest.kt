package sgtmelon.scriptum.test.main


import androidx.test.espresso.Espresso.pressBack
import org.junit.Test
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.view.main.BinFragment
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

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

        MainScreen {
            navigateTo(MainPage.BIN)
            binScreen { assert { onDisplayContent(empty = true) } }
        }
    }

    @Test fun contentList() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin() }

        MainScreen {
            navigateTo(MainPage.BIN)
            binScreen { assert { onDisplayContent(empty = false) } }
        }
    }

    @Test fun listScroll() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin(times = 20) }

        MainScreen {
            navigateTo(MainPage.BIN)
            binScreen {
                onScroll(Scroll.END, time = 4)
                onScroll(Scroll.START, time = 4)
            }
        }
    }

    @Test fun textNoteOpen() {
        beforeLaunch { testData.apply { clearAllData() }.insertTextToBin() }

        MainScreen {
            navigateTo(MainPage.BIN)

            binScreen {
                assert { onDisplayContent(empty = false) }

                onClickItem(position = 0)
                TextNoteScreen {
                    assert { onDisplayContent(State.BIN) }
                    pressBack()
                }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun rollNoteOpen() {
        beforeLaunch { testData.apply { clearAllData() }.insertRollToBin() }

        MainScreen {
            navigateTo(MainPage.BIN)

            binScreen {
                assert { onDisplayContent(empty = false) }

                onClickItem(position = 0)
                RollNoteScreen {
                    assert { onDisplayContent(State.BIN) }
                    pressBack()
                }

                assert { onDisplayContent(empty = false) }
            }
        }
    }


    @Test fun clearDialogOpen() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin(times = 20) }

        MainScreen {
            navigateTo(MainPage.BIN)

            binScreen {
                onClickClearBin()
                clearDialog { assert { onDisplayContent() } }
            }
        }
    }

    @Test fun clearDialogCloseSoft() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin(times = 20) }

        MainScreen {
            navigateTo(MainPage.BIN)

            binScreen {
                onClickClearBin()
                clearDialog { onCloseSoft() }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun clearDialogCloseCancel() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin(times = 20) }

        MainScreen {
            navigateTo(MainPage.BIN)

            binScreen {
                onClickClearBin()
                clearDialog { onClickNo() }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun clearDialogWork() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin(times = 20) }

        MainScreen {
            navigateTo(MainPage.BIN)

            binScreen {
                assert { onDisplayContent(empty = false) }

                onClickClearBin()
                clearDialog { onClickYes() }

                assert { onDisplayContent(empty = true) }
            }
        }
    }


    @Test fun textNoteDialogOpen() {
        val noteItem = testData.apply { clearAllData() }.insertTextToBin()

        launch()

        MainScreen {
            navigateTo(MainPage.BIN)

            binScreen {
                onLongClickItem(position = 0)
                noteDialog { assert { onDisplayContent(noteItem) } }
            }
        }
    }

    @Test fun textNoteDialogClose() {
        beforeLaunch { testData.apply { clearAllData() }.insertTextToBin() }

        MainScreen {
            navigateTo(MainPage.BIN)

            binScreen {
                onLongClickItem(position = 0)
                noteDialog { onCloseSoft() }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun textNoteDialogRestore() {
        beforeLaunch { testData.apply { clearAllData() }.insertTextToBin() }

        MainScreen {
            notesScreen { assert { onDisplayContent(empty = true) } }

            navigateTo(MainPage.BIN)

            binScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem(position = 0)
                noteDialog { onClickRestore() }

                assert { onDisplayContent(empty = true) }
            }

            navigateTo(MainPage.NOTES)

            notesScreen { assert { onDisplayContent(empty = false) } }
        }
    }

    @Test fun textNoteDialogClear() {
        beforeLaunch { testData.apply { clearAllData() }.insertTextToBin() }

        MainScreen {
            navigateTo(MainPage.BIN)

            binScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem(position = 0)
                noteDialog { onClickClear() }

                Thread.sleep(300)
                assert { onDisplayContent(empty = true) }
            }

            navigateTo(MainPage.NOTES)

            notesScreen { assert { onDisplayContent(empty = true) } }
        }
    }


    @Test fun rollNoteDialogOpen() {
        val noteItem = testData.apply { clearAllData() }.insertRollToBin()

        launch()

        MainScreen {
            navigateTo(MainPage.BIN)

            binScreen {
                onLongClickItem(position = 0)
                noteDialog { assert { onDisplayContent(noteItem) } }
            }
        }
    }

    @Test fun rollNoteDialogClose() {
        beforeLaunch { testData.apply { clearAllData() }.insertRollToBin() }

        MainScreen {
            navigateTo(MainPage.BIN)

            binScreen {
                onLongClickItem(position = 0)
                noteDialog { onCloseSoft() }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun rollNoteDialogRestore() {
        beforeLaunch { testData.apply { clearAllData() }.insertRollToBin() }

        MainScreen {
            notesScreen { assert { onDisplayContent(empty = true) } }

            navigateTo(MainPage.BIN)

            binScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem(position = 0)
                noteDialog { onClickRestore() }

                assert { onDisplayContent(empty = true) }
            }

            navigateTo(MainPage.NOTES)

            notesScreen { assert { onDisplayContent(empty = false) } }
        }
    }

    @Test fun rollNoteDialogClear() {
        beforeLaunch { testData.apply { clearAllData() }.insertRollToBin() }

        MainScreen {
            navigateTo(MainPage.BIN)

            binScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem(position = 0)
                noteDialog { onClickClear() }

                Thread.sleep(300)
                assert { onDisplayContent(empty = true) }
            }

            navigateTo(MainPage.NOTES)

            notesScreen { assert { onDisplayContent(empty = true) } }
        }
    }

}