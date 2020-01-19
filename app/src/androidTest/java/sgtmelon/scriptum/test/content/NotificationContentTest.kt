package sgtmelon.scriptum.test.content

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.getText
import sgtmelon.scriptum.basic.extension.getTime
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.ui.screen.NotificationScreen

/**
 * Test for [NotificationScreen.Item]
 */
@RunWith(AndroidJUnit4::class)
class NotificationContentTest : ParentUiTest() {

    @Test fun time() = onAssertList(ArrayList<NoteItem>().also { list ->
        nextArray.forEach { list.add(data.insertNotification(date = getTime(it).getText())) }
    })

    @Test fun colorLight() = startColorTest(Theme.LIGHT)

    @Test fun colorDark() = startColorTest(Theme.DARK)

    private fun startColorTest(@Theme theme: Int) {
        iPreferenceRepo.theme = theme

        onAssertList(ArrayList<NoteItem>().also { list ->
            Color.list.forEachIndexed { i, it ->
                val date = getTime(min = NEXT_HOUR + i * NEXT_HOUR).getText()
                val noteItem = data.insertText(data.textNote.copy(name = "", color = it))

                list.add(data.insertNotification(noteItem, date))
            }
        })
    }


    private fun onAssertList(list: List<NoteItem>) {
        launch {
            mainScreen {
                notesScreen {
                    openNotification { list.forEachIndexed { p, model -> onAssertItem(p, model) } }
                }
            }
        }
    }

    private companion object {
        const val NEXT_HOUR = 60
        const val NEXT_DAY = NEXT_HOUR * 24
        const val NEXT_WEEK = NEXT_DAY * 7
        const val NEXT_MONTH = NEXT_DAY * 30
        const val NEXT_YEAR = NEXT_MONTH * 12

        val nextArray = arrayOf(NEXT_HOUR, NEXT_DAY, NEXT_WEEK, NEXT_MONTH, NEXT_YEAR)
    }

}