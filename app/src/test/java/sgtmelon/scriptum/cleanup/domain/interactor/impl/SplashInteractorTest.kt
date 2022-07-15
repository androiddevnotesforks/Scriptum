package sgtmelon.scriptum.cleanup.domain.interactor.impl

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.infrastructure.preferences.IPreferenceRepo
import sgtmelon.scriptum.parent.ParentInteractorTest

/**
 * Test for [SplashInteractor].
 */
@ExperimentalCoroutinesApi
class SplashInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferenceRepo: IPreferenceRepo

    private val interactor by lazy { SplashInteractor(preferenceRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferenceRepo)
    }

    @Test fun getFirstStart() = FastTest.getFirstStart(preferenceRepo) {
        preferenceRepo.firstStart
    }
}