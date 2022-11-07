package sgtmelon.scriptum.ui.auto.notifications

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_5
import sgtmelon.scriptum.parent.ui.ParentUiTest
import sgtmelon.scriptum.parent.ui.launch

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

    @Test fun contentList() = launch({ db.fillNotification() }) {
        mainScreen { notesScreen { openNotifications() } }
    }

    @Test fun listScroll() = launch({ db.fillNotification() }) {
        mainScreen { notesScreen { openNotifications { onScrollThrough() } } }
    }

    // TODO case: cancel item, snackbar undo click, open item, close note, check snackbar not shown
    // TODO case: cancel item, open item, wait 20 second, close note, check snackbar visibility

    @Test fun textNoteOpen() = db.insertText().let {
        launch({ db.insertNotification(it) }) {
            mainScreen { notesScreen { openNotifications { openText(it) } } }
        }
    }

    @Test fun rollNoteOpen() = db.insertRoll().let {
        launch({ db.insertNotification(it) }) {
            mainScreen { notesScreen { openNotifications { openRoll(it) } } }
        }
    }


    @Test fun itemCancel() = db.insertNotification().let {
        launch {
            mainScreen { notesScreen { openNotifications { itemCancel().assert(isEmpty = true) } } }
        }
    }

    @Test fun itemCancelOnDelete() = db.insertNotification().let {
        launch {
            mainScreen {
                notesScreen {
                    openNoteDialog(it) { onDelete() }.openNotifications(isEmpty = true)
                }
            }
        }
    }

    @Test fun itemCancelFromPast() = db.insertNotification(date = DATE_5).let {
        launch { mainScreen { notesScreen { openNotifications(isEmpty = true) } } }
    }
}