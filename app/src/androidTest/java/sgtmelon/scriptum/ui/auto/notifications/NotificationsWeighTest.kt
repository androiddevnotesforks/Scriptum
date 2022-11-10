package sgtmelon.scriptum.ui.auto.notifications

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.tests.ParentUiWeighTest

/**
 * Weigh test for [NotificationsActivity].
 */
@RunWith(AndroidJUnit4::class)
class NotificationsWeighTest : ParentUiWeighTest() {

    @Test fun screenOpen() = launch({ db.fillNotifications(ITEM_COUNT) }) {
        mainScreen { notesScreen { repeat(REPEAT_COUNT) { openNotifications { clickClose() } } } }
    }

    @Test fun screenRotation() = launch({ db.fillNotifications(ITEM_COUNT) }) {
        mainScreen {
            notesScreen { openNotifications { repeat(REPEAT_COUNT) { rotate.switch() } } }
        }
    }

    @Test fun listScroll() = launch({ db.fillNotifications(ITEM_COUNT) }) {
        mainScreen { notesScreen { openNotifications { onScroll(Scroll.END, SCROLL_COUNT) } } }
    }
}