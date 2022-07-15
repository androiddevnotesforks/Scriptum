package sgtmelon.scriptum.cleanup.domain.interactor.impl

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.infrastructure.preferences.IPreferenceRepo
import sgtmelon.scriptum.parent.ParentInteractorTest

/**
 * Test for [IntroInteractor].
 */
@ExperimentalCoroutinesApi
class IntroInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferenceRepo: IPreferenceRepo

    private val interactor by lazy { IntroInteractor(preferenceRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferenceRepo)
    }

    @Test fun onIntroFinish() {
        interactor.onIntroFinish()

        verifySequence {
            preferenceRepo.firstStart = false
        }
    }
}