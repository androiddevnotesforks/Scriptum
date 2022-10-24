package sgtmelon.scriptum.cleanup.data.room.converter.type

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Test for [BoolConverter].
 */
class BoolConverterTest : ParentTest() {

    private val converter = BoolConverter()

    @Test fun toInt() {
        assertEquals(0, converter.toInt(value = false) )
        assertEquals(1, converter.toInt(value = true) )
    }

    @Test fun toBool() {
        assertTrue(converter.toBool(value = 1))
        assertFalse(converter.toBool(value = 0))
        assertFalse(converter.toBool(value = -1))
    }
}