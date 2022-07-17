package sgtmelon.scriptum.infrastructure.converter

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.infrastructure.model.key.Repeat

/**
 * Test for [RepeatConverter].
 */
class RepeatConverterTest : ParentEnumConverterTest<Repeat>() {

    override val converter = RepeatConverter()

    override val randomValue: Repeat get() = Repeat.values().random()

    @Test override fun toEnum() {
        val value = randomValue
        assertEquals(converter.toEnum(value.ordinal), value)
    }
}