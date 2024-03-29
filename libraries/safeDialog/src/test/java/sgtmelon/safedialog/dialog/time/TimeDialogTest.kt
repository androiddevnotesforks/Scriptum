package sgtmelon.safedialog.dialog.time

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verifySequence
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.extensions.isAfterNow
import sgtmelon.extensions.toText
import sgtmelon.test.common.getRandomSize
import sgtmelon.test.common.nextString
import sgtmelon.tests.uniter.ParentTest
import java.util.Calendar

/**
 * Test for [TimeDialog].
 */
class TimeDialogTest : ParentTest() {

    @Test fun getPositiveEnabled() {
        mockkStatic("sgtmelon.extensions.TimeExtensionsUtils")

        val calendar = mockk<Calendar>()
        val dateList = List(getRandomSize()) { nextString() }

        every { calendar.isAfterNow } returns false
        every { calendar.toText() } returns dateList.random()

        assertFalse(TimeDialog.getPositiveEnabled(calendar, dateList))

        every { calendar.toText() } returns nextString()

        assertFalse(TimeDialog.getPositiveEnabled(calendar, dateList))

        every { calendar.isAfterNow } returns true
        every { calendar.toText() } returns dateList.random()

        assertFalse(TimeDialog.getPositiveEnabled(calendar, dateList))

        every { calendar.toText() } returns nextString()

        assertTrue(TimeDialog.getPositiveEnabled(calendar, dateList))

        verifySequence {
            repeat(times = 2) {
                calendar.isAfterNow
            }

            repeat(times = 2) {
                calendar.isAfterNow
                calendar.toText()
            }
        }
    }
}