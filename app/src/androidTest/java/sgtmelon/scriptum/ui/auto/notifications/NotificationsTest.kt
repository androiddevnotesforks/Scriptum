package sgtmelon.scriptum.ui.auto.notifications

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_5
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.parent.ui.tests.launchNotes
import sgtmelon.scriptum.parent.ui.tests.launchNotifications
import sgtmelon.scriptum.parent.ui.tests.launchNotificationsItem
import sgtmelon.scriptum.parent.ui.tests.launchNotificationsList
import sgtmelon.scriptum.ui.cases.NoteOpenCase
import sgtmelon.scriptum.ui.cases.list.ListContentCase
import sgtmelon.scriptum.ui.cases.list.ListScrollCase

/**
 * Test for [NotificationsActivity].
 */
@RunWith(AndroidJUnit4::class)
class NotificationsTest : ParentUiTest(),
    ListContentCase,
    ListScrollCase,
    NoteOpenCase {

    @Test override fun contentEmpty() = launchNotifications(isEmpty = true)

    @Test override fun contentList() = launchNotificationsList { assertList(it) }

    @Test override fun listScroll() = launchNotificationsList { scrollThrough() }

    @Test fun close() = launchNotes(isEmpty = true) {
        openNotifications(isEmpty = true) { pressBack() }
        assert(isEmpty = true)
        openNotifications(isEmpty = true) { clickClose() }
        assert(isEmpty = true)
    }

    @Test override fun itemTextOpen() = launchNotificationsItem(db.insertText()) { openText(it) }

    @Test override fun itemRollOpen() = launchNotificationsItem(db.insertRoll()) { openRoll(it) }

    @Test fun itemCancel() = launchNotificationsItem(db.insertNote()) {
        itemCancel()
        assert(isEmpty = true)
    }

    @Test fun itemCancelOnNoteDelete() = db.insertNotification().let {
        launchNotes {
            openNotifications { pressBack() }
            openNoteDialog(it) { delete() }
            openNotifications(isEmpty = true)
        }
    }

    @Test fun itemCancelFromPast() = db.insertNotification(date = DATE_5).let {
        launchNotes { openNotifications(isEmpty = true) }
    }
}