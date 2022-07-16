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
 * Test for [AppInteractor].
 */
@ExperimentalCoroutinesApi
class AppInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferences: Preferences

    private val interactor by lazy { AppInteractor(preferences) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferences)
    }

    @Test fun getTheme() = FastTest.getTheme(preferences) { interactor.theme }

}