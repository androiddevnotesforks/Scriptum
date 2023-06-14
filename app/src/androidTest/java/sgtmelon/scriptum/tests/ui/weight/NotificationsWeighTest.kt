package sgtmelon.scriptum.tests.ui.weight

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.source.ui.model.key.Scroll
import sgtmelon.scriptum.source.ui.tests.ParentUiWeighTest
import sgtmelon.scriptum.source.ui.tests.launchMain

/**
 * Weigh test for [NotificationsActivity].
 */
@RunWith(AndroidJUnit4::class)
class NotificationsWeighTest : ParentUiWeighTest() {

    @Test fun screenOpen() = launchMain({ db.fillNotifications(ITEM_COUNT) }) {
        openNotes { repeat(REPEAT_COUNT) { openNotifications { clickClose() } } }
    }

    @Test fun screenRotation() = launchMain({ db.fillNotifications(ITEM_COUNT) }) {
        openNotes { openNotifications { repeat(REPEAT_COUNT) { rotate.switch() } } }
    }

    @Test fun listScroll() = launchMain({ db.fillNotifications(ITEM_COUNT) }) {
        openNotes { openNotifications { scrollTo(Scroll.END, SCROLL_COUNT) } }
    }
}