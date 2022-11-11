package sgtmelon.scriptum.ui.weight

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
        mainScreen { openNotes { repeat(REPEAT_COUNT) { openNotifications { clickClose() } } } }
    }

    @Test fun screenRotation() = launch({ db.fillNotifications(ITEM_COUNT) }) {
        mainScreen {
            openNotes { openNotifications { repeat(REPEAT_COUNT) { rotate.switch() } } }
        }
    }

    @Test fun listScroll() = launch({ db.fillNotifications(ITEM_COUNT) }) {
        mainScreen { openNotes { openNotifications { scrollTo(Scroll.END, SCROLL_COUNT) } } }
    }
}