package sgtmelon.scriptum.ui.auto.notifications

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_5
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.auto.startNotesTest
import sgtmelon.scriptum.ui.cases.ListContentCase
import sgtmelon.scriptum.ui.cases.ListScrollCase
import sgtmelon.scriptum.ui.cases.NoteOpenCase

/**
 * Test for [NotificationsActivity].
 */
@RunWith(AndroidJUnit4::class)
class NotificationsTest : ParentUiTest(),
    ListContentCase,
    ListScrollCase,
    NoteOpenCase {

    @Test override fun contentEmpty() = launch {
        mainScreen { openNotes(isEmpty = true) { openNotifications(isEmpty = true) } }
    }

    @Test override fun contentList() = startNotificationListTest { assertList(it) }

    @Test override fun listScroll() = startNotificationListTest { scrollThrough() }

    @Test fun close() = launch {
        mainScreen {
            openNotes(isEmpty = true) {
                openNotifications(isEmpty = true) { pressBack() }
                openNotifications(isEmpty = true) { clickClose() }
            }
        }
    }

    @Test override fun itemTextOpen() = startNotificationItemTest(db.insertText()) { openText(it) }

    @Test override fun itemRollOpen() = startNotificationItemTest(db.insertRoll()) { openRoll(it) }

    @Test fun itemCancel() = startNotificationItemTest(db.insertNote()) {
        itemCancel()
        assert(isEmpty = true)
    }

    @Test fun itemCancelOnNoteDelete() = db.insertNotification().let {
        launch {
            mainScreen {
                openNotes {
                    openNotifications { pressBack() }
                    openNoteDialog(it) { delete() }
                    openNotifications(isEmpty = true)
                }
            }
        }
    }

    @Test fun itemCancelFromPast() = db.insertNotification(date = DATE_5).let {
        startNotesTest { openNotifications(isEmpty = true) }
    }
}