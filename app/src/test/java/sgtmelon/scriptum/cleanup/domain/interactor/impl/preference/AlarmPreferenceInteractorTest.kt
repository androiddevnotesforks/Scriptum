package sgtmelon.scriptum.cleanup.domain.interactor.impl.preference

import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verifySequence
import java.util.Locale
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.infrastructure.converter.SignalConverter
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.parent.ParentInteractorTest

/**
 * Test for [AlarmPreferenceInteractor].
 */
@ExperimentalCoroutinesApi
class AlarmPreferenceInteractorTest : ParentInteractorTest() {

    //region Setup

    @MockK lateinit var summaryProvider: SummaryProvider
    @MockK lateinit var preferences: Preferences
    @MockK lateinit var signalConverter: SignalConverter

    private val interactor by lazy {
        AlarmPreferenceInteractor(summaryProvider, preferences, signalConverter)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(summaryProvider, preferences, signalConverter)
    }

    //endregion

    @Test fun getRepeat() = FastTest.getRepeat(preferences) { interactor.repeat }

    @Test fun getRepeatSummary() {
        val size = getRandomSize()
        val valueArray = Array(size) { nextString() }
        val index = valueArray.indices.random()
        val value = valueArray[index]

        every { summaryProvider.repeat } returns valueArray
        every { spyInteractor.repeat } returns -1
        assertNull(spyInteractor.getRepeatSummary())

        every { spyInteractor.repeat } returns index
        assertEquals(value, spyInteractor.getRepeatSummary())

        verifySequence {
            repeat(times = 2) {
                spyInteractor.getRepeatSummary()
                summaryProvider.repeat
                spyInteractor.repeat
            }
        }
    }

    @Test fun updateRepeat() {
        val value = Random.nextInt()
        val summary = nextString()

        every { preferences.repeat = value } returns Unit
        every { spyInteractor.getRepeatSummary() } returns null
        assertNull(spyInteractor.updateRepeat(value))

        every { spyInteractor.getRepeatSummary() } returns summary
        assertEquals(summary, spyInteractor.updateRepeat(value))

        verifySequence {
            repeat(times = 2) {
                spyInteractor.updateRepeat(value)
                preferences.repeat = value
                spyInteractor.getRepeatSummary()
            }
        }
    }


    @Test fun getSignalSummaryArray() {
        val summaryArray = Array(size = 3) { nextString() }
        val checkArray = booleanArrayOf(true, false, true)

        fun String.getLow() = lowercase(Locale.getDefault())
        val resultString = "${summaryArray.first().getLow()}, ${summaryArray.last().getLow()}"

        coEvery { summaryProvider.signal } returns arrayOf(nextString())
        assertNull(interactor.getSignalSummary(checkArray))

        coEvery { summaryProvider.signal } returns summaryArray
        assertEquals(resultString, interactor.getSignalSummary(checkArray))

        verifySequence {
            summaryProvider.signal
            summaryProvider.signal
        }
    }

    /**
     * Can't mockk Arrays. Don't try.
     */
    @Test fun updateSignal() {
        val size = getRandomSize()
        val valueArray = BooleanArray(size) { Random.nextBoolean() }
        val value = nextString()
        val summary = nextString()

        every { signalConverter.toString(valueArray) } returns value
        every { preferences.signal = value } returns Unit
        every { spyInteractor.getSignalSummary(valueArray) } returns null
        assertNull(spyInteractor.updateSignal(valueArray))

        every { spyInteractor.getSignalSummary(valueArray) } returns summary
        assertEquals(summary, spyInteractor.updateSignal(valueArray))

        verifySequence {
            repeat(times = 2) {
                spyInteractor.updateSignal(valueArray)
                signalConverter.toString(valueArray)
                preferences.signal = value
                spyInteractor.getSignalSummary(valueArray)
            }
        }
    }


    @Test fun getVolume() = FastTest.getVolume(preferences) { interactor.volume }

    @Test fun getVolumeSummary() {
        val value = Random.nextInt()
        val summary = nextString()

        every { spyInteractor.volume } returns value
        every { summaryProvider.getVolume(value) } returns summary
        assertEquals(summary, spyInteractor.getVolumeSummary())

        verifySequence {
            spyInteractor.getVolumeSummary()
            spyInteractor.volume
            summaryProvider.getVolume(value)
        }
    }

    @Test fun updateVolume() {
        val value = Random.nextInt()
        val summary = nextString()

        every { preferences.volume = value } returns Unit
        every { spyInteractor.getVolumeSummary() } returns summary
        assertEquals(summary, spyInteractor.updateVolume(value))

        verifySequence {
            spyInteractor.updateVolume(value)
            preferences.volume = value
            spyInteractor.getVolumeSummary()
        }
    }

}