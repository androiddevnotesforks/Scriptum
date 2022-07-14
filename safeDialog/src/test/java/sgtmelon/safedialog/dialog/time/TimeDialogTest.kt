package sgtmelon.safedialog.dialog.time

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verifySequence
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.common.utils.afterNow
import sgtmelon.common.utils.getText
import sgtmelon.common.utils.nextString
import sgtmelon.safedialog.ParentTest
import java.util.*

/**
 * Test for [TimeDialog].
 */
class TimeDialogTest : ParentTest() {

    @Test fun getPositiveEnabled() {
        mockkStatic("sgtmelon.common.utils.TimeExtensionUtils")

        val calendar = mockk<Calendar>()
        val dateList = List(size = 5) { nextString() }

        every { calendar.afterNow() } returns false
        every { calendar.getText() } returns dateList.random()

        assertFalse(TimeDialog.getPositiveEnabled(calendar, dateList))

        every { calendar.getText() } returns nextString()

        assertFalse(TimeDialog.getPositiveEnabled(calendar, dateList))

        every { calendar.afterNow() } returns true
        every { calendar.getText() } returns dateList.random()

        assertFalse(TimeDialog.getPositiveEnabled(calendar, dateList))

        every { calendar.getText() } returns nextString()

        assertTrue(TimeDialog.getPositiveEnabled(calendar, dateList))

        verifySequence {
            repeat(times = 2) {
                calendar.afterNow()
            }

            repeat(times = 2) {
                calendar.afterNow()
                calendar.getText()
            }
        }
    }
}