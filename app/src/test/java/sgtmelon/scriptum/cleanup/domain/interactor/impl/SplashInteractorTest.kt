package sgtmelon.scriptum.cleanup.domain.interactor.impl

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.parent.ParentInteractorTest

/**
 * Test for [SplashInteractor].
 */
@ExperimentalCoroutinesApi
class SplashInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferences: Preferences

    private val interactor by lazy { SplashInteractor(preferences) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferences)
    }

    @Test fun getFirstStart() = FastTest.getFirstStart(preferences) {
        preferences.isFirstStart
    }
}