package sgtmelon.scriptum.domain.interactor

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo

/**
 * Test for [SplashInteractor].
 */
@ExperimentalCoroutinesApi
class SplashInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferenceRepo: IPreferenceRepo

    private val interactor by lazy { SplashInteractor(preferenceRepo) }

    @Test fun getFirstStart() {
        every { preferenceRepo.firstStart } returns true
        assertTrue(interactor.firstStart)

        every { preferenceRepo.firstStart } returns false
        assertFalse(interactor.firstStart)

        verifySequence {
            interactor.firstStart
            interactor.firstStart
        }
    }

}