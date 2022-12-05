package sgtmelon.scriptum.ui.weight

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.parent.ui.tests.ParentUiWeighTest
import sgtmelon.scriptum.parent.ui.tests.launchMain

/**
 * Weigh test for [NotificationsActivity].
 */
@RunWith(AndroidJUnit4::class)
class MainWeighTest : ParentUiWeighTest() {

    @Test fun pageSelect() = launchMain(before = {
        db.fillRank(ITEM_COUNT)
        db.fillNotes(ITEM_COUNT)
        db.fillBin(ITEM_COUNT)
    }) {
        repeat(REPEAT_COUNT) { for (page in MainPage.values()) openPage(page) }
    }
}