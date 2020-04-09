package sgtmelon.scriptum.test.control.notification.bind

import org.junit.Test
import sgtmelon.scriptum.presentation.receiver.MainReceiver
import sgtmelon.scriptum.test.ParentNotificationTest

/**
 * Test for UI realisation of [MainReceiver.BindCallback].
 */
class UnbindReceiverTest : ParentNotificationTest() {

    /**
     * Notify on start is implied
     */

    @Test fun rankUnbindReceiver() = data.insertRankForNotes().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it.second) { onBind() } }
                rankScreen {
                    onOpen { onAssertItem(it.first.apply { hasBind = false }) }
                }
            }
        }
    }

    @Test fun notesUnbindReceiver() = with(data) {
        insertText(textNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                notesScreen {
                    onOpen { onAssertItem(it.apply { isStatus = false }) }
                }
            }
        }
    }

    @Test fun textNoteUnbindReceiver() = with(data) {
        insertText(textNote.copy(isStatus = true))
    }.let {
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

    @Test fun rollNoteUnbindReceiver() = with(data) {
        insertRoll(rollNote.copy(isStatus = true))
    }.let {
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

    @Test fun alarmUnbindReceiver() = with(data) {
        insertText(textNote.copy(isStatus = true))
    }.let {
        launchAlarm(it) {
            openAlarm(it) { onOpen { onAssertItem(it.apply { isStatus = false }) } }
        }
    }

}