package sgtmelon.scriptum.infrastructure.converter.types

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.test.common.nextString

/**
 * Test for [NumbersJoinConverter].
 */
class NumbersJoinConverterTest : ParentTest() {

    private val goodList: List<Long> = mutableListOf(1, 2, 3, 4, 5)
    private val goodString = "1, 2, 3, 4, 5"
    private val badList: List<Long> = mutableListOf(1, 93)
    private val badString = "asb, 1, asdj,12, 93"

    private val converter = NumbersJoinConverter()

    @Test fun `toList with empty text`() {
        assertEquals(converter.toList(value = ""), mutableListOf<Long>())
    }

    @Test fun `toList with wrong value`() {
        assertEquals(converter.toList(nextString()), mutableListOf<Long>())
    }

    @Test fun `toList with EMPTY value`() {
        assertEquals(converter.toList(NumbersJoinConverter.EMPTY), mutableListOf<Long>())
    }

    @Test fun `toList with bad values`() {
        assertEquals(converter.toList(badString), badList)
    }

    @Test fun `toList with normal values`() {
        assertEquals(converter.toList(goodString), goodList)
    }

    @Test fun `toString with null list`() {
        assertEquals(converter.toString(value = null), NumbersJoinConverter.EMPTY)
    }

    @Test fun `toString with empty list`() {
        assertEquals(converter.toString(mutableListOf()), NumbersJoinConverter.EMPTY)
    }

    @Test fun `toString with normal values`() {
        assertEquals(converter.toString(goodList), goodString)
    }
}