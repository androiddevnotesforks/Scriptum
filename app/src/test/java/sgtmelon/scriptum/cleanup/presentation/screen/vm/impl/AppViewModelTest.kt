package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.IAppActivity
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.parent.ParentViewModelTest

/**
 * Test for [AppViewModel].
 */
@ExperimentalCoroutinesApi
class AppViewModelTest : ParentViewModelTest() {

    //region Setup

    @MockK lateinit var callback: IAppActivity
    @MockK lateinit var preferencesRepo: PreferencesRepo

    private val viewModel by lazy { AppViewModel(callback, preferencesRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, preferencesRepo)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }

    //endregion

    @Test fun onSetup() {
        TODO()
//        every { interactor.theme } returns Theme.LIGHT
//        viewModel.onSetup()
//        assertEquals(Theme.LIGHT, viewModel.theme)
//
//        every { interactor.theme } returns Theme.DARK
//        viewModel.onSetup()
//        assertEquals(Theme.DARK, viewModel.theme)
//
//        verifySequence {
//            interactor.theme
//            callback.setupTheme(Theme.LIGHT)
//            callback.changeControlColor()
//            callback.changeSystemColor()
//
//            interactor.theme
//            callback.setupTheme(Theme.DARK)
//            callback.changeControlColor()
//            callback.changeSystemColor()
//        }
    }

    @Test fun isThemeChange() {
        TODO()
//        every { interactor.theme } returns Theme.UNDEFINED
//        assertFalse(viewModel.isThemeChange())
//
//        every { interactor.theme } returns Theme.LIGHT
//        viewModel.onSetup()
//        assertFalse(viewModel.isThemeChange())
//
//        every { interactor.theme } returns Theme.DARK
//        assertTrue(viewModel.isThemeChange())
//
//        verifySequence {
//            interactor.theme
//
//            interactor.theme
//            callback.setupTheme(Theme.LIGHT)
//            callback.changeControlColor()
//            callback.changeSystemColor()
//
//            interactor.theme
//            interactor.theme
//        }
    }
}