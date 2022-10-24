package sgtmelon.scriptum.infrastructure.dialogs.data

import io.mockk.every
import org.junit.Test
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.scriptum.infrastructure.model.exception.InvalidIdException
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.utils.record
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

/**
 * Test for [AddSheetData].
 */
class AddSheetDataTest : ParentTest() {

    private val data = AddSheetData()

    @Test fun convert() {
        assertEquals(data.convert(R.id.item_add_text), NoteType.TEXT)
        assertEquals(data.convert(R.id.item_add_roll), NoteType.ROLL)

        FastMock.fireExtensions()
        every { any<InvalidIdException>().record() } returns Unit

        assertNull(data.convert(itemId = -1))
    }
}