package sgtmelon.scriptum.presentation.screen.vm.impl

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.IAppInteractor
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.ui.callback.IAppActivity

/**
 * Test for [AppViewModel].
 */
@ExperimentalCoroutinesApi
class AppViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: IAppActivity

    @MockK lateinit var interactor: IAppInteractor

    private val viewModel by lazy { AppViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }


    @Test fun onSetup() {
        every { interactor.theme } returns Theme.LIGHT
        viewModel.onSetup()

        every { interactor.theme } returns Theme.DARK
        viewModel.onSetup()

        verifySequence {
            interactor.theme
            callback.changeControlColor(true)
            callback.setTheme(R.style.App_Light_UI)

            interactor.theme
            callback.changeControlColor(false)
            callback.setTheme(R.style.App_Dark_UI)
        }
    }

    @Test fun isThemeChange() {
        every { interactor.theme } returns Theme.UNDEFINED
        assertFalse(viewModel.isThemeChange())

        every { interactor.theme } returns Theme.LIGHT
        viewModel.onSetup()
        assertFalse(viewModel.isThemeChange())

        every { interactor.theme } returns Theme.DARK
        assertTrue(viewModel.isThemeChange())
    }

}