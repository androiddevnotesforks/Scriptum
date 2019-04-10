package sgtmelon.scriptum.test.main


import android.content.Intent
import androidx.test.espresso.Espresso.pressBack
import org.junit.Test
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.dialog.ClearDialog
import sgtmelon.scriptum.ui.dialog.NoteDialog
import sgtmelon.scriptum.ui.screen.main.BinScreen
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.main.NotesScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

class BinTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        preference.firstStart = false
    }

    @Test fun contentEmpty() {
        testData.clearAllData()

        testRule.launchActivity(Intent())

        MainScreen {
            navigateTo(MainPage.Name.BIN)
            BinScreen { assert { onDisplayContent(empty = true) } }
        }
    }

    @Test fun contentList() {
        testData.apply { clearAllData() }.fillBin()

        testRule.launchActivity(Intent())

        MainScreen {
            navigateTo(MainPage.Name.BIN)
            BinScreen { assert { onDisplayContent(empty = false) } }
        }
    }

    @Test fun scrollList() {
        testData.apply { clearAllData() }.fillBin(times = 20)

        testRule.launchActivity(Intent())

        MainScreen {
            navigateTo(MainPage.Name.BIN)
            BinScreen {
                onScroll(Scroll.END, time = 4)
                onScroll(Scroll.START, time = 4)
            }
        }
    }

    @Test fun openTextNote() {
        testData.apply { clearAllData() }.insertTextToBin()

        testRule.launchActivity(Intent())

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

    @Test fun openRollNote() {
        testData.apply { clearAllData() }.insertRollToBin()

        testRule.launchActivity(Intent())

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

    @Test fun deleteAllNotes() {
        testData.apply {
            clearAllData()
            repeat(times = 5) { insertTextToBin() }
            repeat(times = 5) { insertRollToBin() }
        }

        testRule.launchActivity(Intent())

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

    @Test fun showTextNoteDialog() {
        val noteItem = testData.apply { clearAllData() }.insertTextToBin()

        testRule.launchActivity(Intent())

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

    @Test fun showRollNoteDialog() {
        val noteItem = testData.apply { clearAllData() }.insertRollToBin()

        testRule.launchActivity(Intent())

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

    @Test fun restoreTextNote() {
        testData.apply { clearAllData() }.insertTextToBin()

        testRule.launchActivity(Intent())

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

    @Test fun restoreRollNote() {
        testData.apply { clearAllData() }.insertRollToBin()

        testRule.launchActivity(Intent())

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

    // TODO copy text

    @Test fun clearTextNote() {
        testData.apply { clearAllData() }.insertTextToBin()

        testRule.launchActivity(Intent())

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

    @Test fun clearRollNote() {
        testData.apply { clearAllData() }.insertRollToBin()

        testRule.launchActivity(Intent())

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