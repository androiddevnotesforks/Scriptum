package sgtmelon.scriptum.test.main

import android.content.Intent
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.data.Scroll
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

    @Test fun scrollList() {
        testData.apply {
            clearAllData()
            repeat(times = 10) { insertText() }
            repeat(times = 10) { insertRoll() }
        }

        testRule.launchActivity(Intent())

        MainScreen {
            assert {
                onDisplayContent()
                onDisplayFab(visible = true)
            }

            NotesScreen {
                assert { onDisplayContent(empty = false) }
                onScroll(Scroll.END, time = 4)
            }

            assert { onDisplayFab(visible = false) }

            NotesScreen {
                onScroll(Scroll.START, time = 4)
                assert { onDisplayContent(empty = false) }
            }

            assert { onDisplayFab(visible = true) }
        }
    }

    @Test fun openTextNote() {
        testData.apply {
            clearAllData()
            insertText()
        }

        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }

            NotesScreen {
                assert { onDisplayContent(empty = false) }

                onClickItem(position = 0)
                TextNoteScreen {
                    assert { onDisplayContent(State.READ) }
                    pressBack()
                }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun openRollNote() {
        testData.apply {
            clearAllData()
            insertRoll()
        }

        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }

            NotesScreen {
                assert { onDisplayContent(empty = false) }

                onClickItem(position = 0)
                RollNoteScreen {
                    assert { onDisplayContent(State.READ) }
                    pressBack()
                }

                assert { onDisplayContent(empty = false) }
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

                    testData.insertText()

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

                    testData.insertRoll()

                    closeSoftKeyboard()
                    pressBack()
                }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun showTextNoteDialog() {
        testData.clearAllData()
        val noteItem = testData.insertText()

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
        val noteItem = testData.insertRoll()

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

    fun bindTextNote() {
        // TODO (reasong = "не знаю как проверить")
        testData.clearAllData()
        val noteItem = testData.insertText()

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

    fun bindRollNote() {

    }

}