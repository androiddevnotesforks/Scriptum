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

    @Test fun contentEmpty() =
            launch { mainScreen { openNotesPage(empty = true) { openNotification(empty = true) } } }

    @Test fun contentList() = launch({ testData.fillNotification() }) {
        mainScreen { openNotesPage { openNotification() } }
    }

    @Test fun listScroll() = launch({ testData.fillNotification() }) {
        mainScreen { openNotesPage { openNotification { onScrollThrough() } } }
    }

    @Test fun textNoteOpen() = testData.insertText().let {
        launch({ testData.insertNotification(it) }) {
            mainScreen { openNotesPage { openNotification { openText(it) } } }
        }
    }

    @Test fun rollNoteOpen() = testData.insertRoll().let {
        launch({ testData.insertNotification(it) }) {
            mainScreen { openNotesPage { openNotification { openRoll(it) } } }
        }
    }

}