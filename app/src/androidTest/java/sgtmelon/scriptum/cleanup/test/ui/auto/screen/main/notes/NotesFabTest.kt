package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.basic.extension.waitBefore
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerMainFabListener
import sgtmelon.scriptum.ui.testing.model.key.Scroll
import sgtmelon.scriptum.ui.testing.parent.ParentUiTest
import sgtmelon.scriptum.ui.testing.parent.launch

/**
 * Test fab for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesFabTest : ParentUiTest() {

    @Test fun onScrollAndPageChange() = launch({ db.fillNotes(count = 45) }) {
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

    @Test fun onResume() = launch({ db.fillNotes() }) {
        mainScreen {
            notesScreen { onScroll(Scroll.END, time = 1) }
            assert(isFabVisible = false)
            notesScreen { openPreferences { clickClose() } }
            assert(isFabVisible = true)
        }
    }

    @Test fun standstill() = launch({ db.fillNotes() }) {
        mainScreen {
            notesScreen { onScroll(Scroll.END, time = 1) }
            assert(isFabVisible = false)
            waitBefore(RecyclerMainFabListener.FAB_STANDSTILL_TIME) {
                assert(isFabVisible = true)
            }

            onScrollTop()
            notesScreen { onScroll(Scroll.END, time = 1) }
            assert(isFabVisible = false)
            onNavigateTo(MainPage.BIN)
            waitBefore(RecyclerMainFabListener.FAB_STANDSTILL_TIME) {
                assert(isFabVisible = false)
            }
        }
    }

}