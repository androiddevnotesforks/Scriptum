package sgtmelon.scriptum.cleanup.data.room.converter.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.data.room.converter.type.StringConverter
import sgtmelon.scriptum.cleanup.parent.ParentTest

/**
 * Test for [StringConverter].
 */
class StringConverterTest : ParentTest() {

    //region Data

    private val firstList: List<Long> = arrayListOf(1, 2, 3, 4, 5)
    private val secondList: List<Long> = arrayListOf()

    private val firstString = "1, 2, 3, 4, 5"
    private val secondString = StringConverter.NONE

    //endregion

    private val converter = StringConverter()

    @Test fun toList() {
        assertEquals(mutableListOf<Long>(), converter.toList(nextString()))

        assertEquals(firstList, converter.toList(firstString))
        assertEquals(secondList, converter.toList(secondString))

        assertNotEquals(firstList, converter.toList(secondString))
    }

    @Test fun convertToString() {
        assertEquals(firstString, converter.toString(firstList))
        assertEquals(secondString, converter.toString(secondList))

        assertNotEquals(secondString, converter.toString(firstList))
    }
}