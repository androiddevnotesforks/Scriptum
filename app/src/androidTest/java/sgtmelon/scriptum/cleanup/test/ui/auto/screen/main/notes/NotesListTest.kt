package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.parent.ui.ParentUiTest
import sgtmelon.scriptum.parent.ui.launch

/**
 * Test list for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesListTest : ParentUiTest() {

    @Test fun contentEmpty() = launch { mainScreen { notesScreen(isEmpty = true) } }

    @Test fun contentList() = launch({ db.fillNotes() }) { mainScreen { notesScreen() } }

    @Test fun listScroll() = launch({ db.fillNotes() }) {
        mainScreen { notesScreen { onScrollThrough() } }
    }


    @Test fun textNoteOpen() = db.insertText().let {
        launch {
            mainScreen {
                notesScreen { openTextNote(it) { pressBack() }.assert(isEmpty = false) }
            }
        }
    }

    @Test fun rollNoteOpen() = db.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openRollNote(it) { pressBack() }.assert(isEmpty = false) }
            }
        }
    }


    @Test fun textCreateAndReturn() = db.createText().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)
                openAddDialog { createText(it) { pressBack() } }
                notesScreen(isEmpty = true)
            }
        }
    }

    @Test fun rollCreateAndReturn() = db.createRoll().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)
                openAddDialog { createRoll(it) { pressBack() } }
                notesScreen(isEmpty = true)
            }
        }
    }

    @Test fun textCreateAndReturnWithSave() = db.createText().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)

                openAddDialog {
                    createText(it) {
                        db.insertText()
                        toolbar { clickBack() }
                    }
                }

                notesScreen()
            }
        }
    }

    @Test fun rollCreateAndReturnWithSave() = db.createRoll().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)

                openAddDialog {
                    createRoll(it) {
                        db.insertRoll()
                        toolbar { clickBack() }
                    }
                }

                notesScreen()
            }
        }
    }

}