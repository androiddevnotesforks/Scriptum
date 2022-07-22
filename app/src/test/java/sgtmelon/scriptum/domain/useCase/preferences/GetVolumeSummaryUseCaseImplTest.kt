package sgtmelon.scriptum.domain.useCase.preferences

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.parent.ParentTest

/**
 * Test for [GetVolumeSummaryUseCaseImpl].
 */
class GetVolumeSummaryUseCaseImplTest : ParentTest() {

    @MockK lateinit var summaryProvider: SummaryProvider
    @MockK lateinit var preferencesRepo: PreferencesRepo

    private val getSummary: GetSummaryUseCase by lazy {
        GetVolumeSummaryUseCaseImpl(summaryProvider, preferencesRepo)
    }
    private val spyGetSummary by lazy { spyk(getSummary) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(summaryProvider, preferencesRepo)
    }

    @Test fun `simple summary get`() {
        val volume = Random.nextInt()
        val summary = nextString()

        every { preferencesRepo.volume } returns volume
        every { summaryProvider.getVolume(volume) } returns summary

        assertEquals(getSummary(), summary)

        verifySequence {
            preferencesRepo.volume
            summaryProvider.getVolume(volume)
        }
    }

    @Test fun `get summary with data set`() {
        val value = Random.nextInt()
        val summary = nextString()

        every { spyGetSummary() } returns summary

        assertEquals(spyGetSummary(value), summary)

        verifySequence {
            spyGetSummary(value)
            preferencesRepo.volume = value
            spyGetSummary()
        }
    }
}