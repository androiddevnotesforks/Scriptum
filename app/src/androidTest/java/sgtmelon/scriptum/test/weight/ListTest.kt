package sgtmelon.scriptum.test.weight

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.data.WeightData
import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.domain.model.key.MainPage
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test recyclerView lists for lags
 */
@RunWith(AndroidJUnit4::class)
class ListTest : ParentUiTest() {

    // TODO #TEST optimization textNote inside lists (because now I load all text length)

    private val pageList = arrayListOf(MainPage.RANK, MainPage.NOTES, MainPage.BIN)

    private val weightData = WeightData(context, RoomProvider(context))


    @Test fun mainPageSelect() = launch(before = {
        data.fillRank(RANK_COUNT)
        data.fillNotes(NOTES_COUNT)
        data.fillBin(BIN_COUNT)
    }) {
        mainScreen { repeat(REPEAT_COUNT) { for (page in pageList) openPage(page) } }
    }

    @Test fun rankScroll() = launch({ data.fillRank(RANK_COUNT) }) {
        mainScreen { rankScreen { onScroll(Scroll.END, SCROLL_COUNT) } }
    }

    @Test fun notesScroll() = launch({ data.fillNotes(NOTES_COUNT) }) {
        mainScreen { notesScreen { onScroll(Scroll.END, SCROLL_COUNT) } }
    }

    @Test fun binScroll() = launch({ data.fillBin(BIN_COUNT) }) {
        mainScreen { binScreen { onScroll(Scroll.END, SCROLL_COUNT) } }
    }


    @Test fun notificationOpen() = launch({ data.fillNotification(NOTIFICATION_COUNT) }) {
        mainScreen {
            notesScreen { repeat(REPEAT_COUNT) { openNotification { onClickClose() } } }
        }
    }

    @Test fun notificationScroll() = launch({ data.fillNotification(NOTIFICATION_COUNT) }) {
        mainScreen { notesScreen { openNotification { onScroll(Scroll.END, SCROLL_COUNT) } } }
    }


    /**
     * Heavy = 30.223s
     * Simple = 19.780s
     * Coroutine = 21.930s
     */
    @Test fun textNoteOpen() = data.insertText(weightData.textNote).let { model ->
        launch {
            mainScreen {
                notesScreen {
                    repeat(REPEAT_COUNT) { openTextNote(model) { toolbar { onClickBack() } } }
                }
            }
        }
    }

    @Test fun rollNoteOpen() = data.insertRoll(
        isVisible = true,
        list = weightData.rollList
    ).let { model ->
        launch {
            mainScreen {
                notesScreen {
                    repeat(REPEAT_COUNT) { openRollNote(model) { toolbar { onClickBack() } } }
                }
            }
        }
    }

    @Test fun rollNoteScroll() = data.insertRoll(
        isVisible = true,
        list = weightData.rollList
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

        Log.i(TAG, "Time millis = ${endTime - startTime}")
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