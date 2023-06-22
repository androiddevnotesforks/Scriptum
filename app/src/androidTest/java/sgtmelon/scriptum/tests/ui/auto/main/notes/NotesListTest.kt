package sgtmelon.scriptum.tests.ui.auto.main.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.source.ui.tests.launchMain
import sgtmelon.scriptum.source.ui.tests.launchNotes
import sgtmelon.scriptum.source.ui.tests.launchNotesItem
import sgtmelon.scriptum.source.ui.tests.launchNotesList
import sgtmelon.scriptum.source.cases.list.ListContentCase
import sgtmelon.scriptum.source.cases.list.ListScrollCase
import sgtmelon.scriptum.source.cases.note.NoteOpenCase
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest

/**
 * Test list for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesListTest : ParentUiRotationTest(),
    ListContentCase,
    ListScrollCase,
    NoteOpenCase {

    @Test override fun contentEmpty() = launchNotes(isEmpty = true)

    @Test override fun contentList() = launchNotesList { assertList(it) }

    @Test override fun contentRotateEmpty() = launchMain {
        openNotes(isEmpty = true) {
            rotate.toSide()
            assert(isEmpty = true)
        }
        assert(isFabVisible = true)
    }

    @Test override fun contentRotateList() = db.fillNotes().let {
        launchMain {
            openNotes {
                rotate.toSide()
                assert(isEmpty = false)
                assertList(it)
            }
            assert(isFabVisible = true)
        }
    }

    @Test override fun listScroll() = launchNotesList { scrollThrough() }

    @Test override fun itemTextOpen() = launchNotesItem(db.insertText()) {
        openText(it) { pressBack() }
        assert(isEmpty = false)
    }

    @Test override fun itemRollOpen() = launchNotesItem(db.insertRoll()) {
        openRoll(it) { pressBack() }
        assert(isEmpty = false)
    }


    @Test fun textCreateAndReturn() = launchMain {
        openNotes(isEmpty = true)
        openAddDialog { createText({ db.createText() }) { pressBack() } }
        openNotes(isEmpty = true)
    }

    @Test fun rollCreateAndReturn() = launchMain {
        openNotes(isEmpty = true)
        openAddDialog { createRoll({ db.createRoll() }) { pressBack() } }
        openNotes(isEmpty = true)
    }

    @Test fun textCreateAndReturnWithSave() = launchMain {
        openNotes(isEmpty = true)

        var item: NoteItem.Text? = null
        openAddDialog {
            createText({ db.createText() }) {
                item = db.insertText()
                toolbar { clickBack() }
            }
        }

        openNotes { assertItem(item!!) }
    }

    @Test fun rollCreateAndReturnWithSave() = launchMain {
        openNotes(isEmpty = true)

        var item: NoteItem.Roll? = null
        openAddDialog {
            createRoll({ db.createRoll() }) {
                item = db.insertRoll()
                toolbar { clickBack() }
            }
        }

        openNotes { assertItem(item!!) }
    }
}