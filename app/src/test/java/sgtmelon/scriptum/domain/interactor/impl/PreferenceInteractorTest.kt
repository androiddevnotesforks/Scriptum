package sgtmelon.scriptum.domain.interactor.impl

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.room.converter.type.IntConverter
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.presentation.provider.SummaryProvider
import java.util.*
import kotlin.random.Random

/**
 * Test for [PreferenceInteractor]
 */
@ExperimentalCoroutinesApi
class PreferenceInteractorTest : ParentInteractorTest() {

    @MockK lateinit var summaryProvider: SummaryProvider
    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var intConverter: IntConverter

    private val interactor by lazy {
        PreferenceInteractor(summaryProvider, preferenceRepo, intConverter)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    override fun tearDown() {
        super.tearDown()
        confirmVerified(summaryProvider, preferenceRepo, intConverter)
    }

    @Test fun getTheme() = FastTest.getTheme(preferenceRepo) { interactor.theme }

    @Test fun getThemeSummary() {
        val size = getRandomSize()
        val valueArray = Array(size) { nextString() }
        val index = valueArray.indices.random()
        val value = valueArray[index]

        every { summaryProvider.theme } returns valueArray
        every { spyInteractor.theme } returns -1
        assertNull(spyInteractor.getThemeSummary())

        every { spyInteractor.theme } returns index
        assertEquals(value, spyInteractor.getThemeSummary())

        verifySequence {
            repeat(times = 2) {
                spyInteractor.getThemeSummary()
                summaryProvider.theme
                spyInteractor.theme
            }
        }
    }

    @Test fun updateTheme() {
        val value = Random.nextInt()
        val summary = nextString()

        every { preferenceRepo.theme = value } returns Unit
        every { spyInteractor.getThemeSummary() } returns null
        assertNull(spyInteractor.updateTheme(value))

        every { spyInteractor.getThemeSummary() } returns summary
        assertEquals(summary, spyInteractor.updateTheme(value))

        verifySequence {
            repeat(times = 2) {
                spyInteractor.updateTheme(value)
                preferenceRepo.theme = value
                spyInteractor.getThemeSummary()
            }
        }
    }


    @Test fun getSort() = FastTest.getSort(preferenceRepo) { interactor.sort }

    @Test fun getSortSummary() {
        val size = getRandomSize()
        val valueArray = Array(size) { nextString() }
        val index = valueArray.indices.random()
        val value = valueArray[index]

        every { summaryProvider.sort } returns valueArray
        every { spyInteractor.sort } returns -1
        assertNull(spyInteractor.getSortSummary())

        every { spyInteractor.sort } returns index
        assertEquals(value, spyInteractor.getSortSummary())

        verifySequence {
            repeat(times = 2) {
                spyInteractor.getSortSummary()
                summaryProvider.sort
                spyInteractor.sort
            }
        }
    }

    @Test fun updateSort() {
        val value = Random.nextInt()
        val summary = nextString()

        every { preferenceRepo.sort = value } returns Unit
        every { spyInteractor.getSortSummary() } returns null
        assertNull(spyInteractor.updateSort(value))

        every { spyInteractor.getSortSummary() } returns summary
        assertEquals(summary, spyInteractor.updateSort(value))

        verifySequence {
            repeat(times = 2) {
                spyInteractor.updateSort(value)
                preferenceRepo.sort = value
                spyInteractor.getSortSummary()
            }
        }
    }


    @Test fun getDefaultColor() = FastTest.getDefaultColor(preferenceRepo) {
        interactor.defaultColor
    }

    @Test fun getDefaultColorSummary() {
        val size = getRandomSize()
        val valueArray = Array(size) { nextString() }
        val index = valueArray.indices.random()
        val value = valueArray[index]

        every { summaryProvider.color } returns valueArray
        every { spyInteractor.defaultColor } returns -1
        assertNull(spyInteractor.getDefaultColorSummary())

        every { spyInteractor.defaultColor } returns index
        assertEquals(value, spyInteractor.getDefaultColorSummary())

        verifySequence {
            repeat(times = 2) {
                spyInteractor.getDefaultColorSummary()
                summaryProvider.color
                spyInteractor.defaultColor
            }
        }
    }

    @Test fun updateDefaultColor() {
        val value = Random.nextInt()
        val summary = nextString()

        every { preferenceRepo.defaultColor = value } returns Unit
        every { spyInteractor.getDefaultColorSummary() } returns null
        assertNull(spyInteractor.updateDefaultColor(value))

        every { spyInteractor.getDefaultColorSummary() } returns summary
        assertEquals(summary, spyInteractor.updateDefaultColor(value))

        verifySequence {
            repeat(times = 2) {
                spyInteractor.updateDefaultColor(value)
                preferenceRepo.defaultColor = value
                spyInteractor.getDefaultColorSummary()
            }
        }
    }


    @Test fun getSavePeriod() {
        val value = Random.nextInt()

        every { preferenceRepo.savePeriod } returns value
        assertEquals(value, interactor.savePeriod)

        verifySequence {
            preferenceRepo.savePeriod
        }
    }

    @Test fun getSavePeriodSummary() {
        val size = getRandomSize()
        val valueArray = Array(size) { nextString() }
        val index = valueArray.indices.random()
        val value = valueArray[index]

        every { summaryProvider.savePeriod } returns valueArray
        every { spyInteractor.savePeriod } returns -1
        assertNull(spyInteractor.getSavePeriodSummary())

        every { spyInteractor.savePeriod } returns index
        assertEquals(value, spyInteractor.getSavePeriodSummary())

        verifySequence {
            repeat(times = 2) {
                spyInteractor.getSavePeriodSummary()
                summaryProvider.savePeriod
                spyInteractor.savePeriod
            }
        }
    }

    @Test fun updateSavePeriod() {
        val value = Random.nextInt()
        val summary = nextString()

        every { preferenceRepo.savePeriod = value } returns Unit
        every { spyInteractor.getSavePeriodSummary() } returns null
        assertNull(spyInteractor.updateSavePeriod(value))

        every { spyInteractor.getSavePeriodSummary() } returns summary
        assertEquals(summary, spyInteractor.updateSavePeriod(value))

        verifySequence {
            repeat(times = 2) {
                spyInteractor.updateSavePeriod(value)
                preferenceRepo.savePeriod = value
                spyInteractor.getSavePeriodSummary()
            }
        }
    }


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