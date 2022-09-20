package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.scriptum.cleanup.parent.ParentViewModelTest
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.IAppActivity
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.key.Theme

/**
 * Test for [ThemeViewModelImpl].
 */
@ExperimentalCoroutinesApi
class ThemeViewModelImplTest : ParentViewModelTest() {

    //region Setup

    @MockK lateinit var callback: IAppActivity
    @MockK lateinit var preferencesRepo: PreferencesRepo

    private val viewModel by lazy { ThemeViewModelImpl(callback, preferencesRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, preferencesRepo)
    }

    @Test override fun onDestroy() {
        every { preferencesRepo.theme } returns mockk()

        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)

        verifySequence {
            preferencesRepo.theme
        }
    }

    //endregion

    @Test fun onSetup() {
        val theme = mockk<Theme>()

        every { preferencesRepo.theme } returns theme
        viewModel.onSetup()

        verifySequence {
            preferencesRepo.theme
            callback.setupTheme(theme)
            callback.changeControlColor()
            callback.changeSystemColor()
        }
    }

    @Test fun isThemeChange() {
        val firstTheme = mockk<Theme>()
        val secondTheme = mockk<Theme>()

        every { preferencesRepo.theme } returns firstTheme
        assertFalse(viewModel.isThemeChange())

        every { preferencesRepo.theme } returns secondTheme
        assertTrue(viewModel.isThemeChange())

        verifySequence {
            preferencesRepo.theme
            preferencesRepo.theme

            preferencesRepo.theme
        }
    }
}