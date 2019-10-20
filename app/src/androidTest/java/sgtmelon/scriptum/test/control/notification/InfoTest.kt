package sgtmelon.scriptum.test.control.notification

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.test.ParentNotificationTest

/**
 * Test of info about notification in status bar
 */
@RunWith(AndroidJUnit4::class)
class InfoTest : ParentNotificationTest() {

    /**
     * Notify on start is implied
     */

    /**
     * Will cancel notification
     */
    @Test fun displayInfoZero() = startDisplayTest(count = 0)

    @Test fun displayInfoOne() = startDisplayTest(count = 1)

    @Test fun displayInfoTwo() = startDisplayTest(count = 2)

    @Test fun displayInfoFew() = startDisplayTest(count = 4)

    @Test fun displayInfoMany() = startDisplayTest(count = 7)

    private fun startDisplayTest(count: Int) {
        repeat(count) { data.insertNotification() }

        launch { onSee { mainScreen() } }
    }


    @Test fun notificationNotifyOnCancel() = data.fillNotification(NOTIFICATION_COUNT).let {
        launch {
            mainScreen {
                notesScreen {
                    openNotification { repeat(NOTIFICATION_COUNT) { onSee { onClickCancel() } } }
                }
            }
        }
    }


    @Test fun notesNotifyOnDate() = data.insertNote().let {
        launch {
            mainScreen {
                notesScreen {
                    openNoteDialog(it) {
                        onNotification { onClickApply { onTime(min = 3).onClickApply() } }
                    }
                    onSee()
                    openNoteDialog(it) { onNotification { onClickReset() } }
                    onSee()
                }
            }
        }
    }

    @Test fun notesNotifyOnDelete() = data.insertNotification().let {
        launch { mainScreen { notesScreen { openNoteDialog(it) { onDelete() } } } }
        onSee()
    }

    private companion object {
        const val NOTIFICATION_COUNT = 7
    }

}