package sgtmelon.scriptum.test.auto.other

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.notification.NotificationActivity
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [NotificationActivity]
 */
@RunWith(AndroidJUnit4::class)
class NotificationTest : ParentUiTest() {

    @Test fun close() = launch {
        mainScreen {
            notesScreen(empty = true) {
                openNotification(empty = true) { onPressBack() }
                openNotification(empty = true) { onClickClose() }
            }
        }
    }

    @Test fun contentEmpty() = launch {
        mainScreen { notesScreen(empty = true) { openNotification(empty = true) } }
    }

    @Test fun contentList() = launch({ data.fillNotification() }) {
        mainScreen { notesScreen { openNotification() } }
    }

    @Test fun listScroll() = launch({ data.fillNotification() }) {
        mainScreen { notesScreen { openNotification { onScrollThrough() } } }
    }


    @Test fun itemCancel() = data.insertNotification().let {
        launch {
            mainScreen { notesScreen { openNotification { onClickCancel().assert(empty = true) } } }
        }
    }

    @Test fun itemCancelOnDelete() = data.insertNotification().let {
        launch {
            mainScreen {
                notesScreen { 
                    openNoteDialog(it) { onDelete() }.openNotification(empty = true)
                }
            }
        }
    }

    @Test fun itemCancelFromPast() = data.insertNotification(date = DATE_0).let {
        launch { mainScreen { notesScreen { openNotification(empty = true) } } }
    }


    @Test fun textNoteOpen() = data.insertText().let {
        launch({ data.insertNotification(it) }) {
            mainScreen { notesScreen { openNotification { openText(it) } } }
        }
    }

    @Test fun rollNoteOpen() = data.insertRoll().let {
        launch({ data.insertNotification(it) }) {
            mainScreen { notesScreen { openNotification { openRoll(it) } } }
        }
    }

}