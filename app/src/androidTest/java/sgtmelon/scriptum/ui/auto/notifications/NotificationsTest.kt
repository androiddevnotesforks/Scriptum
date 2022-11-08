package sgtmelon.scriptum.ui.auto.notifications

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_5
import sgtmelon.scriptum.parent.ui.ParentUiTest
import sgtmelon.scriptum.parent.ui.launch
import sgtmelon.scriptum.ui.auto.startNotesTest

/**
 * Test for [NotificationsActivity].
 */
@RunWith(AndroidJUnit4::class)
class NotificationsTest : ParentUiTest() {

    @Test fun close() = launch {
        mainScreen {
            notesScreen(isEmpty = true) {
                openNotifications(isEmpty = true) { pressBack() }
                openNotifications(isEmpty = true) { clickClose() }
            }
        }
    }

    @Test fun contentEmpty() = launch {
        mainScreen { notesScreen(isEmpty = true) { openNotifications(isEmpty = true) } }
    }

    @Test fun contentList() = startNotificationListTest()

    @Test fun listScroll() = startNotificationListTest { onScrollThrough() }

    @Test fun itemTextOpen() = startNotificationItemTest(db.insertText()) { openText(it) }

    @Test fun itemNoteOpen() = startNotificationItemTest(db.insertRoll()) { openRoll(it) }

    @Test fun itemCancel() = startNotificationItemTest(db.insertNote()) {
        itemCancel()
        assert(isEmpty = true)
    }

    // TODO что-то не так в тёмной теме и диалоге установки уведомления в заметке
    @Test fun itemCancelOnNoteDelete() = db.insertNotification().let {
        launch {
            mainScreen {
                notesScreen {
                    openNotifications { pressBack() }
                    openNoteDialog(it) { onDelete() }.openNotifications(isEmpty = true)
                }
            }
        }
    }

    @Test fun itemCancelFromPast() = db.insertNotification(date = DATE_5).let {
        startNotesTest { openNotifications(isEmpty = true) }
    }
}