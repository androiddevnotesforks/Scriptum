package sgtmelon.scriptum.cleanup.domain.interactor.impl

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.parent.ParentInteractorTest

/**
 * Test for [IntroInteractor].
 */
@ExperimentalCoroutinesApi
class IntroInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferences: Preferences

    private val interactor by lazy { IntroInteractor(preferences) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferences)
    }

    @Test fun onIntroFinish() {
        interactor.onIntroFinish()

        verifySequence {
            preferences.isFirstStart = false
        }
    }
}