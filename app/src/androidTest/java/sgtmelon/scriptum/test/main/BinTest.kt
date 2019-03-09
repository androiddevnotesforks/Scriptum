package sgtmelon.scriptum.test.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.model.key.MainPage
import sgtmelon.scriptum.app.screen.splash.SplashActivity
import sgtmelon.scriptum.data.TestData
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.dialog.clear.ClearDialogUi
import sgtmelon.scriptum.ui.dialog.note.NoteDialogUi
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.main.bin.BinScreen
import sgtmelon.scriptum.ui.screen.main.notes.NotesScreen

@RunWith(AndroidJUnit4::class)
class BinTest : ParentTest() {

    @get:Rule val testRule = ActivityTestRule(SplashActivity::class.java)

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = false
    }

    @Test fun testClearBin() {
        db.clearAllTables()
        with(TestData(context).textNoteItem) {
            isBin = true
            repeat(times = 5) {
                db.daoNote().insert(this)
            }
        }

        MainScreen {
            assert { onDisplayContent() }
            navigateTo(MainPage.Name.BIN)

            BinScreen {
                assert { onDisplayContent(empty = false) }

                onClickClearBin()
                ClearDialogUi {
                    assert { onDisplayContent() }
                    onClickNo()
                }

                assert { onDisplayContent(empty = false) }

                onClickClearBin()
                ClearDialogUi {
                    assert { onDisplayContent() }
                    onClickYes()
                }

                assert { onDisplayContent(empty = true) }
            }
        }
    }

    @Test fun testNoteRestore() {
        db.clearAllTables()

        val noteItem = TestData(context).textNoteItem.apply { isBin = true }
        db.daoNote().insert(noteItem)

        MainScreen {
            assert { onDisplayContent() }

            navigateTo(MainPage.Name.BIN)
            BinScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem()
                NoteDialogUi {
                    assert { onDisplayContent(noteItem) }
                    onClickRestore()
                }

                assert { onDisplayContent(empty = true) }
            }

            navigateTo(MainPage.Name.NOTES)
            NotesScreen { assert { onDisplayContent(empty = false) } }
        }
    }

    @Test fun testNoteClear() {
        db.clearAllTables()

        val noteItem = TestData(context).textNoteItem.apply { isBin = true }
        db.daoNote().insert(noteItem)

        MainScreen {
            assert { onDisplayContent() }

            navigateTo(MainPage.Name.BIN)
            BinScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem()
                NoteDialogUi {
                    assert { onDisplayContent(noteItem) }
                    onClickClear()
                }

                assert { onDisplayContent(empty = true) }
            }

            navigateTo(MainPage.Name.NOTES)
            NotesScreen { assert { onDisplayContent(empty = true) } }
        }
    }

}