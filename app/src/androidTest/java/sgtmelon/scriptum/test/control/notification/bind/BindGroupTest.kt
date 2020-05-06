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
                    it.forEachIndexed { p, item ->
                        openNoteDialog(item, p) { onBind() }.apply { onSee() }
                    }

                    it.forEachIndexed { p, item ->
                        openNoteDialog(item, p) { onBind() }.apply { onSee() }
                    }
                }
            }
        }
    }

}