package sgtmelon.scriptum.room.converter.type

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest


/**
 * Test for [IntConverter].
 */
class IntConverterTest : ParentTest() {

    private val converter = IntConverter()

    @Test fun toInt() = with(converter) {
        assertEquals(toInt(ZERO), 0)
        assertEquals(toInt(ONE), 1)
        assertEquals(toInt(TWO), 2)
        assertEquals(toInt(THREE), 3)
        assertEquals(toInt(FOUR), 4)
        assertEquals(toInt(FIVE), 5)
        assertEquals(toInt(SIX), 6)
        assertEquals(toInt(SEVEN), 7)

        assertNotEquals(toInt(booleanArrayOf(F, T, T, T)), 7)
        assertEquals(toInt(booleanArrayOf(T, T, T, F)), 7)
    }

    @Test fun toArray() = with(converter) {
        assertEquals(toArray(value = 0, minSize = 2).toList(), arrayListOf(F, F))
        assertEquals(toArray(value = 1, minSize = 2).toList(), arrayListOf(T, F))
        assertEquals(toArray(value = 2, minSize = 2).toList(), arrayListOf(F, T))
        assertEquals(toArray(value = 3, minSize = 2).toList(), arrayListOf(T, T))
        assertEquals(toArray(value = 4, minSize = 3).toList(), arrayListOf(F, F, T))
        assertEquals(toArray(value = 5, minSize = 3).toList(), arrayListOf(T, F, T))
        assertEquals(toArray(value = 6, minSize = 3).toList(), arrayListOf(F, T, T))
        assertEquals(toArray(value = 7, minSize = 3).toList(), arrayListOf(T, T, T))

        assertNotEquals(toArray(value = 7, minSize = 4).toList(), arrayListOf(F, T, T, T))
        assertEquals(toArray(value = 7, minSize = 4).toList(), arrayListOf(T, T, T, F))
    }

    @Test fun inScope() = with(converter) {
        assertEquals(toInt(toArray(value = 0)), 0)
        assertEquals(toInt(toArray(value = 1)), 1)
        assertEquals(toInt(toArray(value = 2)), 2)
        assertEquals(toInt(toArray(value = 3)), 3)
        assertEquals(toInt(toArray(value = 4)), 4)
        assertEquals(toInt(toArray(value = 5)), 5)
        assertEquals(toInt(toArray(value = 6)), 6)
        assertEquals(toInt(toArray(value = 7)), 7)

        assertNotEquals(toInt(toArray(value = 7)), 8)
        assertEquals(toInt(toArray(value = -7)), 7)
    }

    private companion object {
        const val F = false
        const val T = true

        val ZERO = booleanArrayOf(F, F, F)
        val ONE = booleanArrayOf(T, F, F)
        val TWO = booleanArrayOf(F, T, F)
        val THREE = booleanArrayOf(T, T, F)
        val FOUR = booleanArrayOf(F, F, T)
        val FIVE = booleanArrayOf(T, F, T)
        val SIX = booleanArrayOf(F, T, T)
        val SEVEN = booleanArrayOf(T, T, T)
    }

}