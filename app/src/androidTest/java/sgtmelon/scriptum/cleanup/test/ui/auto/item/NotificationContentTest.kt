package sgtmelon.scriptum.cleanup.test.ui.auto.item

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.common.utils.getClearCalendar
import sgtmelon.common.utils.toText
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.ui.item.NotificationItemUi
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.parent.ParentUiTest

/**
 * Test for [NotificationItemUi]
 */
@RunWith(AndroidJUnit4::class)
class NotificationContentTest : ParentUiTest() {

    private val nextArray = arrayOf(NEXT_HOUR, NEXT_DAY, NEXT_WEEK, NEXT_MONTH, NEXT_YEAR)

    @Test fun time() = onAssertList(ArrayList<NoteItem>().also { list ->
        for (it in nextArray) {
            list.add(db.insertNotification(date = getClearCalendar(it).toText()))
        }
    })

    @Test fun colorLight() = startColorTest(ThemeDisplayed.LIGHT)

    @Test fun colorDark() = startColorTest(ThemeDisplayed.DARK)

    private fun startColorTest(theme: ThemeDisplayed) {
        setupTheme(theme)

        onAssertList(ArrayList<NoteItem>().also { list ->
            for ((i, it) in Color.values().withIndex()) {
                val date = getClearCalendar(addMinutes = NEXT_HOUR + i * NEXT_HOUR).toText()
                val noteItem = db.insertText(db.textNote.copy(name = "", color = it))

                list.add(db.insertNotification(noteItem, date))
            }
        })
    }


    private fun onAssertList(list: List<NoteItem>) {
        launch {
            mainScreen {
                notesScreen {
                    openNotification { for ((p, it) in list.withIndex()) onAssertItem(p, it) }
                }
            }
        }
    }

    companion object {
        private const val NEXT_HOUR = 60
        private const val NEXT_DAY = NEXT_HOUR * 24
        private const val NEXT_WEEK = NEXT_DAY * 7
        private const val NEXT_MONTH = NEXT_DAY * 30
        private const val NEXT_YEAR = NEXT_MONTH * 12
    }
}