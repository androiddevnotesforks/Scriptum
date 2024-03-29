package sgtmelon.scriptum.cleanup.test.ui.auto.error.notification

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.test.parent.ParentNotificationTest
import sgtmelon.scriptum.cleanup.test.ui.auto.error.Description

import sgtmelon.test.common.nextString

/**
 * Test fix of old errors for bind in status bar.
 */
@RunWith(AndroidJUnit4::class)
class BindErrorTest : ParentNotificationTest() {

    /**
     * [Description.Note.RestoreChanges]
     */
    @Test fun textNoteUnbindOnRestore() = with(db) {
        insertText(textNote.copy(isStatus = true))
    }.let {
        launchSplash {
            mainScreen {
                openNotes {
                    openText(it) {
                        controlPanel { onEdit() }
                        onEnterText(nextString())
//                        automator.unbind(item)
                        toolbar { clickBack() }
                    }
                }
            }
        }
    }

    /**
     * [Description.Note.RestoreChanges]
     */
    @Test fun rollNoteUnbindOnRestore() = with(db) {
        insertRoll(rollNote.copy(isStatus = true))
    }.let {
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
                        controlPanel { onEdit() }
                        enterPanel { onAdd(nextString()) }
//                        automator.unbind(item)
                        toolbar { clickBack() }
                    }
                }
            }
        }
    }
}