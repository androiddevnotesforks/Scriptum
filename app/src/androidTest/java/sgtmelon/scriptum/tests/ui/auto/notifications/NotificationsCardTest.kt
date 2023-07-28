package sgtmelon.scriptum.tests.ui.auto.notifications

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extensions.getClearCalendar
import sgtmelon.extensions.toText
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.source.ui.screen.item.NotificationItemUi
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchNotifications
import sgtmelon.scriptum.source.utils.NEXT_HOUR
import sgtmelon.scriptum.source.utils.nextArray
import sgtmelon.test.common.halfChance

/**
 * Test for [NotificationItemUi]
 */
@RunWith(AndroidJUnit4::class)
class NotificationsCardTest : ParentUiTest() {

    @Test fun timeFormatting() = startListTest(List(nextArray.size) {
        db.insertNotification(date = getClearCalendar(nextArray[it]).toText())
    })

    @Test fun colorIndicatorLight() = startColorTest(ThemeDisplayed.LIGHT)

    @Test fun colorIndicatorDark() = startColorTest(ThemeDisplayed.DARK)

    private fun startColorTest(theme: ThemeDisplayed) {
        setupTheme(theme)

        startListTest(List(Color.values().size) {
            val date = getClearCalendar(addMinutes = NEXT_HOUR + it * NEXT_HOUR).toText()
            val item = if (halfChance()) {
                db.insertText(db.textNote.copy(color = Color.values()[it]))
            } else {
                db.insertRoll(db.rollNote.copy(color = Color.values()[it]))
            }

            return@List db.insertNotification(item, date)
        })
    }

    private fun startListTest(list: List<NoteItem>) = launchNotifications {
        assertList(list, withWait = true)
    }
}