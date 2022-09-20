package sgtmelon.scriptum.infrastructure.screen.theme

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.key.Theme

/**
 * Test for [ThemeViewModelImpl].
 */
class ThemeViewModelImplTest : ParentTest() {

    //region Setup


    @MockK lateinit var preferencesRepo: PreferencesRepo

    private val viewModel by lazy { ThemeViewModelImpl(preferencesRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferencesRepo)
    }

    //endregion

    @Test fun init() {
        val initTheme = mockk<Theme>()

        every { preferencesRepo.theme } returns initTheme

        assertEquals(viewModel.theme, initTheme)

        verifySequence {
            preferencesRepo.theme
        }
    }

    @Test fun isThemeChange() {
        val firstTheme = mockk<Theme>()
        val secondTheme = mockk<Theme>()

        every { preferencesRepo.theme } returns firstTheme

        assertEquals(viewModel.theme, firstTheme)
        assertFalse(viewModel.isThemeChanged())

        every { preferencesRepo.theme } returns secondTheme

        assertEquals(viewModel.theme, firstTheme)
        assertTrue(viewModel.isThemeChanged())
        assertEquals(viewModel.theme, secondTheme)

        verifySequence {
            preferencesRepo.theme
            preferencesRepo.theme
            preferencesRepo.theme
        }
    }
}