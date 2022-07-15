package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference

import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IPreferenceInteractor
import sgtmelon.scriptum.parent.ParentViewModelTest
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.IPreferenceFragment
import kotlin.random.Random

/**
 * Test for [PreferenceViewModel].
 */
@ExperimentalCoroutinesApi
class PreferenceViewModelTest : ParentViewModelTest() {

    //region Setup

    @MockK lateinit var interactor: IPreferenceInteractor
    @MockK lateinit var callback: IPreferenceFragment

    private val viewModel by lazy { PreferenceViewModel(application) }

    @Before override fun setup() {
        super.setup()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor)
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }

    //endregion

    @Test fun onSetup() {
        val themeSummary = nextString()

        every { interactor.isDeveloper } returns false
        every { interactor.getThemeSummary() } returns themeSummary
        viewModel.onSetup()

        every { interactor.isDeveloper } returns true
        viewModel.onSetup()

        coVerifySequence {
            callback.setupApp()
            callback.setupOther()
            interactor.isDeveloper
            interactor.getThemeSummary()
            callback.updateThemeSummary(themeSummary)

            callback.setupApp()
            callback.setupOther()
            interactor.isDeveloper
            callback.setupDeveloper()
            interactor.getThemeSummary()
            callback.updateThemeSummary(themeSummary)
        }
    }


    @Test fun onClickTheme() {
        val value = Random.nextInt()

        every { interactor.theme } returns value

        viewModel.onClickTheme()

        verifySequence {
            interactor.theme
            callback.showThemeDialog(value)
        }
    }

    @Test fun onResultTheme() {
        val value = Random.nextInt()
        val summary = nextString()

        every { interactor.updateTheme(value) } returns summary

        viewModel.onResultTheme(value)

        verifySequence {
            interactor.updateTheme(value)
            callback.updateThemeSummary(summary)
        }
    }


    @Test fun onUnlockDeveloper() {
        every { interactor.isDeveloper } returns false
        every { interactor.isDeveloper = true } returns Unit
        viewModel.onUnlockDeveloper()

        every { interactor.isDeveloper } returns true
        viewModel.onUnlockDeveloper()

        verifySequence {
            interactor.isDeveloper
            interactor.isDeveloper = true
            callback.setupDeveloper()
            callback.showToast(R.string.pref_toast_develop_unlock)

            interactor.isDeveloper
            callback.showToast(R.string.pref_toast_develop_already)
        }
    }
}