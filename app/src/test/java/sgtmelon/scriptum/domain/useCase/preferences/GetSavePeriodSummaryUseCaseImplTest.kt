package sgtmelon.scriptum.domain.useCase.preferences

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSavePeriodSummaryUseCaseImpl
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.converter.key.SavePeriodConverter
import sgtmelon.scriptum.infrastructure.model.key.SavePeriod

/**
 * Test for [GetSavePeriodSummaryUseCaseImpl]
 */
class GetSavePeriodSummaryUseCaseImplTest : ParentEnumSummaryUseCaseTest<SavePeriodConverter>() {

    @MockK override lateinit var converter: SavePeriodConverter

    override val getSummary: GetSummaryUseCase by lazy {
        GetSavePeriodSummaryUseCaseImpl(summaryProvider, preferencesRepo, converter)
    }

    @Test override fun `simple summary get`() {
        val savePeriod = mockk<SavePeriod>()
        val summary = nextString()

        every { preferencesRepo.savePeriod } returns savePeriod
        every { summaryProvider.getSavePeriod(savePeriod) } returns summary

        assertEquals(getSummary(), summary)

        verifySequence {
            preferencesRepo.savePeriod
            summaryProvider.getSavePeriod(savePeriod)
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