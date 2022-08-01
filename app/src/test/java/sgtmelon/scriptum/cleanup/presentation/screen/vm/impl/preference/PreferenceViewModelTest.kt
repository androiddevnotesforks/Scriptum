package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference

import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.parent.ParentViewModelTest
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.IPreferenceFragment
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.model.key.Theme

/**
 * Test for [PreferenceViewModel].
 */
@ExperimentalCoroutinesApi
class PreferenceViewModelTest : ParentViewModelTest() {

    //region Setup

    @MockK lateinit var callback: IPreferenceFragment
    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var getSummary: GetSummaryUseCase

    private val viewModel by lazy { PreferenceViewModel(callback, preferencesRepo, getSummary) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, preferencesRepo, getSummary)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }

    //endregion

    @Test fun onSetup() {
        val themeSummary = nextString()

        every { preferencesRepo.isDeveloper } returns false
        every { getSummary() } returns themeSummary
        viewModel.onSetup()

        every { preferencesRepo.isDeveloper } returns true
        viewModel.onSetup()

        coVerifySequence {
            callback.setupApp()
            callback.setupOther()
            preferencesRepo.isDeveloper
            getSummary()
            callback.updateThemeSummary(themeSummary)

            callback.setupApp()
            callback.setupOther()
            preferencesRepo.isDeveloper
            callback.setupDeveloper()
            getSummary()
            callback.updateThemeSummary(themeSummary)
        }
    }


    @Test fun onClickTheme() {
        val value = mockk<Theme>()

        every { preferencesRepo.theme } returns value

        viewModel.onClickTheme()

        verifySequence {
            preferencesRepo.theme
            callback.showThemeDialog(value)
        }
    }

    @Test fun onResultTheme() {
        val value = Random.nextInt()
        val summary = nextString()

        every { getSummary(value) } returns summary

        viewModel.onResultTheme(value)

        verifySequence {
            getSummary(value)
            callback.updateThemeSummary(summary)
        }
    }


    @Test fun onUnlockDeveloper() {
        every { preferencesRepo.isDeveloper } returns false
        viewModel.onUnlockDeveloper()

        every { preferencesRepo.isDeveloper } returns true
        viewModel.onUnlockDeveloper()

        verifySequence {
            preferencesRepo.isDeveloper
            preferencesRepo.isDeveloper = true
            callback.setupDeveloper()
            callback.showToast(R.string.pref_toast_develop_unlock)

            preferencesRepo.isDeveloper
            callback.showToast(R.string.pref_toast_develop_already)
        }
    }
}