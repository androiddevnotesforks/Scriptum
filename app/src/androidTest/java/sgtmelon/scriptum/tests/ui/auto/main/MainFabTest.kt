package sgtmelon.scriptum.tests.ui.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerMainFabListener
import sgtmelon.scriptum.source.ui.model.key.Scroll
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchMain
import sgtmelon.test.cappuccino.utils.await

/**
 * Test fab for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class MainFabTest : ParentUiTest() {

    @Test fun pageSelect() = launchMain {
        repeat(times = 3) { for (page in MainPage.values()) clickPage(page) }
    }

    @Test fun scrollAndPageChange() = launchMain({ db.fillNotes(count = 45) }) {
        openNotes {
            scrollTo(Scroll.END, time = 3)
            assert(isFabVisible = false)
            scrollTo(Scroll.START, time = 1)
            assert(isFabVisible = true)
            scrollTo(Scroll.START, time = 1)
        }

        clickPage(listOf(MainPage.RANK, MainPage.BIN).random())
        assert(isFabVisible = false)
        clickPage(MainPage.NOTES)
        assert(isFabVisible = true)
    }

    @Test fun screenResumeState() = launchMain({ db.fillNotes() }) {
        openNotes { scrollTo(Scroll.END, time = 1) }
        assert(isFabVisible = false)
        openNotes { openNotifications(isEmpty = true) { clickClose() } }
        assert(isFabVisible = true)
    }

    @Test fun standstill() = launchMain({ db.fillNotes() }) {
        openNotes { scrollTo(Scroll.END, time = 1) }

        assert(isFabVisible = false)
        await(RecyclerMainFabListener.FAB_STANDSTILL_TIME)
        assert(isFabVisible = true)

        scrollTop()
        openNotes { scrollTo(Scroll.END, time = 1) }

        assert(isFabVisible = false)
        clickPage(MainPage.BIN)
        await(RecyclerMainFabListener.FAB_STANDSTILL_TIME)
        assert(isFabVisible = false)
    }
}