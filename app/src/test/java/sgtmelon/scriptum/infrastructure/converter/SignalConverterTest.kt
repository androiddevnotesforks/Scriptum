package sgtmelon.scriptum.infrastructure.converter

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

import sgtmelon.scriptum.infrastructure.model.state.SignalState

/**
 * Test for [SignalConverter].
 */
class SignalConverterTest {

    private val converter = SignalConverter()

    private val emptyString = ""
    private val badString = "RandomStaff, 0, 1,2"
    private val normalString = "0, 2, 3, 1"

    @Test fun `string toState`() {
        val onEmptyState = SignalState(isMelody = false, isVibration = false)
        assertEquals(converter.toState(emptyString), onEmptyState)

        val onBadState = SignalState(isMelody = true, isVibration = false)
        assertEquals(converter.toState(badString), onBadState)

        val onNormalState = SignalState(isMelody = true, isVibration = true)
        assertEquals(converter.toState(normalString), onNormalState)
    }

    @Test fun `array toString`() {
        val emptyArray = booleanArrayOf()
        assertEquals(converter.toString(emptyArray), "")

        val bigSizeArray = booleanArrayOf(false, true, true, true)
        assertEquals(converter.toString(bigSizeArray), "1")

        val normalArray = booleanArrayOf(true, true)
        assertEquals(converter.toString(normalArray), "0, 1")
    }

    @Test fun `string toArray`() {
        assertArrayEquals(converter.toArray(emptyString), booleanArrayOf(false, false))
        assertArrayEquals(converter.toArray(badString), booleanArrayOf(true, false))
        assertArrayEquals(converter.toArray(normalString), booleanArrayOf(true, true))
    }
}