package sgtmelon.scriptum.domain.model.annotation

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [Signal].
 */
class SignalTest : ParentTest() {

    @Test fun valueCheck() {
        assertEquals(0, Signal.MELODY)
        assertEquals(1, Signal.VIBRATION)

        assertEquals(2, Signal.digitCount)
    }

}