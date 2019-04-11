package sgtmelon.scriptum.test.main


import androidx.test.espresso.Espresso.pressBack
import org.junit.Test
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.view.main.BinFragment
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.dialog.ClearDialog
import sgtmelon.scriptum.ui.dialog.NoteDialog
import sgtmelon.scriptum.ui.screen.main.BinScreen
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.main.NotesScreen
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
            navigateTo(MainPage.Name.BIN)
            BinScreen { assert { onDisplayContent(empty = true) } }
        }
    }

    @Test fun contentList() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin() }

        MainScreen {
            navigateTo(MainPage.Name.BIN)
            BinScreen { assert { onDisplayContent(empty = false) } }
        }
    }

    @Test fun listScroll() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin(times = 20) }

        MainScreen {
            navigateTo(MainPage.Name.BIN)
            BinScreen {
                onScroll(Scroll.END, time = 4)
                onScroll(Scroll.START, time = 4)
            }
        }
    }

    @Test fun textNoteOpen() {
        beforeLaunch { testData.apply { clearAllData() }.insertTextToBin() }

        MainScreen {
            navigateTo(MainPage.Name.BIN)

            BinScreen {
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
            navigateTo(MainPage.Name.BIN)

            BinScreen {
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

    @Test fun clearDialogWork() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin(times = 20) }

        MainScreen {
            navigateTo(MainPage.Name.BIN)

            BinScreen {
                assert { onDisplayContent(empty = false) }

                onClickClearBin()
                ClearDialog {
                    assert { onDisplayContent() }
                    onClickNo()
                }

                assert { onDisplayContent(empty = false) }

                onClickClearBin()
                ClearDialog {
                    assert { onDisplayContent() }
                    onClickYes()
                }

                assert { onDisplayContent(empty = true) }
            }
        }
    }


    @Test fun textNoteDialogOpen() {
        val noteItem = testData.apply { clearAllData() }.insertTextToBin()

        launch()

        MainScreen {
            navigateTo(MainPage.Name.BIN)

            BinScreen {
                onLongClickItem(position = 0)
                NoteDialog {
                    assert { onDisplayContent(noteItem) }
                    pressBack()
                }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun textNoteDialogRestore() {
        beforeLaunch { testData.apply { clearAllData() }.insertTextToBin() }

        MainScreen {
            NotesScreen { assert { onDisplayContent(empty = true) } }

            navigateTo(MainPage.Name.BIN)

            BinScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem(position = 0)
                NoteDialog { onClickRestore() }

                assert { onDisplayContent(empty = true) }
            }

            navigateTo(MainPage.Name.NOTES)

            NotesScreen { assert { onDisplayContent(empty = false) } }
        }
    }

    @Test fun textNoteDialogCopy() {
        TODO("not create")
    }

    @Test fun textNoteDialogClear() {
        beforeLaunch { testData.apply { clearAllData() }.insertTextToBin() }

        MainScreen {
            navigateTo(MainPage.Name.BIN)

            BinScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem(position = 0)
                NoteDialog { onClickClear() }

                Thread.sleep(300)
                assert { onDisplayContent(empty = true) }
            }

            navigateTo(MainPage.Name.NOTES)

            NotesScreen { assert { onDisplayContent(empty = true) } }
        }
    }


    @Test fun rollNoteDialogOpen() {
        val noteItem = testData.apply { clearAllData() }.insertRollToBin()

        launch()

        MainScreen {
            navigateTo(MainPage.Name.BIN)

            BinScreen {
                onLongClickItem(position = 0)
                NoteDialog {
                    assert { onDisplayContent(noteItem) }
                    pressBack()
                }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun rollNoteDialogRestore() {
        beforeLaunch { testData.apply { clearAllData() }.insertRollToBin() }

        MainScreen {
            NotesScreen { assert { onDisplayContent(empty = true) } }

            navigateTo(MainPage.Name.BIN)

            BinScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem(position = 0)
                NoteDialog { onClickRestore() }

                assert { onDisplayContent(empty = true) }
            }

            navigateTo(MainPage.Name.NOTES)

            NotesScreen { assert { onDisplayContent(empty = false) } }
        }
    }

    @Test fun rollNoteDialogCopy() {
        TODO("not create")
    }

    @Test fun rollNoteDialogClear() {
        beforeLaunch { testData.apply { clearAllData() }.insertRollToBin() }

        MainScreen {
            navigateTo(MainPage.Name.BIN)

            BinScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem(position = 0)
                NoteDialog { onClickClear() }

                Thread.sleep(300)
                assert { onDisplayContent(empty = true) }
            }

            navigateTo(MainPage.Name.NOTES)

            NotesScreen { assert { onDisplayContent(empty = true) } }
        }
    }

}