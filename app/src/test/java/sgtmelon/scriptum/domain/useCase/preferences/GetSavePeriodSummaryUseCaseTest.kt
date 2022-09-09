package sgtmelon.scriptum.domain.useCase.preferences

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSavePeriodSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.converter.key.SavePeriodConverter
import sgtmelon.scriptum.infrastructure.model.key.SavePeriod
import sgtmelon.test.common.nextString

/**
 * Test for [GetSavePeriodSummaryUseCase]
 */
class GetSavePeriodSummaryUseCaseTest : ParentEnumSummaryUseCaseTest<SavePeriodConverter>() {

    @MockK override lateinit var converter: SavePeriodConverter

    override val getSummary: GetSummaryUseCase by lazy {
        GetSavePeriodSummaryUseCase(summaryDataSource, preferencesRepo, converter)
    }

    @Test override fun `simple summary get`() {
        val savePeriod = mockk<SavePeriod>()
        val summary = nextString()

        every { preferencesRepo.savePeriod } returns savePeriod
        every { summaryDataSource.getSavePeriod(savePeriod) } returns summary

        assertEquals(getSummary(), summary)

        verifySequence {
            preferencesRepo.savePeriod
            summaryDataSource.getSavePeriod(savePeriod)
        }
    }

    @Test override fun `get summary with data set`() {
        val value = Random.nextInt()
        val savePeriod = mockk<SavePeriod>()
        val summary = nextString()

        every { converter.toEnum(value) } returns savePeriod
        every { spyGetSummary() } returns summary

        assertEquals(spyGetSummary(value), summary)

        verifySequence {
            spyGetSummary(value)
            converter.toEnum(value)
            preferencesRepo.savePeriod = savePeriod
            spyGetSummary()
        }
    }
}