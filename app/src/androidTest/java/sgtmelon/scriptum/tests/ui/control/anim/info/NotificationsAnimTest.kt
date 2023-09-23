package sgtmelon.scriptum.tests.ui.control.anim.info

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchNotificationsItem

/**
 * Test of animation info about empty list for [NotificationsActivity].
 */
@RunWith(AndroidJUnit4::class)
class NotificationsAnimTest : ParentUiTest() {

    @Test fun onScreen_cancel_restore() = launchNotificationsItem(db.insertNotification()) {
        repeat(SWITCH_REPEAT) {
            itemCancel()
            assert(isEmpty = true)
            snackbar { action() }
            assert(isEmpty = false)
        }
    }

    @Test fun onSide_note_notificationReset() = launchNotificationsItem(db.insertNotification()) {
        when (it) {
            is NoteItem.Text -> openText(it) {
                controlPanel { onNotification(isUpdateDate = true) { reset() } }
                clickClose()
            }
            is NoteItem.Roll -> openRoll(it) {
                controlPanel { onNotification(isUpdateDate = true) { reset() } }
                clickClose()
            }
        }
        assert(isEmpty = true)
    }

    @Test fun onSide_note_delete() = launchNotificationsItem(db.insertNotification()) {
        when (it) {
            is NoteItem.Text -> openText(it) { controlPanel { onDelete() } }
            is NoteItem.Roll -> openRoll(it) { controlPanel { onDelete() } }
        }
        assert(isEmpty = true)
    }

    companion object {
        private const val SWITCH_REPEAT = 3
    }
}