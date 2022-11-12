package sgtmelon.scriptum.cleanup.test.ui.control.notification.count

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.basic.exception.NoteCastException
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.test.parent.ParentNotificationTest

/**
 * Test of info about notification in status bar
 */
@RunWith(AndroidJUnit4::class)
class NotifyTest : ParentNotificationTest() {

    // TODO fix all

    /**
     * Notify on start is implied
     */

    /**
     * Should hide info notification on alarm start
     */
    @Test fun alarmNotifyOnStart() {
        TODO()
    }

    /**
     * Notification in status bar should show - "You have 1 notification".
     */
    @Test fun alarmNotify() {
        TODO()

        val item = db.insertNotification()
        db.insertNotification()

        launchAlarm(item) { alarmScreen(item) { onSee { onClickRepeat() } } }
    }

    /**
     * Update info count on item cancel
     */
    @Test fun notificationNotifyOnCancel() = db.fillNotifications(NOTIFICATION_COUNT).let {
        TODO()
        launch {
            mainScreen {
                openNotes {
                    openNotifications { repeat(NOTIFICATION_COUNT) { onSee { itemCancel() } } }
                }
            }
        }
    }

    /**
     * Update info count after snackbar undo
     */
    @Test fun notificationNotifyOnUndo() {
        TODO()
    }


    @Test fun notesNotifyOnDate() = db.insertNote().let {
        TODO()
        launch {
            mainScreen {
                openNotes {
                    openNoteDialog(it) {
                        notification { onClickApply { onTime(min = 3).onClickApply() } }
                    }
                    onSee()
                    openNoteDialog(it) { notification { onClickReset() } }
                    onSee()
                }
            }
        }
    }

    @Test fun notesNotifyOnDelete() = db.insertNotification().let {
        TODO()
        launch { mainScreen { openNotes { openNoteDialog(it) { delete() } } } }
        onSee()
    }


    @Test fun textNoteNotifyOnDate() = db.insertText().let {
        TODO()
        launch {
            mainScreen {
                openNotes {
                    openText(it) {
                        controlPanel {
                            onNotification { onClickApply { onTime(min = 3).onClickApply() } }
                            onSee()
                            onNotification(isUpdateDate = true) { onClickReset() }
                            onSee()
                        }
                    }
                }
            }
        }
    }

    @Test fun textNoteNotifyOnDelete() = with(db) { insertNotification(insertText()) }.let {
        if (it !is NoteItem.Text) throw NoteCastException()

        TODO()
        launch { mainScreen { openNotes { openText(it) { controlPanel { onDelete() } } } } }
        onSee()
    }

    @Test fun rollNoteNotifyOnDate() = db.insertRoll().let {
        TODO()
        launch {
            mainScreen {
                openNotes {
                    openRoll(it) {
                        controlPanel {
                            onNotification { onClickApply { onTime(min = 3).onClickApply() } }
                            onSee()
                            onNotification(isUpdateDate = true) { onClickReset() }
                            onSee()
                        }
                    }
                }
            }
        }
    }

    @Test fun rollNoteNotifyOnDelete() = with(db) { insertNotification(insertRoll()) }.let {
        if (it !is NoteItem.Roll) throw NoteCastException()

        TODO()
        launch { mainScreen { openNotes { openRoll(it) { controlPanel { onDelete() } } } } }
        onSee()
    }


    companion object {
        private const val NOTIFICATION_COUNT = 7
    }
}