package sgtmelon.scriptum.infrastructure.dialogs.data

import io.mockk.every
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.infrastructure.utils.record

/**
 * Test for [RepeatSheetData].
 */
class RepeatSheetDataTest : ParentTest() {

    private val data = RepeatSheetData()

    @Test fun convert() {
        assertEquals(data.convert(R.id.item_repeat_0), Repeat.MIN_10)
        assertEquals(data.convert(R.id.item_repeat_1), Repeat.MIN_30)
        assertEquals(data.convert(R.id.item_repeat_2), Repeat.MIN_60)
        assertEquals(data.convert(R.id.item_repeat_3), Repeat.MIN_180)
        assertEquals(data.convert(R.id.item_repeat_4), Repeat.MIN_1440)

        FastMock.fireExtensions()
        every { any<NullPointerException>().record() } returns Unit

        assertNull(data.convert(itemId = -1))
    }
}