package sgtmelon.scriptum.ui.auto.notifications

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.parent.ui.tests.ParentUiRotationTest

/**
 * Test of [NotificationsActivity] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class NotificationsRotationTest : ParentUiRotationTest() {

    @Test fun contentEmpty() = launch {
        mainScreen {
            openNotes(isEmpty = true) {
                openNotifications(isEmpty = true) {
                    assert(isEmpty = true)
                    rotate.toSide()
                    assert(isEmpty = true)
                }
            }
        }
    }

    @Test fun contentList() = startNotificationListTest {
        assert(isEmpty = false)
        rotate.toSide()
        assert(isEmpty = false)
    }

    @Test fun itemCancelAndSnackbar() = startNotificationItemTest(db.insertNote()) {
        repeat(times = 3) { _ ->
            assertItem(it)
            itemCancel()
            assert(isEmpty = true)
            rotate.switch()
            assert(isEmpty = true)
            snackbar { clickCancel() }
            assertItem(it)
        }
    }


    @Test fun itemTextOpen() = startNotificationItemTest(db.insertText()) {
        rotate.switch()
        openText(it)
    }

    @Test fun itemNoteOpen() = startNotificationItemTest(db.insertRoll()) {
        rotate.switch()
        openRoll(it)
    }
}