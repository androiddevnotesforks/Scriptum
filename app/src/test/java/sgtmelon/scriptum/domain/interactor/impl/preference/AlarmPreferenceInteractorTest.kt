package sgtmelon.scriptum.domain.interactor.impl.preference

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.common.nextString
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.room.converter.type.IntConverter
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.parent.ParentInteractorTest
import sgtmelon.scriptum.presentation.provider.SummaryProvider
import java.util.*
import kotlin.random.Random

/**
 * Test for [AlarmPreferenceInteractor].
 */
@ExperimentalCoroutinesApi
class AlarmPreferenceInteractorTest : ParentInteractorTest() {

    //region Setup

    @MockK lateinit var summaryProvider: SummaryProvider
    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var intConverter: IntConverter

    private val interactor by lazy {
        AlarmPreferenceInteractor(summaryProvider, preferenceRepo, intConverter)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(summaryProvider, preferenceRepo)
    }

    //endregion

    @Test fun getRepeat() = FastTest.getRepeat(preferenceRepo) { interactor.repeat }

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

        every { preferenceRepo.repeat = value } returns Unit
        every { spyInteractor.getRepeatSummary() } returns null
        assertNull(spyInteractor.updateRepeat(value))

        every { spyInteractor.getRepeatSummary() } returns summary
        assertEquals(summary, spyInteractor.updateRepeat(value))

        verifySequence {
            repeat(times = 2) {
                spyInteractor.updateRepeat(value)
                preferenceRepo.repeat = value
                spyInteractor.getRepeatSummary()
            }
        }
    }


    @Test fun getSignalSummaryArray() {
        val summaryArray = Array(size = 3) { nextString() }
        val checkArray = booleanArrayOf(true, false, true)

        fun String.getLow() = toLowerCase(Locale.getDefault())
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
        val value = Random.nextInt()
        val summary = nextString()

        every { intConverter.toInt(valueArray) } returns value
        every { preferenceRepo.signal = value } returns Unit
        every { spyInteractor.getSignalSummary(valueArray) } returns null
        assertNull(spyInteractor.updateSignal(valueArray))

        every { spyInteractor.getSignalSummary(valueArray) } returns summary
        assertEquals(summary, spyInteractor.updateSignal(valueArray))

        verifySequence {
            repeat(times = 2) {
                spyInteractor.updateSignal(valueArray)
                intConverter.toInt(valueArray)
                preferenceRepo.signal = value
                spyInteractor.getSignalSummary(valueArray)
            }
        }
    }


    @Test fun getVolume() = FastTest.getVolume(preferenceRepo) { interactor.volume }

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

        every { preferenceRepo.volume = value } returns Unit
        every { spyInteractor.getVolumeSummary() } returns summary
        assertEquals(summary, spyInteractor.updateVolume(value))

        verifySequence {
            spyInteractor.updateVolume(value)
            preferenceRepo.volume = value
            spyInteractor.getVolumeSummary()
        }
    }

}