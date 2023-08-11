package sgtmelon.scriptum.domain.useCase.preferences

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verifySequence
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetVolumeSummaryUseCase
import sgtmelon.test.common.nextString
import kotlin.random.Random

/**
 * Test for [GetVolumeSummaryUseCase].
 */
class GetVolumeSummaryUseCaseTest : sgtmelon.tests.uniter.ParentTest() {

    @MockK lateinit var summaryDataSource: SummaryDataSource
    @MockK lateinit var preferencesRepo: PreferencesRepo

    private val getSummary: GetSummaryUseCase by lazy {
        GetVolumeSummaryUseCase(summaryDataSource, preferencesRepo)
    }
    private val spyGetSummary by lazy { spyk(getSummary) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(summaryDataSource, preferencesRepo)
    }

    @Test fun `simple summary get`() {
        val volumePercent = Random.nextInt()
        val summary = nextString()

        every { preferencesRepo.volumePercent } returns volumePercent
        every { summaryDataSource.getVolume(volumePercent) } returns summary

        assertEquals(getSummary(), summary)

        verifySequence {
            preferencesRepo.volumePercent
            summaryDataSource.getVolume(volumePercent)
        }
    }

    @Test fun `get summary with data set`() {
        val value = Random.nextInt()
        val summary = nextString()

        every { spyGetSummary() } returns summary

        assertEquals(spyGetSummary(value), summary)

        verifySequence {
            spyGetSummary(value)
            preferencesRepo.volumePercent = value
            spyGetSummary()
        }
    }
}