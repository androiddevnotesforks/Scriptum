package sgtmelon.scriptum.test.control.notification.bind

import org.junit.Test
import sgtmelon.scriptum.test.ParentNotificationTest

/**
 * Test for bind notification groups.
 */
class BindGroupTest : ParentNotificationTest() {

    @Test fun notesGroup() = data.fillNotes(count = 3).let {
        launch {
            mainScreen {
                notesScreen {
                    for ((p, item) in it.withIndex()) {
                        openNoteDialog(item, p) { onBind() }.apply { onSee() }
                    }

                    for ((p, item) in it.withIndex()) {
                        openNoteDialog(item, p) { onBind() }.apply { onSee() }
                    }
                }
            }
        }
    }

}