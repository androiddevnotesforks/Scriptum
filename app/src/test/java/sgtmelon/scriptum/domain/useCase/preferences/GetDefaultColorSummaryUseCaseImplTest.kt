package sgtmelon.scriptum.domain.useCase.preferences

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.model.key.Color

/**
 * Test for [GetDefaultColorSummaryUseCaseImpl].
 */
class GetDefaultColorSummaryUseCaseImplTest : ParentEnumSummaryUseCaseTest<ColorConverter>() {

    @MockK override lateinit var converter: ColorConverter

    override val getSummary: GetSummaryUseCase by lazy {
        GetDefaultColorSummaryUseCaseImpl(summaryProvider, preferencesRepo, converter)
    }

    @Test override fun `simple summary get`() {
        val color = mockk<Color>()
        val summary = nextString()

        every { preferencesRepo.defaultColor } returns color
        every { summaryProvider.getColor(color) } returns summary

        assertEquals(getSummary(), summary)

        verifySequence {
            preferencesRepo.defaultColor
            summaryProvider.getColor(color)
        }
    }

    @Test override fun `get summary with data set`() {
        val value = Random.nextInt()
        val color = mockk<Color>()
        val summary = nextString()

        every { converter.toEnum(value) } returns color
        every { spyGetSummary() } returns summary

        assertEquals(spyGetSummary(value), summary)

        verifySequence {
            spyGetSummary(value)
            converter.toEnum(value)
            preferencesRepo.defaultColor = color
            spyGetSummary()
        }
    }
}