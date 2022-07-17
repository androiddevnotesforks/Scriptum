package sgtmelon.scriptum.infrastructure.converter

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.infrastructure.model.key.SavePeriod

/**
 * Test for [SavePeriodConverter].
 */
class SavePeriodConverterTest : ParentEnumConverterTest<SavePeriod>() {

    override val converter = SavePeriodConverter()

    override val randomValue: SavePeriod get() = SavePeriod.values().random()

    @Test override fun toEnum() {
        val value = randomValue
        assertEquals(converter.toEnum(value.ordinal), value)
    }
}