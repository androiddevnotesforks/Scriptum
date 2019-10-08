package sgtmelon.scriptum.test.content

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.basic.extension.getTime
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.ui.screen.NotificationScreen

/**
 * Test for [NotificationScreen.Item]
 */
@RunWith(AndroidJUnit4::class)
class NotificationContentTest : ParentUiTest() {

    @Test fun time() = onAssertList(ArrayList<NoteModel>().also { list ->
        nextArray.forEach { list.add(data.insertNotification(date = getTime(it))) }
    })

    @Test fun colorLight() = startColorTest(Theme.LIGHT)

    @Test fun colorDark() = startColorTest(Theme.DARK)

    private fun startColorTest(@Theme theme: Int) {
        iPreferenceRepo.theme = theme

        onAssertList(ArrayList<NoteModel>().also { list ->
            Color.list.forEach {
                with(data) { list.add(insertNotification(insertText(textNote.copy(color = it)))) }
            }
        })
    }


    private fun onAssertList(list: List<NoteModel>) {
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