package sgtmelon.scriptum.ui.auto.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.ListContentCase
import sgtmelon.scriptum.ui.cases.ListScrollCase
import sgtmelon.scriptum.ui.cases.NoteOpenCase

/**
 * Test list for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesListTest : ParentUiTest(),
    ListContentCase,
    ListScrollCase,
    NoteOpenCase {

    @Test override fun contentEmpty() = launch { mainScreen { openNotes(isEmpty = true) } }

    @Test override fun contentList() = db.fillNotes().let {
        launch { mainScreen { openNotes { assertList(it) } } }
    }

    @Test override fun listScroll() = launch({ db.fillNotes() }) {
        mainScreen { openNotes { scrollThrough() } }
    }

    @Test override fun itemTextOpen() = db.insertText().let {
        launch {
            mainScreen {
                openNotes {
                    openText(it) { pressBack() }
                    assert(isEmpty = false)
                }
            }
        }
    }

    @Test override fun itemRollOpen() = db.insertRoll().let {
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