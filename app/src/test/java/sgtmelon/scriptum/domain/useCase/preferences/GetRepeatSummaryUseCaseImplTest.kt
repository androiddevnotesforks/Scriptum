package sgtmelon.scriptum.domain.useCase.preferences

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetRepeatSummaryUseCaseImpl
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.converter.key.RepeatConverter
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.test.common.nextString


/**
 * Test for [GetRepeatSummaryUseCaseImpl].
 */
class GetRepeatSummaryUseCaseImplTest : ParentEnumSummaryUseCaseTest<RepeatConverter>() {

    @MockK override lateinit var converter: RepeatConverter

    override val getSummary: GetSummaryUseCase by lazy {
        GetRepeatSummaryUseCaseImpl(summaryDataSource, preferencesRepo, converter)
    }

    @Test override fun `simple summary get`() {
        val repeat = mockk<Repeat>()
        val summary = nextString()

        every { preferencesRepo.repeat } returns repeat
        every { summaryDataSource.getRepeat(repeat) } returns summary

        assertEquals(getSummary(), summary)

        verifySequence {
            preferencesRepo.repeat
            summaryDataSource.getRepeat(repeat)
        }
    }

    @Test override fun `get summary with data set`() {
        val value = Random.nextInt()
        val repeat = mockk<Repeat>()
        val summary = nextString()

        every { converter.toEnum(value) } returns repeat
        every { spyGetSummary() } returns summary

        assertEquals(spyGetSummary(value), summary)

        verifySequence {
            spyGetSummary(value)
            converter.toEnum(value)
            preferencesRepo.repeat = repeat
            spyGetSummary()
        }
    }
}