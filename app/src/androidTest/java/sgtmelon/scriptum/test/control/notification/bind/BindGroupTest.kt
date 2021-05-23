package sgtmelon.scriptum.test.control.notification.bind

import org.junit.Test
import sgtmelon.scriptum.test.parent.ParentNotificationTest

/**
 * Test for bind notification groups.
 */
class BindGroupTest : ParentNotificationTest() {


    //TODO fix
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