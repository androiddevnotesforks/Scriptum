package sgtmelon.scriptum.data.room.converter.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.data.room.converter.type.StringConverter
import kotlin.random.Random

/**
 * Test for [StringConverter].
 */
class StringConverterTest : ParentTest() {

    private val converter = StringConverter()

    @Test fun toList() {
        assertEquals(mutableListOf<Long>(), converter.toList(Random.nextString()))

        assertEquals(firstList, converter.toList(firstString))
        assertEquals(secondList, converter.toList(secondString))

        assertNotEquals(firstList, converter.toList(secondString))
    }

    @Test fun convertToString() {
        assertEquals(firstString, converter.toString(firstList))
        assertEquals(secondString, converter.toString(secondList))

        assertNotEquals(secondString, converter.toString(firstList))
    }

    private val firstList: List<Long> = arrayListOf(1, 2, 3, 4, 5)
    private val secondList: List<Long> = arrayListOf()

    companion object {
        private const val firstString = "1, 2, 3, 4, 5"
        private const val secondString = StringConverter.NONE
    }

}