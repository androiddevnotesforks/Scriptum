package sgtmelon.scriptum.infrastructure.converter

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.infrastructure.model.key.Color

/**
 * Test for [ColorConverter].
 */
class ColorConverterTest : ParentEnumConverterTest<Color>() {

    override val converter = ColorConverter()

    override val randomValue: Color get() = Color.values().random()

    @Test override fun toEnum() {
        val value = randomValue
        assertEquals(converter.toEnum(value.ordinal), value)
    }
}