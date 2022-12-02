package sgtmelon.scriptum.infrastructure.converter.key

import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.infrastructure.model.exception.converter.EnumConverterException
import sgtmelon.scriptum.infrastructure.utils.extensions.record

/**
 * Parent class for testing child of [ParentEnumConverter].
 */
abstract class ParentEnumConverterTest<E: Enum<E>> {

    abstract val converter: ParentEnumConverter<E>
    private val spyConverter by lazy { spyk(converter) }

    abstract val values: Array<E>

    private val randomValue: E get() = values.random()

    @Test fun `enum values() are equal`() {
        assertArrayEquals(converter.values, values)
    }

    @Test fun toInt() {
        val value = randomValue
        assertEquals(converter.toInt(value), value.ordinal)
    }

    @Test fun toEnum() {
        val value = randomValue
        assertEquals(converter.toEnum(value.ordinal), value)
    }

    @Test fun `toEnum with bad ordinal`() {
        val ordinal = -1
        val crashlytics = mockk<FirebaseCrashlytics>()
        val exception = mockk<EnumConverterException>()

        FastMock.fireExtensions()
        every { spyConverter.getOrdinalException(ordinal) } returns exception
        every { exception.record() } returns exception

        assertNull(spyConverter.toEnum(ordinal))

        verifySequence {
            spyConverter.toEnum(ordinal)
            spyConverter.values
            spyConverter.getOrdinalException(ordinal)
            exception.record()
        }
    }
}