package sgtmelon.scriptum.infrastructure.converter

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.infrastructure.model.key.Sort

/**
 * Test for [SortConverter].
 */
class SortConverterTest : ParentEnumConverterTest<Sort>() {

    override val converter = SortConverter()

    override val randomValue: Sort get() = Sort.values().random()

    @Test override fun toEnum() {
        val value = randomValue
        assertEquals(converter.toEnum(value.ordinal), value)
    }
}