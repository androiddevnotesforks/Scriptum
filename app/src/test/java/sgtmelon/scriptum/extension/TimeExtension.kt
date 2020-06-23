package sgtmelon.scriptum.extension

import org.junit.Assert
import org.junit.Test
import sgtmelon.extension.clearSeconds
import sgtmelon.extension.getCalendar
import sgtmelon.extension.getCalendarWithAdd
import java.util.*

class TimeExtension {

    @Test fun calendarWithAdd() {
        listOf(1, 10, 30, 43).forEach {
            val calendar = getCalendar().clearSeconds().apply { add(Calendar.MINUTE, it) }
            Assert.assertEquals(calendar, getCalendarWithAdd(it))
        }
    }

}