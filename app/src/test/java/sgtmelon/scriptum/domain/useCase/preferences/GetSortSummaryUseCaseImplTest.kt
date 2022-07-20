package sgtmelon.scriptum.domain.useCase.preferences

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test

import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.infrastructure.converter.key.SortConverter
import sgtmelon.scriptum.infrastructure.model.key.Sort

/**
 * Test for [GetSortSummaryUseCaseImpl].
 */
class GetSortSummaryUseCaseImplTest : ParentEnumSummaryUseCaseTest<SortConverter>() {

    @MockK override lateinit var converter: SortConverter

    override val getSummary: GetSummaryUseCase by lazy {
        GetSortSummaryUseCaseImpl(summaryProvider, preferencesRepo, converter)
    }

    @Test override fun `simple summary get`() {
        val sort = mockk<Sort>()
        val summary = nextString()

        every { preferencesRepo.sort } returns sort
        every { summaryProvider.getSort(sort) } returns summary

        assertEquals(getSummary(), summary)

        verifySequence {
            preferencesRepo.sort
            summaryProvider.getSort(sort)
        }
    }

    @Test override fun `get summary with data set`() {
        val value = Random.nextInt()
        val sort = mockk<Sort>()
        val summary = nextString()

        every { converter.toEnum(value) } returns sort
        every { spyGetSummary() } returns summary

        assertEquals(spyGetSummary(value), summary)

        verifySequence {
            spyGetSummary(value)
            converter.toEnum(value)
            preferencesRepo.sort = sort
            spyGetSummary()
        }
    }
}