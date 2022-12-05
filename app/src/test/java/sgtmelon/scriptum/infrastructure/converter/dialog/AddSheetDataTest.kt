package sgtmelon.scriptum.infrastructure.converter.dialog

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.infrastructure.model.exception.InvalidIdException
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.utils.extensions.record
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Test for [AddSheetData].
 */
class AddSheetDataTest : ParentTest() {

    private val data = AddSheetData()

    @Test fun convert() {
        assertEquals(data.convert(R.id.item_add_text), NoteType.TEXT)
        assertEquals(data.convert(R.id.item_add_roll), NoteType.ROLL)

        FastMock.fireExtensions()
        every { any<InvalidIdException>().record() } returns mockk()

        assertNull(data.convert(itemId = -1))
    }
}