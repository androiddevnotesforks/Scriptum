package sgtmelon.scriptum.test.main

import android.content.Intent
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.data.State
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
                    assert { onDisplayContent(State.NEW) }

                    testData.insertTextNote()

                    closeSoftKeyboard()
                    pressBack()
                }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun showRollNoteInListAfterCreate() {
        db.apply { clearAllTables() }.close()
        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }

            NotesScreen {
                assert { onDisplayContent(empty = true) }

                onClickFab()
                AddDialog {
                    assert { onDisplayContent() }
                    onClickItem(NoteType.ROLL)
                }

                RollNoteScreen {
                    assert { onDisplayContent(State.NEW) }

                    testData.insertRollNote()

                    closeSoftKeyboard()
                    pressBack()
                }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun showTextNoteDialog() {
        testData.clearAllData()
        val noteItem = testData.insertTextNote()

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
        testData.clearAllData()
        val noteItem = testData.insertRollNote()

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

    @Test fun bindTextNote() {
        // TODO (reasong = "не знаю как проверить")
        testData.clearAllData()
        val noteItem = testData.insertTextNote()

        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }

            NotesScreen {
                assert { onDisplayContent(empty = false) }

                onLongClickItem(position = 0)
                NoteDialog {
                    assert { onDisplayContent(noteItem) }

                }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun bindRollNote() {

    }

}