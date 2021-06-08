package sgtmelon.scriptum.test.ui.auto.screen.main.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.basic.extension.waitBefore
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.domain.model.key.MainPage
import sgtmelon.scriptum.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test fab for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesFabTest : ParentUiTest() {

    @Test fun onScrollAndPageChange() = launch({ data.fillNotes(count = 45) }) {
        mainScreen {
            for (it in listOf(MainPage.RANK, MainPage.BIN)) {
                notesScreen { onScroll(Scroll.END, time = 5) }
                assert(isFabVisible = false)
                notesScreen { onScroll(Scroll.START, time = 1) }
                assert(isFabVisible = true)
                notesScreen { onScroll(Scroll.START, time = 2) }
                onNavigateTo(it)
                assert(isFabVisible = false)
                onNavigateTo(MainPage.NOTES)
            }
        }
    }

    @Test fun onResume() = launch({ data.fillNotes() }) {
        mainScreen {
            notesScreen { onScroll(Scroll.END, time = 1) }
            assert(isFabVisible = false)
            notesScreen { openPreference { onClickClose() } }
            assert(isFabVisible = true)
        }
    }

    @Test fun standstill() = launch({ data.fillNotes() }) {
        mainScreen {
            notesScreen { onScroll(Scroll.END, time = 1) }
            assert(isFabVisible = false)
            waitBefore(NotesFragment.FAB_STANDSTILL_TIME) { assert(isFabVisible = true) }

            onScrollTop()
            notesScreen { onScroll(Scroll.END, time = 1) }
            assert(isFabVisible = false)
            onNavigateTo(MainPage.BIN)
            waitBefore(NotesFragment.FAB_STANDSTILL_TIME) { assert(isFabVisible = false) }
        }
    }

}