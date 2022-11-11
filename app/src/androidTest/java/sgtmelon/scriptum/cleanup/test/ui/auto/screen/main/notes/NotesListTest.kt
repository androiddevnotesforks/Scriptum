package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test list for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesListTest : ParentUiTest() {

    @Test fun contentEmpty() = launch { mainScreen { openNotes(isEmpty = true) } }

    @Test fun contentList() = launch({ db.fillNotes() }) { mainScreen { openNotes() } }

    @Test fun listScroll() = launch({ db.fillNotes() }) {
        mainScreen { openNotes { scrollThrough() } }
    }


    @Test fun textNoteOpen() = db.insertText().let {
        launch {
            mainScreen {
                openNotes {
                    openText(it) { pressBack() }
                    assert(isEmpty = false)
                }
            }
        }
    }

    @Test fun rollNoteOpen() = db.insertRoll().let {
        launch {
            mainScreen {
                openNotes {
                    openRoll(it) { pressBack() }
                    assert(isEmpty = false)
                }
            }
        }
    }


    @Test fun textCreateAndReturn() = db.createText().let {
        launch {
            mainScreen {
                openNotes(isEmpty = true)
                openAddDialog { createText(it) { pressBack() } }
                openNotes(isEmpty = true)
            }
        }
    }

    @Test fun rollCreateAndReturn() = db.createRoll().let {
        launch {
            mainScreen {
                openNotes(isEmpty = true)
                openAddDialog { createRoll(it) { pressBack() } }
                openNotes(isEmpty = true)
            }
        }
    }

    @Test fun textCreateAndReturnWithSave() = db.createText().let {
        launch {
            mainScreen {
                openNotes(isEmpty = true)

                openAddDialog {
                    createText(it) {
                        db.insertText()
                        toolbar { clickBack() }
                    }
                }

                openNotes()
            }
        }
    }

    @Test fun rollCreateAndReturnWithSave() = db.createRoll().let {
        launch {
            mainScreen {
                openNotes(isEmpty = true)

                openAddDialog {
                    createRoll(it) {
                        db.insertRoll()
                        toolbar { clickBack() }
                    }
                }

                openNotes()
            }
        }
    }

}