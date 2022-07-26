package sgtmelon.scriptum.infrastructure.converter.key

import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verifySequence
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.infrastructure.model.exception.EnumConverterException

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

        mockkStatic(FirebaseCrashlytics::class)
        every { FirebaseCrashlytics.getInstance() } returns crashlytics
        every { crashlytics.recordException(exception) } returns Unit
        every { spyConverter.getOrdinalException(ordinal) } returns exception

        assertNull(spyConverter.toEnum(ordinal))

        verifySequence {
            spyConverter.toEnum(ordinal)
            spyConverter.values
            spyConverter.getOrdinalException(ordinal)
            FirebaseCrashlytics.getInstance()
            crashlytics.recordException(exception)
        }
    }
}