package sgtmelon.scriptum.data.room.converter.type

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.domain.model.key.NoteType

/**
 * Test for [NoteTypeConverter].
 */
class NoteTypeConverterTest : ParentTest() {

    private val converter = NoteTypeConverter()

    @Test fun toInt() {
        assertEquals(0, converter.toInt(NoteType.TEXT))
        assertEquals(1, converter.toInt(NoteType.ROLL))
    }

    @Test fun toEnum() {
        assertEquals(NoteType.TEXT, converter.toEnum(0))
        assertEquals(NoteType.ROLL, converter.toEnum(1))
    }

}