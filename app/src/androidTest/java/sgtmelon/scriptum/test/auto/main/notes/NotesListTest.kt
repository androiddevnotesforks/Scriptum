package sgtmelon.scriptum.test.auto.main.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test list for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesListTest : ParentUiTest() {

    @Test fun contentEmpty() = launch { mainScreen { notesScreen(isEmpty = true) } }

    @Test fun contentList() = launch({ data.fillNotes() }) { mainScreen { notesScreen() } }

    @Test fun listScroll() = launch({ data.fillNotes() }) {
        mainScreen { notesScreen { onScrollThrough() } }
    }


    @Test fun textNoteOpen() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen { openTextNote(it) { onPressBack() }.assert(isEmpty = false) }
            }
        }
    }

    @Test fun rollNoteOpen() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openRollNote(it) { onPressBack() }.assert(isEmpty = false) }
            }
        }
    }


    @Test fun textCreateAndReturn() = data.createText().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)
                openAddDialog { createText(it) { onPressBack() } }
                notesScreen(isEmpty = true)
            }
        }
    }

    @Test fun rollCreateAndReturn() = data.createRoll().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)
                openAddDialog { createRoll(it) { onPressBack() } }
                notesScreen(isEmpty = true)
            }
        }
    }

    @Test fun textCreateAndReturnWithSave() = data.createText().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)

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
                notesScreen(isEmpty = true)

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