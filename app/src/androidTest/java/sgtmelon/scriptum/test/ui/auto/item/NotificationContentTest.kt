package sgtmelon.scriptum.test.ui.auto.item

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.common.utils.getCalendarWithAdd
import sgtmelon.common.utils.getText
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.ui.item.NotificationItemUi

/**
 * Test for [NotificationItemUi]
 */
@RunWith(AndroidJUnit4::class)
class NotificationContentTest : ParentUiTest() {

    private val nextArray = arrayOf(NEXT_HOUR, NEXT_DAY, NEXT_WEEK, NEXT_MONTH, NEXT_YEAR)

    @Test fun time() = onAssertList(ArrayList<NoteItem>().also { list ->
        for (it in nextArray) {
            list.add(data.insertNotification(date = getCalendarWithAdd(it).getText()))
        }
    })

    @Test fun colorLight() = startColorTest(ThemeDisplayed.LIGHT)

    @Test fun colorDark() = startColorTest(ThemeDisplayed.DARK)

    private fun startColorTest(theme: ThemeDisplayed) {
        setupTheme(theme)

        onAssertList(ArrayList<NoteItem>().also { list ->
            for ((i, it) in Color.values().withIndex()) {
                val date = getCalendarWithAdd(min = NEXT_HOUR + i * NEXT_HOUR).getText()
                val noteItem = data.insertText(data.textNote.copy(name = "", color = it.ordinal))

                list.add(data.insertNotification(noteItem, date))
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