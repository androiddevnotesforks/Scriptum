package sgtmelon.scriptum.test.auto

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.view.notification.NotificationActivity
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Тест для [NotificationActivity]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class NotificationTest : ParentUiTest() {

    override fun setUp() {
        super.setUp()

        iPreferenceRepo.firstStart = false
        testData.clear()
    }

    @Test fun contentEmpty() =
            launch { mainScreen { openNotesPage { openNotification(empty = true) } } }

    @Test fun contentList() = launch({ testData.fillNotification() }) {
        mainScreen { openNotesPage { openNotification(empty = false) } }
    }

    @Test fun listScroll() = launch({ testData.fillNotification(times = 20) }) {
        mainScreen { openNotesPage { openNotification(empty = false) { onScrollThrough() } } }
    }

    @Test fun textNoteOpen() = launch({ testData.insertNotification(testData.insertText()) }) {
        mainScreen { openNotesPage { openNotification(empty = false) { openText() } } }
    }

    @Test fun rollNoteOpen() = launch({ testData.insertNotification(testData.insertRoll()) }) {
        mainScreen { openNotesPage { openNotification(empty = false) { openRoll() } } }
    }

}