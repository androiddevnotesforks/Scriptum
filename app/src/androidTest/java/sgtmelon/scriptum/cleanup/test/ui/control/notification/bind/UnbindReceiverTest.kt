package sgtmelon.scriptum.cleanup.test.ui.control.notification.bind

import org.junit.Test
import sgtmelon.scriptum.cleanup.test.parent.ParentNotificationTest


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
                openNotes { openNoteDialog(it.second) { onBind() } }
                openRank {
                    onOpen { assertItem(it.first.apply { bindCount = 0 }) }
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
                openNotes {
                    onOpen { assertItem(it.apply { isStatus = false }) }
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
                openNotes {
                    openText(it) {
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
                openNotes {
                    openRoll(it) {
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
            alarmScreen(it) { onOpen { onAssertItem(it.apply { isStatus = false }) } }
        }
    }
}