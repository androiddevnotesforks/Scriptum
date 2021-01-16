package sgtmelon.scriptum.data.room.converter.type

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest


/**
 * Test for [IntConverter].
 */
class IntConverterTest : ParentTest() {

    //region Data

    private val zeroArray = booleanArrayOf(F, F, F)
    private val oneArray = booleanArrayOf(T, F, F)
    private val troArray = booleanArrayOf(F, T, F)
    private val threeArray = booleanArrayOf(T, T, F)
    private val fourArray = booleanArrayOf(F, F, T)
    private val fiveArray = booleanArrayOf(T, F, T)
    private val sixArray = booleanArrayOf(F, T, T)
    private val sevenArray = booleanArrayOf(T, T, T)

    //endregion

    private val converter = IntConverter()

    @Test fun toInt() = with(converter) {
        assertEquals(toInt(zeroArray), 0)
        assertEquals(toInt(oneArray), 1)
        assertEquals(toInt(troArray), 2)
        assertEquals(toInt(threeArray), 3)
        assertEquals(toInt(fourArray), 4)
        assertEquals(toInt(fiveArray), 5)
        assertEquals(toInt(sixArray), 6)
        assertEquals(toInt(sevenArray), 7)

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

    companion object {
        private const val F = false
        private const val T = true
    }
}