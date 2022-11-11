package sgtmelon.scriptum.ui.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.basic.extension.waitBefore
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerMainFabListener

import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test fab for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class MainFabTest : ParentUiTest() {

    @Test fun onPageSelect() = launch {
        mainScreen {
            repeat(times = 3) { for (page in MainPage.values()) clickPage(page) }
        }
    }

    @Test fun onScrollAndPageChange() = launch({ db.fillNotes(count = 45) }) {
        mainScreen {
            for (it in listOf(MainPage.RANK, MainPage.BIN)) {
                openNotes { scrollTo(Scroll.END, time = 5) }
                assert(isFabVisible = false)
                openNotes { scrollTo(Scroll.START, time = 1) }
                assert(isFabVisible = true)
                openNotes { scrollTo(Scroll.START, time = 2) }
                clickPage(it)
                assert(isFabVisible = false)
                clickPage(MainPage.NOTES)
            }
        }
    }

    @Test fun onResume() = launch({ db.fillNotes() }) {
        mainScreen {
            openNotes { scrollTo(Scroll.END, time = 1) }
            assert(isFabVisible = false)
            openNotes { openPreferences { clickClose() } }
            assert(isFabVisible = true)
        }
    }

    @Test fun standstill() = launch({ db.fillNotes() }) {
        mainScreen {
            openNotes { scrollTo(Scroll.END, time = 1) }
            assert(isFabVisible = false)
            waitBefore(RecyclerMainFabListener.FAB_STANDSTILL_TIME) {
                assert(isFabVisible = true)
            }

            scrollTop()
            openNotes { scrollTo(Scroll.END, time = 1) }
            assert(isFabVisible = false)
            clickPage(MainPage.BIN)
            waitBefore(RecyclerMainFabListener.FAB_STANDSTILL_TIME) {
                assert(isFabVisible = false)
            }
        }
    }

}