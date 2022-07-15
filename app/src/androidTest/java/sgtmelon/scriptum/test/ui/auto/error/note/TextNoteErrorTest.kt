package sgtmelon.scriptum.test.ui.auto.error.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.test.ui.auto.error.Description
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test fix of old errors for [TextNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class TextNoteErrorTest : ParentUiTest() {

    /**
     * [Description.Note.RestoreChanges]
     */
    @Test fun restoreChanges() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onBind().onEdit() }
                        toolbar { onClickBack() }

                        controlPanel {
                            onNotification { onClickApply { onTime(min = 3).onClickApply() } }
                            onEdit()
                        }
                        toolbar { onClickBack() }
                    }
                }
            }
        }
    }
}