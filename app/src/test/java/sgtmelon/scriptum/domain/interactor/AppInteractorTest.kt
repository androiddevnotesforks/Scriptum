package sgtmelon.scriptum.domain.interactor

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.domain.model.annotation.Theme

/**
 * Test for [AppInteractor].
 */
@ExperimentalCoroutinesApi
class AppInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferenceRepo: IPreferenceRepo

    private val interactor by lazy { AppInteractor(preferenceRepo) }

    @Test fun getTheme() {
        every { preferenceRepo.theme } returns Theme.DARK
        assertEquals(Theme.DARK, interactor.theme)

        every { preferenceRepo.theme } returns Theme.LIGHT
        assertEquals(Theme.LIGHT, interactor.theme)

        verifySequence {
            interactor.theme
            interactor.theme
        }
    }

}