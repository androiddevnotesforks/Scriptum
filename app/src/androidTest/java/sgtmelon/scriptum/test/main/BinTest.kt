package sgtmelon.scriptum.test.main

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.model.key.MainPage
import sgtmelon.scriptum.data.TestData
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.dialog.ClearDialog
import sgtmelon.scriptum.ui.dialog.NoteDialog
import sgtmelon.scriptum.ui.screen.main.BinScreen
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.main.NotesScreen

@RunWith(AndroidJUnit4::class)
class BinTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = false
    }

    @Test fun testClearBin() {
        db.apply {
            clearAllTables()

            with(TestData(context).textNoteItem) {
                isBin = true
                repeat(times = 5) { daoNote().insert(this) }
            }
        }.close()

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

    @Test fun testNoteRestore() {
        val noteItem = TestData(context).textNoteItem.apply { isBin = true }
        db.apply {
            clearAllTables()
            daoNote().insert(noteItem)
        }.close()

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

    @Test fun testNoteClear() {
        val noteItem = TestData(context).textNoteItem.apply { isBin = true }
        db.apply {
            clearAllTables()
            daoNote().insert(noteItem)
        }.close()

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