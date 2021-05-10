package sgtmelon.scriptum.domain.interactor.impl.preference

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.parent.ParentInteractorTest
import sgtmelon.scriptum.presentation.provider.SummaryProvider
import kotlin.random.Random

/**
 * Test for [NotePreferenceInteractor].
 */
@ExperimentalCoroutinesApi
class NotePreferenceInteractorTest : ParentInteractorTest() {

    @MockK lateinit var summaryProvider: SummaryProvider
    @MockK lateinit var preferenceRepo: IPreferenceRepo

    private val interactor by lazy { NotePreferenceInteractor(summaryProvider, preferenceRepo) }
    private val spyInteractor by lazy { spyk(interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(summaryProvider, preferenceRepo)
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

}