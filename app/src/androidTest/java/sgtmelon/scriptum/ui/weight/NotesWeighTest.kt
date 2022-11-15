package sgtmelon.scriptum.ui.weight

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.tests.ParentUiWeighTest
import sgtmelon.scriptum.ui.auto.notes.startNotesItemTest
import sgtmelon.scriptum.ui.auto.notes.startNotesListTest
import sgtmelon.scriptum.ui.cases.ListScrollCase
import sgtmelon.scriptum.ui.cases.NoteOpenCase

/**
 * Weigh test for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesWeighTest : ParentUiWeighTest(),
    ListScrollCase,
    NoteOpenCase {

    @Test override fun listScroll() = startNotesListTest(ITEM_COUNT) {
        scrollTo(Scroll.END, SCROLL_COUNT)
    }

    @Test override fun itemTextOpen() = startNotesItemTest(db.insertText(dbWeight.textNote)) {
        repeat(REPEAT_COUNT) { _ -> openText(it) { toolbar { clickBack() } } }
    }

    @Test override fun itemRollOpen() = startNotesItemTest(
        db.insertRoll(isVisible = true, list = dbWeight.rollList)
    ) {
        repeat(REPEAT_COUNT) { _ -> openRoll(it) { toolbar { clickBack() } } }
    }
}