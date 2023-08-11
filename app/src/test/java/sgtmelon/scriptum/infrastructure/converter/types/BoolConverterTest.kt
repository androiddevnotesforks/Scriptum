package sgtmelon.scriptum.infrastructure.converter.types

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Test for [BoolConverter].
 */
class BoolConverterTest : sgtmelon.tests.uniter.ParentTest() {

    private val converter = BoolConverter()

    @Test fun toInt() {
        assertEquals(converter.toInt(value = false), 0)
        assertEquals(converter.toInt(value = true), 1)
    }

    @Test fun toBool() {
        assertTrue(converter.toBool(value = 1))
        assertFalse(converter.toBool(value = 0))
        assertFalse(converter.toBool(value = -1))
        assertFalse(converter.toBool(value = 10))
    }
}