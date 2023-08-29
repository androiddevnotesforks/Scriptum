package sgtmelon.scriptum.tests.ui.auto.notifications

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.infrastructure.utils.extensions.note.clearAlarm
import sgtmelon.scriptum.source.cases.list.ListContentCase
import sgtmelon.scriptum.source.cases.list.ListScrollCase
import sgtmelon.scriptum.source.cases.note.NoteOpenCase
import sgtmelon.scriptum.source.provider.DateProvider.DATE_5
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchNotes
import sgtmelon.scriptum.source.ui.tests.launchNotifications
import sgtmelon.scriptum.source.ui.tests.launchNotificationsItem
import sgtmelon.scriptum.source.ui.tests.launchNotificationsList

/**
 * Test for [NotificationsActivity].
 */
@RunWith(AndroidJUnit4::class)
class NotificationsTest : ParentUiRotationTest(),
    ListContentCase,
    ListScrollCase,
    NoteOpenCase {

    @Test override fun contentEmpty() = launchNotifications(isEmpty = true)

    @Test override fun contentList() = launchNotificationsList { assertList(it) }

    @Test override fun contentRotateEmpty() = launchNotifications(isEmpty = true) {
        assert(isEmpty = true)
        rotate.switch()
        assert(isEmpty = true)
    }

    @Test override fun contentRotateList() = launchNotificationsList {
        assert(isEmpty = false)
        rotate.switch()
        assert(isEmpty = false)
        assertList(it)
    }

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

    /** Here is necessary to launch from splash. */
    @Test fun itemCancelFromPast() = db.insertNotification(date = DATE_5).let {
        launchSplash {
            mainScreen {
                openNotes {
                    // TODO bug fail on assertion: notification indicator shown
                    assertItem(it.clearAlarm())
                    openNotifications(isEmpty = true)
                }
            }
        }
    }
}