package sgtmelon.scriptum.cleanup.test.ui.control.notification.bind

import org.junit.Test
import sgtmelon.scriptum.cleanup.test.parent.ParentNotificationTest
import sgtmelon.scriptum.infrastructure.receiver.screen.UpdateAlarmReceiver

/**
 * Test for UI realisation of [UpdateAlarmReceiver.BindCallback].
 */
class UnbindReceiverTest : ParentNotificationTest() {

    // TODO fix all

    /**
     * Notify on start is implied
     */

    @Test fun rankUnbindReceiver() = db.insertRankForNotes().let {
        TODO()
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it.second) { onBind() } }
                rankScreen {
                    onOpen { onAssertItem(it.first.apply { bindCount = 0 }) }
                }
            }
        }
    }

    @Test fun notesUnbindReceiver() = with(db) {
        insertText(textNote.copy(isStatus = true))
    }.let {
        TODO()
        launch {
            mainScreen {
                notesScreen {
                    onOpen { onAssertItem(it.apply { isStatus = false }) }
                }
            }
        }
    }

    @Test fun textNoteUnbindReceiver() = with(db) {
        insertText(textNote.copy(isStatus = true))
    }.let {
        TODO()
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        onOpen { apply { it.isStatus = false }.fullAssert() }
                    }
                }
            }
        }
    }

    @Test fun rollNoteUnbindReceiver() = with(db) {
        insertRoll(rollNote.copy(isStatus = true))
    }.let {
        TODO()
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        onOpen { apply { it.isStatus = false }.fullAssert() }
                    }
                }
            }
        }
    }

    @Test fun alarmUnbindReceiver() = with(db) {
        insertText(textNote.copy(isStatus = true))
    }.let {
        TODO()
        launchAlarm(it) {
            openAlarm(it) { onOpen { onAssertItem(it.apply { isStatus = false }) } }
        }
    }
}