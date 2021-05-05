package sgtmelon.scriptum.presentation.screen.vm.impl

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.domain.interactor.callback.IAppInteractor
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.parent.ParentViewModelTest
import sgtmelon.scriptum.presentation.screen.ui.callback.IAppActivity

/**
 * Test for [AppViewModel].
 */
@ExperimentalCoroutinesApi
class AppViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: IAppActivity

    @MockK lateinit var interactor: IAppInteractor

    private val viewModel by lazy { AppViewModel(application) }

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


    @Test fun onSetup() {
        every { interactor.theme } returns Theme.LIGHT
        viewModel.onSetup()
        assertEquals(Theme.LIGHT, viewModel.theme)

        every { interactor.theme } returns Theme.DARK
        viewModel.onSetup()
        assertEquals(Theme.DARK, viewModel.theme)

        verifySequence {
            interactor.theme
            callback.setupTheme(Theme.LIGHT)
            callback.changeControlColor()
            callback.changeSystemColor()

            interactor.theme
            callback.setupTheme(Theme.DARK)
            callback.changeControlColor()
            callback.changeSystemColor()
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

        verifySequence {
            interactor.theme

            interactor.theme
            callback.setupTheme(Theme.LIGHT)
            callback.changeControlColor()
            callback.changeSystemColor()

            interactor.theme
            interactor.theme
        }
    }
}