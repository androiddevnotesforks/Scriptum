package sgtmelon.scriptum.infrastructure.converter

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.infrastructure.model.key.Theme

/**
 * Test for [ThemeConverter].
 */
class ThemeConverterTest : ParentEnumConverterTest<Theme>() {

    override val converter = ThemeConverter()

    override val randomValue: Theme get() = Theme.values().random()

    @Test override fun toEnum() {
        val value = randomValue
        assertEquals(converter.toEnum(value.ordinal), value)
    }
}