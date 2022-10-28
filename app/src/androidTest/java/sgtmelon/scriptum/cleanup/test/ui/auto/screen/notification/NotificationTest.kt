package sgtmelon.scriptum.cleanup.test.ui.auto.screen.notification

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationActivity
import sgtmelon.scriptum.parent.ParentUiTest
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_5

/**
 * Test for [NotificationActivity].
 */
@RunWith(AndroidJUnit4::class)
class NotificationTest : ParentUiTest() {

    @Test fun close() = launch {
        mainScreen {
            notesScreen(isEmpty = true) {
                openNotification(isEmpty = true) { onPressBack() }
                openNotification(isEmpty = true) { onClickClose() }
            }
        }
    }

    @Test fun contentEmpty() = launch {
        mainScreen { notesScreen(isEmpty = true) { openNotification(isEmpty = true) } }
    }

    @Test fun contentList() = launch({ db.fillNotification() }) {
        mainScreen { notesScreen { openNotification() } }
    }

    @Test fun listScroll() = launch({ db.fillNotification() }) {
        mainScreen { notesScreen { openNotification { onScrollThrough() } } }
    }

    // TODO case: cancel item, snackbar undo click, open item, close note, check snackbar not shown
    // TODO case: cancel item, open item, wait 20 second, close note, check snackbar visibility

    @Test fun textNoteOpen() = db.insertText().let {
        launch({ db.insertNotification(it) }) {
            mainScreen { notesScreen { openNotification { openText(it) } } }
        }
    }

    @Test fun rollNoteOpen() = db.insertRoll().let {
        launch({ db.insertNotification(it) }) {
            mainScreen { notesScreen { openNotification { openRoll(it) } } }
        }
    }


    @Test fun itemCancel() = db.insertNotification().let {
        launch {
            mainScreen { notesScreen { openNotification { onClickCancel().assert(isEmpty = true) } } }
        }
    }

    @Test fun itemCancelOnDelete() = db.insertNotification().let {
        launch {
            mainScreen {
                notesScreen {
                    openNoteDialog(it) { onDelete() }.openNotification(isEmpty = true)
                }
            }
        }
    }

    @Test fun itemCancelFromPast() = db.insertNotification(date = DATE_5).let {
        launch { mainScreen { notesScreen { openNotification(isEmpty = true) } } }
    }
}