package sgtmelon.scriptum.test.main

import android.content.Intent
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.data.TestData
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.dialog.AddDialog
import sgtmelon.scriptum.ui.dialog.NoteDialog
import sgtmelon.scriptum.ui.screen.PreferenceScreen
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.main.NotesScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

@RunWith(AndroidJUnit4::class)
class NotesTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = false
    }

    @Test fun openPreference() {
        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }

            NotesScreen {
                onClickPreference()
                PreferenceScreen { assert { onDisplayContent() } }
            }
        }
    }

    @Test fun showTextNoteInListAfterCreate() {
        db.apply { clearAllTables() }.close()
        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }

            NotesScreen {
                assert { onDisplayContent(empty = true) }

                onClickFab()
                AddDialog {
                    assert { onDisplayContent() }
                    onClickItem(NoteType.TEXT)
                }

                TextNoteScreen {
                    db.apply { daoNote().insert(TestData(context).textNoteItem) }.close()

                    closeSoftKeyboard()
                    pressBack()
                }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun showRollNoteInListAfterCreate() {
        TODO (reason = "create rollNote insert in TestData")

        db.apply { clearAllTables() }.close()
        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }

            NotesScreen {
                assert { onDisplayContent(empty = true) }

                onClickFab()
                AddDialog {
                    assert { onDisplayContent() }
                    onClickItem(NoteType.TEXT)
                }

                RollNoteScreen {
//                    db.apply { daoNote().insert(TestData(context).textNoteItem) }.close() TODO

                    closeSoftKeyboard()
                    pressBack()
                }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun showTextNoteDialog() {
        val noteItem = TestData(context).textNoteItem
        db.apply {
            clearAllTables()
            daoNote().insert(noteItem)
        }.close()

        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }

            NotesScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem(position = 0)
                NoteDialog { assert { onDisplayContent(noteItem) } }
                pressBack()

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun showRollNoteDialog() {
        TODO (reason = "create rollNote insert in TestData")
    }

}