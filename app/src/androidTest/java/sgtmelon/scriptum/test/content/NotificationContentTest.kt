package sgtmelon.scriptum.test.content

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.getDateFormat
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.ui.screen.NotificationScreen
import java.util.*
import kotlin.collections.ArrayList

/**
 * Test for [NotificationScreen.Item]
 */
@RunWith(AndroidJUnit4::class)
class NotificationContentTest : ParentUiTest() {

    @Test fun itemTime() {
        val list = ArrayList<NoteModel>()

        nextArray.forEach { list.add(data.insertNotification(date = getTime(it))) }

        launch {
            mainScreen {
                openNotesPage {
                    openNotification { list.forEachIndexed { p, model -> onAssertItem(p, model) } }
                }
            }
        }
    }


    private fun getTime(addValue: Int) = getDateFormat().format(Calendar.getInstance().apply {
        add(Calendar.MINUTE, addValue)
    }.time)

    private companion object {
        const val NEXT_HOUR = 60
        const val NEXT_DAY = NEXT_HOUR * 24
        const val NEXT_WEEK = NEXT_DAY * 7
        const val NEXT_MONTH = NEXT_DAY * 30
        const val NEXT_YEAR = NEXT_MONTH * 12

        val nextArray = arrayOf(NEXT_HOUR, NEXT_DAY, NEXT_WEEK, NEXT_MONTH, NEXT_YEAR)
    }

}