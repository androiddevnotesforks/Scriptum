package sgtmelon.scriptum.domain.model.state

import android.os.Bundle
import android.os.Handler
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentTest
import kotlin.random.Random

/**
 * Test for [OpenState].
 */
class OpenStateTest : ParentTest() {

    private val openState by lazy { OpenState() }
    private val spyOpenState by lazy { spyk(openState) }

    override fun setUp() {
        super.setUp()

        assertFalse(openState.value)
        assertTrue(openState.changeEnabled)
        assertEquals(OpenState.Tag.ND, openState.tag)
        assertFalse(openState.skipClear)
    }

    @Test fun tryInvoke() {
        val func = mockk<() -> Unit>(relaxUnitFun = true)

        every { func.invoke() } returns Unit

        openState.changeEnabled = false
        openState.tryInvoke(func)

        assertFalse(openState.changeEnabled)

        verifySequence(inverse = true) { func() }

        openState.changeEnabled = true
        openState.value = true
        openState.tryInvoke(func)

        assertTrue(openState.changeEnabled)
        assertTrue(openState.value)

        verifySequence(inverse = true) { func() }

        openState.value = false
        openState.tryInvoke(func)

        assertTrue(openState.changeEnabled)
        assertTrue(openState.value)

        verifySequence { func() }
    }

    @Test fun tryInvoke_byTag() {
        val func = mockk<() -> Unit>(relaxUnitFun = true)
        val tag = Random.nextString()

        every { func.invoke() } returns Unit

        openState.tag = tag
        openState.changeEnabled = false
        openState.tryInvoke(Random.nextString(), func)

        assertFalse(openState.changeEnabled)

        verifySequence(inverse = true) { func() }

        openState.changeEnabled = true
        openState.value = true
        openState.tryInvoke(Random.nextString(), func)

        assertTrue(openState.changeEnabled)
        assertTrue(openState.value)

        verifySequence(inverse = true) { func() }

        openState.value = false
        openState.tryInvoke(Random.nextString(), func)

        assertTrue(openState.changeEnabled)
        assertTrue(openState.value)

        verifySequence { func() }

        openState.tryInvoke(tag, func)

        verifySequence {
            func()
            func()
        }
    }

    @Test fun tryReturnInvoke() {
        val func = mockk<() -> Boolean>(relaxUnitFun = true)
        val value = Random.nextBoolean()

        every { func.invoke() } returns value

        openState.changeEnabled = false
        assertNull(openState.tryReturnInvoke(func))

        assertFalse(openState.changeEnabled)

        verifySequence(inverse = true) { func() }

        openState.changeEnabled = true
        openState.value = true
        assertNull(openState.tryReturnInvoke(func))

        assertTrue(openState.changeEnabled)
        assertTrue(openState.value)

        verifySequence(inverse = true) { func() }

        openState.value = false
        assertEquals(value, openState.tryReturnInvoke(func))

        assertTrue(openState.changeEnabled)
        assertTrue(openState.value)

        verifySequence { func() }
    }

    @Test fun tryCall() {
        val func = mockk<() -> Unit>(relaxUnitFun = true)

        every { func.invoke() } returns Unit

        openState.changeEnabled = false
        openState.tryCall(func)

        assertFalse(openState.changeEnabled)

        verifySequence(inverse = true) { func() }

        openState.changeEnabled = true
        openState.value = true
        openState.tryCall(func)

        assertTrue(openState.changeEnabled)
        assertTrue(openState.value)

        verifySequence(inverse = true) { func() }

        openState.value = false
        openState.tryCall(func)

        assertTrue(openState.changeEnabled)
        assertFalse(openState.value)

        verifySequence { func() }
    }

    @Test fun block() {
        val handler = mockk<Handler>(relaxUnitFun = true)
        val time = Random.nextLong()

        every { handler.postDelayed(any(), time) } returns true

        openState.changeEnabled = true
        openState.handler = handler
        openState.block(time)

        assertFalse(openState.changeEnabled)

        verifySequence {
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed(any(), time)
        }
    }

    @Test fun blockEnd() {
        every { spyOpenState.clear() } returns Unit

        spyOpenState.changeEnabled = false
        spyOpenState.blockEnd()

        assertTrue(spyOpenState.changeEnabled)

        verifySequence {
            spyOpenState.changeEnabled = false
            spyOpenState.blockEnd()

            spyOpenState.clear()

            spyOpenState.changeEnabled
        }
    }

    @Test fun clear() {
        val tag = Random.nextString()

        openState.value = true
        openState.tag = tag

        openState.skipClear = true
        openState.clear()

        assertFalse(openState.skipClear)
        assertTrue(openState.value)
        assertEquals(tag, openState.tag)

        openState.changeEnabled = false
        openState.clear()

        assertFalse(openState.changeEnabled)
        assertTrue(openState.value)
        assertEquals(tag, openState.tag)

        openState.changeEnabled = true
        openState.clear()

        assertFalse(openState.value)
        assertEquals(OpenState.Tag.ND, openState.tag)
    }

    @Test fun clearBlockCallback() {
        val handler = mockk<Handler>(relaxUnitFun = true)

        spyOpenState.handler = handler
        spyOpenState.clearBlockCallback()

        verifySequence {
            spyOpenState.handler = handler
            spyOpenState.clearBlockCallback()

            handler.removeCallbacksAndMessages(null)
        }
    }

    @Test fun get() {
        val bundle = mockk<Bundle>(relaxUnitFun = true)

        val changeEnabled = Random.nextBoolean()
        val value = Random.nextBoolean()
        val tag = Random.nextString()

        every { bundle.getBoolean(OpenState.KEY_CHANGE) } returns changeEnabled
        every { bundle.getBoolean(OpenState.KEY_VALUE) } returns value
        every { bundle.getString(OpenState.KEY_TAG) } returns null

        openState.get(bundle)

        assertEquals(changeEnabled, openState.changeEnabled)
        assertEquals(value, openState.value)
        assertEquals(OpenState.Tag.ND, openState.tag)

        every { bundle.getString(OpenState.KEY_TAG) } returns tag

        openState.get(bundle)

        assertEquals(changeEnabled, openState.changeEnabled)
        assertEquals(value, openState.value)
        assertEquals(tag, openState.tag)

        verifySequence {
            repeat(times = 2) {
                bundle.getBoolean(OpenState.KEY_CHANGE)
                bundle.getBoolean(OpenState.KEY_VALUE)
                bundle.getString(OpenState.KEY_TAG)
            }
        }
    }

    @Test fun save() {
        val bundle = mockk<Bundle>(relaxUnitFun = true)

        val changeEnabled = Random.nextBoolean()
        val value = Random.nextBoolean()
        val tag = Random.nextString()

        openState.changeEnabled = changeEnabled
        openState.value = value
        openState.tag = tag

        openState.save(bundle)

        verifySequence {
            bundle.putBoolean(OpenState.KEY_CHANGE, changeEnabled)
            bundle.putBoolean(OpenState.KEY_VALUE, value)
            bundle.putString(OpenState.KEY_TAG, tag)
        }
    }

}