package sgtmelon.scriptum.cleanup.test.ui.control.notification.bind

import org.junit.Test
import sgtmelon.scriptum.cleanup.test.parent.ParentNotificationTest


/**
 * Test for bind notification groups.
 */
class BindGroupTest : ParentNotificationTest() {


    //TODO fix
    @Test fun notesGroup() = db.fillNotes(count = 3).let {
        TODO()

        launchSplash {
            mainScreen {
                openNotes {
                    for ((p, item) in it.withIndex()) {
                        openNoteDialog(item, p) { bind() }.apply { onSee() }
                    }

                    for ((p, item) in it.withIndex()) {
                        openNoteDialog(item, p) { bind() }.apply { onSee() }
                    }
                }
            }
        }
    }
}