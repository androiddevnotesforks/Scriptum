package sgtmelon.scriptum.domain.model.state

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.parent.ParentTest
import kotlin.random.Random

/**
 * Test for [SignalState].
 */
class SignalStateTest : ParentTest() {

    @Test fun get() {
        assertNull(SignalState[booleanArrayOf()])

        val isMelody = Random.nextBoolean()
        val isVibration = Random.nextBoolean()
        val signalState = SignalState(isMelody, isVibration)
        assertEquals(signalState, SignalState[booleanArrayOf(isMelody, isVibration)])
    }
}