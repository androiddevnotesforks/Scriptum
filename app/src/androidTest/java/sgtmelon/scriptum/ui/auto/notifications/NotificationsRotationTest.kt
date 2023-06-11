package sgtmelon.scriptum.ui.auto.notifications

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchNotifications
import sgtmelon.scriptum.source.ui.tests.launchNotificationsItem
import sgtmelon.scriptum.source.ui.tests.launchNotificationsList
import sgtmelon.scriptum.ui.cases.list.ListContentCase
import sgtmelon.scriptum.ui.cases.note.NoteOpenCase

/**
 * Test of [NotificationsActivity] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class NotificationsRotationTest : ParentUiRotationTest(),
    ListContentCase,
    NoteOpenCase {

    @Test override fun contentEmpty() = launchNotifications(isEmpty = true) {
        assert(isEmpty = true)
        rotate.toSide()
        assert(isEmpty = true)
    }

    @Test override fun contentList() = launchNotificationsList {
        assert(isEmpty = false)
        rotate.toSide()
        assert(isEmpty = false)
        assertList(it)
    }

    @Test fun itemCancelAndSnackbar() = launchNotificationsItem(db.insertNote()) {
        repeat(times = 3) { _ ->
            assertItem(it)
            itemCancel()
            assert(isEmpty = true)
            rotate.switch()
            assert(isEmpty = true)
            snackbar { action() }
            assertItem(it)
        }
    }


    @Test override fun itemTextOpen() = launchNotificationsItem(db.insertText()) {
        rotate.switch()
        openText(it)
    }

    @Test override fun itemRollOpen() = launchNotificationsItem(db.insertRoll()) {
        rotate.switch()
        openRoll(it)
    }
}