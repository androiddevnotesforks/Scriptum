package sgtmelon.scriptum.test.auto.dialog

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.test.ParentUiTest

@RunWith(AndroidJUnit4::class)
class DateTimeDialogTest : ParentUiTest() {

    @Test fun textNoteReset() = data.insertNotification(data.insertText()).let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onNotification(updateDate = true) { onClickReset() } }
                    }
                }
            }
        }
    }

    @Test fun rollNoteReset() = data.insertNotification(data.insertRoll()).let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onNotification(updateDate = true) { onClickReset() } }
                    }
                }
            }
        }
    }

}