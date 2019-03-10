package sgtmelon.scriptum.test.main

import android.content.Intent
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.data.TestData
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.dialog.AddDialog
import sgtmelon.scriptum.ui.dialog.NoteDialog
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.main.NotesScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

@RunWith(AndroidJUnit4::class)
class NotesTest : ParentTest() {

    private val listAddNoteType: List<NoteType> = object : ArrayList<NoteType>() {
        init {
            add(NoteType.TEXT)
            add(NoteType.ROLL)
            add(NoteType.ROLL)
            add(NoteType.TEXT)
        }
    }

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = false
    }

    @Test fun testAddDialog() {
        db.apply { clearAllTables() }.close()
        testRule.launchActivity(Intent())

        MainScreen {
            assert { onDisplayContent() }

            for (noteType in listAddNoteType) {
                NotesScreen {
                    assert { onDisplayContent(empty = true) }

                    onClickFab()
                    AddDialog {
                        assert { onDisplayContent() }
                        onClickItem(noteType)
                    }

                    when (noteType) {
                        NoteType.TEXT -> {
                            TextNoteScreen {
                                assert { onDisplayContent(State.NEW) }
                                closeSoftKeyboard()
                                pressBack()
                            }
                        }
                        NoteType.ROLL -> {
                            RollNoteScreen {
                                assert { onDisplayContent(State.NEW) }
                                closeSoftKeyboard()
                                pressBack()
                            }
                        }
                    }

                    assert { onDisplayContent(empty = true) }
                }
            }
        }
    }

    @Test fun testAddTextNote() {
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
                    addNote(TestData(context).textNoteItem)
                    pressBack()
                }

                assert { onDisplayContent(empty = false) }
            }
        }
    }

    @Test fun testTextNoteDialogOpen() {
        db.clearAllTables()
        val noteItem = TestData(context).textNoteItem
        db.daoNote().insert(noteItem)

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

}