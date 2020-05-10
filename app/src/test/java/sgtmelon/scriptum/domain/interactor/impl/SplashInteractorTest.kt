package sgtmelon.scriptum.domain.interactor.impl

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
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
        TODO("nullable")

        every { preferenceRepo.firstStart } returns null
        assertEquals(null, interactor.firstStart)

        every { preferenceRepo.firstStart } returns true
        assertEquals(true, interactor.firstStart)

        every { preferenceRepo.firstStart } returns false
        assertEquals(false, interactor.firstStart)

        verifySequence {
            interactor.firstStart
            interactor.firstStart
            interactor.firstStart
        }
    }

}