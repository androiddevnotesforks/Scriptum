package sgtmelon.scriptum.cleanup.domain.interactor.impl.preference

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.parent.ParentInteractorTest

/**
 * Test for [NotePreferenceInteractor].
 */
@ExperimentalCoroutinesApi
class NotePreferenceInteractorTest : ParentInteractorTest() {

    @MockK lateinit var summaryProvider: SummaryProvider
    @MockK lateinit var preferences: Preferences

    private val interactor by lazy { NotePreferenceInteractor(summaryProvider, preferences) }
    private val spyInteractor by lazy { spyk(interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(summaryProvider, preferences)
    }

    @Test fun getSavePeriod() {
        val value = Random.nextInt()

        every { preferences.savePeriod } returns value
        assertEquals(value, interactor.savePeriod)

        verifySequence {
            preferences.savePeriod
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

        every { preferences.savePeriod = value } returns Unit
        every { spyInteractor.getSavePeriodSummary() } returns null
        assertNull(spyInteractor.updateSavePeriod(value))

        every { spyInteractor.getSavePeriodSummary() } returns summary
        assertEquals(summary, spyInteractor.updateSavePeriod(value))

        verifySequence {
            repeat(times = 2) {
                spyInteractor.updateSavePeriod(value)
                preferences.savePeriod = value
                spyInteractor.getSavePeriodSummary()
            }
        }
    }

}