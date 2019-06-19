package sgtmelon.scriptum.room.converter

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.model.key.NoteType

/**
 * Тест для [NoteTypeConverter]
 *
 * @author SerjantArbuz
 */
class NoteTypeConverterTest {

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