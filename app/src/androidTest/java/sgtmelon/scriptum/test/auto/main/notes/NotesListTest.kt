package sgtmelon.scriptum.test.auto.main.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test list for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesListTest : ParentUiTest() {

    @Test fun contentEmpty() = launch { mainScreen { notesScreen(empty = true) } }

    @Test fun contentList() = launch({ data.fillNotes() }) { mainScreen { notesScreen() } }

    @Test fun listScroll() = launch({ data.fillNotes() }) {
        mainScreen { notesScreen { onScrollThrough() } }
    }


    @Test fun textNoteOpen() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen { openTextNote(it) { onPressBack() }.assert(empty = false) }
            }
        }
    }

    @Test fun rollNoteOpen() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openRollNote(it) { onPressBack() }.assert(empty = false) }
            }
        }
    }


    @Test fun textCreateAndReturn() = data.createText().let {
        launch {
            mainScreen {
                notesScreen(empty = true)
                openAddDialog { createText(it) { onPressBack() } }
                notesScreen(empty = true)
            }
        }
    }

    @Test fun rollCreateAndReturn() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(empty = true)
                openAddDialog { createRoll(it) { onPressBack() } }
                notesScreen(empty = true)
            }
        }
    }

    @Test fun textCreateAndReturnWithSave() = data.createText().let {
        launch {
            mainScreen {
                notesScreen(empty = true)

                openAddDialog {
                    createText(it) {
                        data.insertText()
                        toolbar { onClickBack() }
                    }
                }

                notesScreen()
            }
        }
    }

    @Test fun rollCreateAndReturnWithSave() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(empty = true)

                openAddDialog {
                    createRoll(it) {
                        data.insertRoll()
                        toolbar { onClickBack() }
                    }
                }

                notesScreen()
            }
        }
    }

}