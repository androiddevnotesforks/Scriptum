package sgtmelon.scriptum.test.error.notification

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.nextString
import sgtmelon.scriptum.test.ParentNotificationTest
import sgtmelon.scriptum.test.error.Description
import kotlin.random.Random

/**
 * Test fix of old errors for bind in status bar.
 */
@RunWith(AndroidJUnit4::class)
class BindErrorTest : ParentNotificationTest() {

    /**
     * [Description.Note.RestoreChanges]
     */
    @Test fun textNoteUnbindOnRestore() = with(data) {
        insertText(textNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onEdit() }
                        onEnterText(Random.nextString())
                        onOpen { noteItem.isStatus = false }
                        toolbar { onClickBack() }
                    }
                }
            }
        }
    }

    /**
     * [Description.Note.RestoreChanges]
     */
    @Test fun rollNoteUnbindOnRestore() = with(data) {
        insertRoll(rollNote.copy(isStatus = true))
    }.let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onEdit() }
                        enterPanel { onAdd(Random.nextString()) }
                        onOpen { noteItem.isStatus = false }
                        toolbar { onClickBack() }
                    }
                }
            }
        }
    }


}