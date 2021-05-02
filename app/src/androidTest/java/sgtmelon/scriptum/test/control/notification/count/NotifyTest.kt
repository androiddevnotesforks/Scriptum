package sgtmelon.scriptum.test.control.notification.count

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.basic.exception.NoteCastException
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.test.ParentNotificationTest

/**
 * Test of info about notification in status bar
 */
@RunWith(AndroidJUnit4::class)
class NotifyTest : ParentNotificationTest() {

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
        val item = data.insertNotification()
        data.insertNotification()

        launchAlarm(item) { openAlarm(item) { onSee { onClickRepeat() } } }
    }

    /**
     * Update info count on item cancel
     */
    @Test fun notificationNotifyOnCancel() = data.fillNotification(NOTIFICATION_COUNT).let {
        launch {
            mainScreen {
                notesScreen {
                    openNotification { repeat(NOTIFICATION_COUNT) { onSee { onClickCancel() } } }
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


    @Test fun textNoteNotifyOnDate() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
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

    @Test fun textNoteNotifyOnDelete() = with(data) { insertNotification(insertText()) }.let {
        if (it !is NoteItem.Text) throw NoteCastException()

        launch { mainScreen { notesScreen { openTextNote(it) { controlPanel { onDelete() } } } } }
        onSee()
    }

    @Test fun rollNoteNotifyOnDate() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
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

    @Test fun rollNoteNotifyOnDelete() = with(data) { insertNotification(insertRoll()) }.let {
        if (it !is NoteItem.Roll) throw NoteCastException()

        launch { mainScreen { notesScreen { openRollNote(it) { controlPanel { onDelete() } } } } }
        onSee()
    }


    companion object {
        private const val NOTIFICATION_COUNT = 7
    }
}