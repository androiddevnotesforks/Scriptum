package sgtmelon.scriptum.test.main


import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.model.key.MainPage
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.dialog.ClearDialog
import sgtmelon.scriptum.ui.dialog.NoteDialog
import sgtmelon.scriptum.ui.screen.main.BinScreen
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.main.NotesScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen


@RunWith(AndroidJUnit4::class)
class BinTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = false
    }

    @Test fun scrollList() {
        testData.apply {
            clearAllData()
            repeat(times = 10) { insertTextToBin() }
            repeat(times = 10) { insertRollToBin() }
        }

        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }
            navigateTo(MainPage.Name.BIN)

            BinScreen {
                assert { onDisplayContent(empty = false) }

                onScroll(Scroll.END, time = 4)
                onScroll(Scroll.START, time = 4)

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun openTextNote() {
        testData.apply {
            clearAllData()
            insertTextToBin()
        }

        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }
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
        testData.apply {
            clearAllData()
            insertRollToBin()
        }

        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }
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
            assert { onDisplayContent() }
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
        testData.clearAllData()
        val noteItem = testData.insertTextToBin()

        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }
            navigateTo(MainPage.Name.BIN)

            BinScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem(position = 0)
                NoteDialog { assert { onDisplayContent(noteItem) } }
                pressBack()

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun showRollNoteDialog() {
        testData.clearAllData()
        val noteItem = testData.insertRollToBin()

        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }
            navigateTo(MainPage.Name.BIN)

            BinScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem(position = 0)
                NoteDialog { assert { onDisplayContent(noteItem) } }
                pressBack()

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun restoreTextNote() {
        testData.clearAllData()
        val noteItem = testData.insertTextToBin()

        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }

            navigateTo(MainPage.Name.BIN)
            BinScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem()
                NoteDialog {
                    assert { onDisplayContent(noteItem) }
                    onClickRestore()
                }

                assert { onDisplayContent(empty = true) }
            }

            navigateTo(MainPage.Name.NOTES)
            NotesScreen { assert { onDisplayContent(empty = false) } }
        }
    }

    @Test fun restoreRollNote() {
        testData.clearAllData()
        val noteItem = testData.insertRollToBin()

        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }

            navigateTo(MainPage.Name.BIN)
            BinScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem()
                NoteDialog {
                    assert { onDisplayContent(noteItem) }
                    onClickRestore()
                }

                assert { onDisplayContent(empty = true) }
            }

            navigateTo(MainPage.Name.NOTES)
            NotesScreen { assert { onDisplayContent(empty = false) } }
        }
    }

    // TODO (compare text)
    fun copyTextNote() {
        testData.clearAllData()
        val noteItem = testData.insertTextToBin()

        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }

            navigateTo(MainPage.Name.BIN)
            BinScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem()
                NoteDialog {
                    assert { onDisplayContent(noteItem) }
                    onClickCopy()
                }

                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE)
                        as ClipboardManager?
            }
        }
    }

    // TODO (compare text)
    fun copyRollNote() {
        testData.clearAllData()
        val noteItem = testData.insertRollToBin()

        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }

            navigateTo(MainPage.Name.BIN)
            BinScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem()
                NoteDialog {
                    assert { onDisplayContent(noteItem) }
                    onClickCopy()
                }

                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE)
                        as ClipboardManager?
            }
        }
    }

    @Test fun clearTextNote() {
        testData.clearAllData()
        val noteItem = testData.insertTextToBin()

        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }

            navigateTo(MainPage.Name.BIN)
            BinScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem()
                NoteDialog {
                    assert { onDisplayContent(noteItem) }
                    onClickClear()
                }

                Thread.sleep(300)
                assert { onDisplayContent(empty = true) }
            }

            navigateTo(MainPage.Name.NOTES)
            NotesScreen { assert { onDisplayContent(empty = true) } }
        }
    }

    @Test fun clearRollNote() {
        testData.clearAllData()
        val noteItem = testData.insertRollToBin()

        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }

            navigateTo(MainPage.Name.BIN)
            BinScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem()
                NoteDialog {
                    assert { onDisplayContent(noteItem) }
                    onClickClear()
                }

                Thread.sleep(300)
                assert { onDisplayContent(empty = true) }
            }

            navigateTo(MainPage.Name.NOTES)
            NotesScreen { assert { onDisplayContent(empty = true) } }
        }
    }

}