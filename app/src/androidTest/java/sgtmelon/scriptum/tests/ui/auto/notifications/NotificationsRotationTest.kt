package sgtmelon.scriptum.tests.ui.auto.notifications

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.source.cases.note.NoteOpenCase
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchNotificationsItem

/**
 * Test of [NotificationsActivity] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class NotificationsRotationTest : ParentUiRotationTest(),
    NoteOpenCase {

    @Test override fun itemTextOpen() = launchNotificationsItem(db.insertText()) {
        rotate.switch()
        openText(it)
    }

    @Test override fun itemRollOpen() = launchNotificationsItem(db.insertRoll()) {
        rotate.switch()
        openRoll(it)
    }
}