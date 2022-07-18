package sgtmelon.scriptum.cleanup.domain.interactor.impl.preference

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.converter.ThemeConverter
import sgtmelon.scriptum.parent.ParentInteractorTest

/**
 * Test for [PreferenceInteractor]
 */
@ExperimentalCoroutinesApi
class PreferenceInteractorTest : ParentInteractorTest() {

    //region Setup

    @MockK lateinit var summaryProvider: SummaryProvider
    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var themeConverter: ThemeConverter

    private val interactor by lazy {
        PreferenceInteractor(summaryProvider, preferencesRepo, themeConverter)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(summaryProvider, preferencesRepo, themeConverter)
    }

    //endregion

    @Test fun getThemeSummary() {
        TODO()
//        val size = getRandomSize()
//        val valueArray = Array(size) { nextString() }
//        val index = valueArray.indices.random()
//        val value = valueArray[index]
//
//        every { summaryProvider.theme } returns valueArray
//        every { spyInteractor.theme } returns -1
//        assertNull(spyInteractor.getThemeSummary())
//
//        every { spyInteractor.theme } returns index
//        assertEquals(value, spyInteractor.getThemeSummary())
//
//        verifySequence {
//            repeat(times = 2) {
//                spyInteractor.getThemeSummary()
//                summaryProvider.theme
//                spyInteractor.theme
//            }
//        }
    }

    @Test fun updateTheme() {
        TODO()
//        val value = Random.nextInt()
//        val summary = nextString()
//
//        every { preferences.theme = value } returns Unit
//        every { spyInteractor.getThemeSummary() } returns null
//        assertNull(spyInteractor.updateTheme(value))
//
//        every { spyInteractor.getThemeSummary() } returns summary
//        assertEquals(summary, spyInteractor.updateTheme(value))
//
//        verifySequence {
//            repeat(times = 2) {
//                spyInteractor.updateTheme(value)
//                preferences.theme = value
//                spyInteractor.getThemeSummary()
//            }
//        }
    }
}