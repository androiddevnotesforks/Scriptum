package sgtmelon.scriptum.ui.auto.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.parent.ui.tests.launchMain
import sgtmelon.scriptum.parent.ui.tests.launchNotes
import sgtmelon.scriptum.parent.ui.tests.launchNotesItem
import sgtmelon.scriptum.parent.ui.tests.launchNotesList
import sgtmelon.scriptum.ui.cases.NoteOpenCase
import sgtmelon.scriptum.ui.cases.list.ListContentCase
import sgtmelon.scriptum.ui.cases.list.ListScrollCase

/**
 * Test list for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesListTest : ParentUiTest(),
    ListContentCase,
    ListScrollCase,
    NoteOpenCase {

    @Test override fun contentEmpty() = launchNotes(isEmpty = true)

    @Test override fun contentList() = launchNotesList { assertList(it) }

    @Test override fun listScroll() = launchNotesList { scrollThrough() }

    @Test override fun itemTextOpen() = launchNotesItem(db.insertText()) {
        openText(it) { pressBack() }
        assert(isEmpty = false)
    }

    @Test override fun itemRollOpen() = launchNotesItem(db.insertRoll()) {
        openRoll(it) { pressBack() }
        assert(isEmpty = false)
    }


    @Test fun textCreateAndReturn() = db.createText().let {
        launchMain {
            openNotes(isEmpty = true)
            openAddDialog { createText(it) { pressBack() } }
            openNotes(isEmpty = true)
        }
    }

    @Test fun rollCreateAndReturn() = db.createRoll().let {
        launchMain {
            openNotes(isEmpty = true)
            openAddDialog { createRoll(it) { pressBack() } }
            openNotes(isEmpty = true)
        }
    }

    @Test fun textCreateAndReturnWithSave() = db.createText().let {
        launchMain {
            openNotes(isEmpty = true)

            var item: NoteItem.Text? = null
            openAddDialog {
                createText(it) {
                    item = db.insertText()
                    toolbar { clickBack() }
                }
            }

            openNotes { assertItem(item!!) }
        }
    }

    @Test fun rollCreateAndReturnWithSave() = db.createRoll().let {
        launchMain {
            openNotes(isEmpty = true)

            var item: NoteItem.Roll? = null
            openAddDialog {
                createRoll(it) {
                    item = db.insertRoll()
                    toolbar { clickBack() }
                }
            }

            openNotes { assertItem(item!!) }
        }
    }
}