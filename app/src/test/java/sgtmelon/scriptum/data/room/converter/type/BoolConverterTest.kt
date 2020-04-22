package sgtmelon.scriptum.data.room.converter.type

import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [BoolConverter].
 */
class BoolConverterTest : ParentTest() {

    private val converter = BoolConverter()

    @Test fun toInt() {
        TODO()
        assertEquals(0, converter.toInt(false) )
        assertEquals(1, converter.toInt(true) )
    }

    @Test fun toBool() {
        TODO()
        assertTrue(converter.toBool(1))
        assertFalse(converter.toBool(0))
        assertFalse(converter.toBool(-1))
    }

}