package sgtmelon.scriptum.domain.useCase.preferences

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetThemeSummaryUseCaseImpl
import sgtmelon.scriptum.infrastructure.converter.key.ThemeConverter
import sgtmelon.scriptum.infrastructure.model.key.Theme

/**
 * Test for [GetThemeSummaryUseCaseImpl].
 */
class GetThemeSummaryUseCaseImplTest : ParentEnumSummaryUseCaseTest<ThemeConverter>() {

    @MockK override lateinit var converter: ThemeConverter

    override val getSummary: GetSummaryUseCase by lazy {
        GetThemeSummaryUseCaseImpl(summaryProvider, preferencesRepo, converter)
    }

    @Test override fun `simple summary get`() {
        val theme = mockk<Theme>()
        val summary = nextString()

        every { preferencesRepo.theme } returns theme
        every { summaryProvider.getTheme(theme) } returns summary

        assertEquals(getSummary(), summary)

        verifySequence {
            preferencesRepo.theme
            summaryProvider.getTheme(theme)
        }
    }

    @Test override fun `get summary with data set`() {
        val value = Random.nextInt()
        val theme = mockk<Theme>()
        val summary = nextString()

        every { converter.toEnum(value) } returns theme
        every { spyGetSummary() } returns summary

        assertEquals(spyGetSummary(value), summary)

        verifySequence {
            spyGetSummary(value)
            converter.toEnum(value)
            preferencesRepo.theme = theme
            spyGetSummary()
        }
    }
}