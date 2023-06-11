package sgtmelon.scriptum.cleanup.test.ui.auto.error.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.test.ui.auto.error.Description
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteFragmentImpl
import sgtmelon.scriptum.source.ui.tests.ParentUiTest

/**
 * Test fix of old errors for [TextNoteFragmentImpl].
 */
@RunWith(AndroidJUnit4::class)
class TextNoteErrorTest : ParentUiTest() {

    /**
     * [Description.Note.RestoreChanges]
     */
    @Test fun restoreChanges() = db.insertText().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openText(it) {
                        controlPanel { onBind().onEdit() }
                        toolbar { clickBack() }

                        controlPanel {
                            onNotification { applyDate { set(addMin = 3).applyTime() } }
                            onEdit()
                        }
                        toolbar { clickBack() }
                    }
                }
            }
        }
    }
}