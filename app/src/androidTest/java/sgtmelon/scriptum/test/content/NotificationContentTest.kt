package sgtmelon.scriptum.test.content

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.getDateFormat
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.ui.screen.NotificationScreen
import java.util.*

/**
 * Test for [NotificationScreen.Item]
 */
@RunWith(AndroidJUnit4::class)
class NotificationContentTest : ParentUiTest() {

    private fun startTest(addValue: Int) {
        data.insertNotification(date = getTime(addValue)).let {
            launch { mainScreen { openNotesPage { openNotification { onAssertItem(it) } } } }
        }
    }


    @Test fun itemNextHour() = startTest(NEXT_HOUR)

    @Test fun itemNextDay() = startTest(NEXT_DAY)

    @Test fun itemNextWeek() = startTest(NEXT_WEEK)

    @Test fun itemNextMonth() = startTest(NEXT_MONTH)

    @Test fun itemNextYear() = startTest(NEXT_YEAR)


    private fun getTime(addValue: Int) = getDateFormat().format(Calendar.getInstance().apply {
        add(Calendar.MINUTE, addValue)
    }.time)

    private companion object {
        const val NEXT_HOUR = 60
        const val NEXT_DAY = NEXT_HOUR * 24
        const val NEXT_WEEK = NEXT_DAY * 7
        const val NEXT_MONTH = NEXT_DAY * 30
        const val NEXT_YEAR = NEXT_MONTH * 12
    }

}