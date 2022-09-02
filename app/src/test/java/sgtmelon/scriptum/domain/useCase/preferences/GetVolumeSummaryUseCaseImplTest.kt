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
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetVolumeSummaryUseCaseImpl
import sgtmelon.scriptum.infrastructure.provider.SummaryDataSource
import sgtmelon.test.common.nextString

/**
 * Test for [GetVolumeSummaryUseCaseImpl].
 */
class GetVolumeSummaryUseCaseImplTest : ParentTest() {

    @MockK lateinit var summaryDataSource: SummaryDataSource
    @MockK lateinit var preferencesRepo: PreferencesRepo

    private val getSummary: GetSummaryUseCase by lazy {
        GetVolumeSummaryUseCaseImpl(summaryDataSource, preferencesRepo)
    }
    private val spyGetSummary by lazy { spyk(getSummary) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(summaryDataSource, preferencesRepo)
    }

    @Test fun `simple summary get`() {
        val volume = Random.nextInt()
        val summary = nextString()

        every { preferencesRepo.volume } returns volume
        every { summaryDataSource.getVolume(volume) } returns summary

        assertEquals(getSummary(), summary)

        verifySequence {
            preferencesRepo.volume
            summaryDataSource.getVolume(volume)
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