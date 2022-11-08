package sgtmelon.scriptum.ui.auto.notifications

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extensions.getClearCalendar
import sgtmelon.extensions.toText
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.parent.ui.ParentUiTest
import sgtmelon.scriptum.parent.ui.screen.item.NotificationItemUi
import sgtmelon.scriptum.ui.auto.startNotificationsTest

/**
 * Test for [NotificationItemUi]
 */
@RunWith(AndroidJUnit4::class)
class NotificationsCardTest : ParentUiTest() {

    private val nextArray = arrayOf(NEXT_HOUR, NEXT_DAY, NEXT_WEEK, NEXT_MONTH, NEXT_YEAR)

    @Test fun timeFormatting() = startListTest(List(nextArray.size) {
        db.insertNotification(date = getClearCalendar(nextArray[it]).toText())
    })

    @Test fun colorIndicatorLight() = startColorTest(ThemeDisplayed.LIGHT)

    @Test fun colorIndicatorDark() = startColorTest(ThemeDisplayed.DARK)

    private fun startColorTest(theme: ThemeDisplayed) {
        setupTheme(theme)

        startListTest(List(Color.values().size) {
            val date = getClearCalendar(addMinutes = NEXT_HOUR + it * NEXT_HOUR).toText()
            val item = if (Random.nextBoolean()) {
                db.insertText(db.textNote.copy(color = Color.values()[it]))
            } else {
                db.insertRoll(db.rollNote.copy(color = Color.values()[it]))
            }

            return@List db.insertNotification(item, date)
        })
    }

    private fun startListTest(list: List<NoteItem>) = startNotificationsTest {
        assertList(list, withWait = true)
    }
}