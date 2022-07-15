package sgtmelon.scriptum.cleanup.domain.interactor.impl

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.infrastructure.preferences.AppPreferences
import sgtmelon.scriptum.parent.ParentInteractorTest

/**
 * Test for [AppInteractor].
 */
@ExperimentalCoroutinesApi
class AppInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferenceRepo: AppPreferences

    private val interactor by lazy { AppInteractor(preferenceRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferenceRepo)
    }

    @Test fun getTheme() = FastTest.getTheme(preferenceRepo) { interactor.theme }

}