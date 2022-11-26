package sgtmelon.scriptum.ui.auto.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
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

    @Test override fun contentEmpty() = launchSplash { mainScreen { openNotes(isEmpty = true) } }

    @Test override fun contentList() = startNotesListTest { assertList(it) }

    @Test override fun listScroll() = startNotesListTest { scrollThrough() }

    @Test override fun itemTextOpen() = startNotesItemTest(db.insertText()) {
        openText(it) { pressBack() }
        assert(isEmpty = false)
    }

    @Test override fun itemRollOpen() = startNotesItemTest(db.insertRoll()) {
        openRoll(it) { pressBack() }
        assert(isEmpty = false)
    }


    @Test fun textCreateAndReturn() = db.createText().let {
        launchSplash {
            mainScreen {
                openNotes(isEmpty = true)
                openAddDialog { createText(it) { pressBack() } }
                openNotes(isEmpty = true)
            }
        }
    }

    @Test fun rollCreateAndReturn() = db.createRoll().let {
        launchSplash {
            mainScreen {
                openNotes(isEmpty = true)
                openAddDialog { createRoll(it) { pressBack() } }
                openNotes(isEmpty = true)
            }
        }
    }

    @Test fun textCreateAndReturnWithSave() = db.createText().let {
        launchSplash {
            mainScreen {
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
    }

    @Test fun rollCreateAndReturnWithSave() = db.createRoll().let {
        launchSplash {
            mainScreen {
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
}