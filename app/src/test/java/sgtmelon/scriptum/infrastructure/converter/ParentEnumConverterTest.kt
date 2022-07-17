package sgtmelon.scriptum.infrastructure.converter

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

abstract class ParentEnumConverterTest<E: Enum<E>> {

    abstract val converter: ParentEnumConverter<E>

    abstract val randomValue: E

    @Test fun toInt() {
        val value = randomValue
        assertEquals(converter.toInt(value), value.ordinal)
    }

    abstract fun toEnum()

    @Test fun `toEnum with bad ordinal`() {
        assertNull(converter.toEnum(value = -1))
    }
}