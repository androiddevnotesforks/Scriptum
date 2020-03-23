package sgtmelon.scriptum.test.auto.main.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.basic.extension.waitBefore
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test fab for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesFabTest : ParentUiTest() {

    @Test fun onScrollAndPageChange() = launch({ data.fillNotes(count = 45) }) {
        mainScreen {
            listOf(MainPage.RANK, MainPage.BIN).forEach { pageTo ->
                notesScreen { onScroll(Scroll.END, time = 5) }
                assert(fabVisible = false)
                notesScreen { onScroll(Scroll.START, time = 1) }
                assert(fabVisible = true)
                notesScreen { onScroll(Scroll.START, time = 2) }
                onNavigateTo(pageTo)
                assert(fabVisible = false)
                onNavigateTo(MainPage.NOTES)
            }
        }
    }

    @Test fun onResume() = launch({ data.fillNotes() }) {
        mainScreen {
            notesScreen { onScroll(Scroll.END, time = 1) }
            assert(fabVisible = false)
            notesScreen { openPreference { onClickClose() } }
            assert(fabVisible = true)
        }
    }

    @Test fun standstill() = launch({ data.fillNotes() }) {
        mainScreen {
            notesScreen { onScroll(Scroll.END, time = 1) }
            assert(fabVisible = false)
            waitBefore(NotesFragment.FAB_STANDSTILL_TIME) { assert(fabVisible = true) }

            onScrollTop()
            notesScreen { onScroll(Scroll.END, time = 1) }
            assert(fabVisible = false)
            onNavigateTo(MainPage.BIN)
            waitBefore(NotesFragment.FAB_STANDSTILL_TIME) { assert(fabVisible = false) }
        }
    }

}