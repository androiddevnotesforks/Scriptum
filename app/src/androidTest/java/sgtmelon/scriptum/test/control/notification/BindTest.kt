package sgtmelon.scriptum.test.control.notification

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.test.ParentNotificationTest

/**
 * Test of note bind in status bar
 */
@RunWith(AndroidJUnit4::class)
class BindTest : ParentNotificationTest() {

    /**
     * TODO Check where use BindControl and test this pieces
     */

    @Test fun notifyOnStart() = data.insertText(data.textNote.copy(isStatus = true)).let {
        launch { mainScreen { notesScreen { onSee { onAssertItem(it) } } } }
    }


    @Test fun notesUnbindReceiver() = data.insertText(data.textNote.copy(isStatus = true)).let {
        launch {
            mainScreen {
                notesScreen { onOpen { onAssertItem(it.apply { noteEntity.isStatus = false }) } }
            }
        }
    }

    @Test fun textNoteUnbindReceiver() = data.insertText(data.textNote.copy(isStatus = true)).let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        onOpen {
                            it.noteEntity.isStatus = false
                            fullAssert()
                        }
                    }
                }
            }
        }
    }

    @Test fun rollNoteUnbindReceiver() = data.insertRoll(data.rollNote.copy(isStatus = true)).let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        onOpen {
                            it.noteEntity.isStatus = false
                            fullAssert()
                        }
                    }
                }
            }
        }
    }

}