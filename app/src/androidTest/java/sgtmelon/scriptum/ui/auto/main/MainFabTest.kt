package sgtmelon.scriptum.ui.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerMainFabListener
import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.test.cappuccino.utils.await

/**
 * Test fab for [NotesFragment].
 */
@RunWith(AndroidJUnit4::class)
class MainFabTest : ParentUiTest() {

    @Test fun pageSelect() = launchSplash {
        mainScreen {
            repeat(times = 3) { for (page in MainPage.values()) clickPage(page) }
        }
    }

    @Test fun scrollAndPageChange() = launchSplash({ db.fillNotes(count = 45) }) {
        mainScreen {
            for (it in listOf(MainPage.RANK, MainPage.BIN)) {
                openNotes { scrollTo(Scroll.END, time = 3) }
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

    @Test fun screenResumeState() = launchSplash({ db.fillNotes() }) {
        mainScreen {
            openNotes { scrollTo(Scroll.END, time = 1) }
            assert(isFabVisible = false)
            openNotes { openNotifications(isEmpty = true) { clickClose() } }
            assert(isFabVisible = true)
        }
    }

    @Test fun standstill() = launchSplash({ db.fillNotes() }) {
        mainScreen {
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
}