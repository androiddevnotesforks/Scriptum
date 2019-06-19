package sgtmelon.scriptum.room.converter

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * Тест для [StringConverter]
 *
 * @author SerjantArbuz
 */
class StringConverterTest {

    private val converter = StringConverter()

    @Test fun toList() {
        assertEquals(listFirst, converter.toList(stringFirst))
        assertEquals(listSecond, converter.toList(stringSecond))

        assertNotEquals(listFirst, converter.toList(stringSecond))
    }

    @Test fun convertToString() {
        assertEquals(stringFirst, converter.toString(listFirst))
        assertEquals(stringSecond, converter.toString(listSecond))

        assertNotEquals(stringSecond, converter.toString(listFirst))
    }

    private companion object {
        val listFirst: List<Long> = arrayListOf(1, 2, 3, 4, 5)
        const val stringFirst = "1,2,3,4,5"

        val listSecond: List<Long> = arrayListOf()
        const val stringSecond = StringConverter.NONE
    }

}