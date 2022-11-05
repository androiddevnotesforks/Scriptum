package sgtmelon.scriptum.cleanup.test.ui.weight

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.testData.Scroll
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.ui.testing.parent.ParentUiWeighTest
import timber.log.Timber

/**
 * Test recyclerView lists for lags
 */
@RunWith(AndroidJUnit4::class)
class ListTest : ParentUiWeighTest() {

    // TODO #TEST optimization textNote inside lists (because now I load all text length)

    private val pageList = arrayListOf(MainPage.RANK, MainPage.NOTES, MainPage.BIN)

    @Test fun mainPageSelect() = launch(before = {
        db.fillRank(RANK_COUNT)
        db.fillNotes(NOTES_COUNT)
        db.fillBin(BIN_COUNT)
    }) {
        mainScreen { repeat(REPEAT_COUNT) { for (page in pageList) openPage(page) } }
    }

    @Test fun rankScroll() = launch({ db.fillRank(RANK_COUNT) }) {
        mainScreen { rankScreen { onScroll(Scroll.END, SCROLL_COUNT) } }
    }

    @Test fun notesScroll() = launch({ db.fillNotes(NOTES_COUNT) }) {
        mainScreen { notesScreen { onScroll(Scroll.END, SCROLL_COUNT) } }
    }

    @Test fun binScroll() = launch({ db.fillBin(BIN_COUNT) }) {
        mainScreen { binScreen { onScroll(Scroll.END, SCROLL_COUNT) } }
    }


    @Test fun notificationOpen() = launch({ db.fillNotification(NOTIFICATION_COUNT) }) {
        mainScreen {
            notesScreen { repeat(REPEAT_COUNT) { openNotifications { onClickClose() } } }
        }
    }

    @Test fun notificationScroll() = launch({ db.fillNotification(NOTIFICATION_COUNT) }) {
        mainScreen { notesScreen { openNotifications { onScroll(Scroll.END, SCROLL_COUNT) } } }
    }


    /**
     * Heavy = 30.223s
     * Simple = 19.780s
     * Coroutine = 21.930s
     */
    @Test fun textNoteOpen() = db.insertText(dbWeight.textNote).let { model ->
        launch {
            mainScreen {
                notesScreen {
                    repeat(REPEAT_COUNT) { openTextNote(model) { toolbar { onClickBack() } } }
                }
            }
        }
    }

    @Test fun rollNoteOpen() = db.insertRoll(
        isVisible = true,
        list = dbWeight.rollList
    ).let { model ->
        launch {
            mainScreen {
                notesScreen {
                    repeat(REPEAT_COUNT) { openRollNote(model) { toolbar { onClickBack() } } }
                }
            }
        }
    }

    @Test fun rollNoteScroll() = db.insertRoll(
        isVisible = true,
        list = dbWeight.rollList
    ).let {
        launch {
            mainScreen { notesScreen { openRollNote(it) { onScroll(Scroll.END, SCROLL_COUNT) } } }
        }
    }


    private var startTime = 0L
    private var endTime = 0L

    private fun onTime(func: () -> Unit) {
        startTime = System.currentTimeMillis()
        func()
        endTime = System.currentTimeMillis()

        Timber.i(message = "Time millis = ${endTime - startTime}")
    }

    companion object {
        val TAG: String? = ListTest::class.java.canonicalName

        private const val RANK_COUNT = 200
        private const val NOTES_COUNT = 250
        private const val BIN_COUNT = 250
        private const val NOTIFICATION_COUNT = 200

        private const val REPEAT_COUNT = 10
        private const val SCROLL_COUNT = 15
    }
}