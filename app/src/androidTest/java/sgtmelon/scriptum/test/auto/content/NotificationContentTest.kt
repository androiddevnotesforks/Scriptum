package sgtmelon.scriptum.test.auto.content

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.getCalendarWithAdd
import sgtmelon.extension.getText
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.ui.screen.NotificationScreen

/**
 * Test for [NotificationScreen.Item]
 */
@RunWith(AndroidJUnit4::class)
class NotificationContentTest : ParentUiTest() {

    private val nextArray = arrayOf(NEXT_HOUR, NEXT_DAY, NEXT_WEEK, NEXT_MONTH, NEXT_YEAR)

    @Test fun time() = onAssertList(ArrayList<NoteItem>().also { list ->
        for (it in nextArray) {
            list.add(data.insertNotification(date = getCalendarWithAdd(it).getText()))
        }
    })

    @Test fun colorLight() = startColorTest(Theme.LIGHT)

    @Test fun colorDark() = startColorTest(Theme.DARK)

    private fun startColorTest(@Theme theme: Int) {
        setupTheme(theme)

        onAssertList(ArrayList<NoteItem>().also { list ->
            for ((i, it) in Color.list.withIndex()) {
                val date = getCalendarWithAdd(min = NEXT_HOUR + i * NEXT_HOUR).getText()
                val noteItem = data.insertText(data.textNote.copy(name = "", color = it))

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