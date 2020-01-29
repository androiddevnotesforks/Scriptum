package sgtmelon.scriptum.screen.vm

import android.os.Bundle
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkClass
import io.mockk.verify
import org.junit.Test

import org.junit.Assert.*
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.R
import sgtmelon.scriptum.interactor.callback.IAppInteractor
import sgtmelon.scriptum.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.callback.IAppActivity
import sgtmelon.scriptum.screen.ui.callback.ISplashActivity

/**
 * Test for [AppViewModel]
 */
class AppViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: IAppActivity

    @MockK lateinit var interactor: IAppInteractor

    private val viewModel by lazy { AppViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        assertEquals(callback, viewModel.callback)

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
        verify(exactly = 1) { callback.setTheme(R.style.App_Light_UI) }

        every { interactor.theme } returns Theme.DARK

        viewModel.onSetup()
        verify(exactly = 1) { callback.setTheme(R.style.App_Dark_UI) }
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