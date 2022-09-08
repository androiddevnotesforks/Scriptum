package sgtmelon.safedialog.dialog.time

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verifySequence
import java.util.Calendar
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.extensions.isAfterNow
import sgtmelon.extensions.toText
import sgtmelon.safedialog.ParentTest
import sgtmelon.test.common.nextString

/**
 * Test for [TimeDialog].
 */
class TimeDialogTest : ParentTest() {

    @Test fun getPositiveEnabled() {
        mockkStatic("sgtmelon.extensions.TimeExtensionsUtils")

        val calendar = mockk<Calendar>()
        val dateList = List(size = 5) { nextString() }

        every { calendar.isAfterNow() } returns false
        every { calendar.toText() } returns dateList.random()

        assertFalse(TimeDialog.getPositiveEnabled(calendar, dateList))

        every { calendar.toText() } returns nextString()

        assertFalse(TimeDialog.getPositiveEnabled(calendar, dateList))

        every { calendar.isAfterNow() } returns true
        every { calendar.toText() } returns dateList.random()

        assertFalse(TimeDialog.getPositiveEnabled(calendar, dateList))

        every { calendar.toText() } returns nextString()

        assertTrue(TimeDialog.getPositiveEnabled(calendar, dateList))

        verifySequence {
            repeat(times = 2) {
                calendar.isAfterNow()
            }

            repeat(times = 2) {
                calendar.isAfterNow()
                calendar.toText()
            }
        }
    }
}