package sgtmelon.scriptum.data.room.converter.type

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.data.room.converter.model.StringConverter

/**
 * Test for [StringConverter].
 */
class StringConverterTest : ParentTest() {

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
        const val stringFirst = "1, 2, 3, 4, 5"

        val listSecond: List<Long> = arrayListOf()
        const val stringSecond = StringConverter.NONE
    }

}