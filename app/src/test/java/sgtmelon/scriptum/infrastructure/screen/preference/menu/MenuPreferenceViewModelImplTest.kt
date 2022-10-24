package sgtmelon.scriptum.infrastructure.screen.preference.menu

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.testing.getOrAwaitValue
import sgtmelon.scriptum.testing.parent.ParentLiveDataTest
import sgtmelon.test.common.nextString

/**
 * Test for [MenuPreferenceViewModelImpl].
 */
class MenuPreferenceViewModelImplTest : ParentLiveDataTest() {

    //region Setup

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var getSummary: GetSummaryUseCase

    private val startThemeSummary = nextString()
    private val startDeveloper = Random.nextBoolean()

    private val viewModel by lazy { MenuPreferenceViewModelImpl(preferencesRepo, getSummary) }

    @Before override fun setUp() {
        super.setUp()
        every { getSummary() } returns startThemeSummary
        every { preferencesRepo.isDeveloper } returns startDeveloper
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferencesRepo, getSummary)
    }

    //endregion

    private fun verifySetup() {
        getSummary()
        preferencesRepo.isDeveloper
    }

    @Test fun getTheme() {
        val value = mockk<Theme>()

        every { preferencesRepo.theme } returns value

        assertEquals(viewModel.theme, value)

        verifySequence {
            verifySetup()
            preferencesRepo.theme
        }
    }

    @Test fun getThemeSummary() {
        assertEquals(viewModel.themeSummary.value, startThemeSummary)

        verifySequence {
            verifySetup()
        }
    }

    @Test fun updateTheme() {
        val value = Random.nextInt()
        val summary = nextString()

        every { getSummary(value) } returns summary

        viewModel.updateTheme(value)

        assertEquals(viewModel.themeSummary.getOrAwaitValue(), summary)

        verifySequence {
            verifySetup()
            getSummary(value)
        }
    }

    @Test fun isDeveloper() {
        assertEquals(viewModel.isDeveloper.value, startDeveloper)

        verifySequence {
            verifySetup()
        }
    }

    @Test fun `unlockDeveloper already developer`() {
        every { preferencesRepo.isDeveloper } returns true

        viewModel.isDeveloper.postValue(false)
        assertFalse(viewModel.isDeveloper.getOrAwaitValue())
        viewModel.unlockDeveloper()
        assertFalse(viewModel.isDeveloper.getOrAwaitValue())

        verifySequence {
            verifySetup()
            preferencesRepo.isDeveloper
        }
    }

    @Test fun `unlockDeveloper become a developer`() {
        every { preferencesRepo.isDeveloper } returns false

        viewModel.isDeveloper.postValue(false)
        assertFalse(viewModel.isDeveloper.getOrAwaitValue())
        viewModel.unlockDeveloper()
        assertTrue(viewModel.isDeveloper.getOrAwaitValue())

        verifySequence {
            verifySetup()
            preferencesRepo.isDeveloper
            preferencesRepo.isDeveloper = true
        }
    }
}