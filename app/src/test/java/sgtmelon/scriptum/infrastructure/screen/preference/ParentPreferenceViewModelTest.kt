package sgtmelon.scriptum.infrastructure.screen.preference

import androidx.lifecycle.LiveData
import io.mockk.every
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.Assert.assertEquals
import sgtmelon.scriptum.testing.getOrAwaitValue
import sgtmelon.scriptum.testing.parent.ParentLiveDataTest
import sgtmelon.test.common.nextString

/**
 * Test for preference viewModels.
 */
abstract class ParentPreferenceViewModelTest : ParentLiveDataTest() {

    abstract fun verifySetup()

    inline fun <T> getPreferenceTest(
        value: T,
        crossinline getPrefFunc: () -> T,
        getViewModelFunc: () -> T
    ) {
        every { getPrefFunc() } returns value

        assertEquals(getViewModelFunc(), value)

        verifySequence {
            verifySetup()
            getPrefFunc()
        }
    }

    inline fun <T> getSummaryTest(value: T, getViewModelFunc: () -> LiveData<T>) {
        assertEquals(getViewModelFunc().value, value)

        verifySequence {
            verifySetup()
        }
    }

    inline fun updateValueTest(
        crossinline getSummaryFunc: (Int) -> String,
        updateFunc: (Int) -> Unit,
        getResultFunc: () -> LiveData<String>
    ) {
        val value = Random.nextInt()
        val summary = nextString()

        every { getSummaryFunc(value) } returns summary

        updateFunc(value)

        assertEquals(getResultFunc().getOrAwaitValue(), summary)

        verifySequence {
            verifySetup()
            getSummaryFunc(value)
        }
    }
}