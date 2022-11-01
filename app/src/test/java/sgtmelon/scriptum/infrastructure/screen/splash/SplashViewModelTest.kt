package sgtmelon.scriptum.infrastructure.screen.splash

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Test for [SplashViewModel].
 */
class SplashViewModelTest : ParentTest() {

    @MockK lateinit var preferencesRepo: PreferencesRepo

    private val viewModel by lazy { SplashViewModel(preferencesRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferencesRepo)
    }

    @Test fun isFirstStart() {
        val isFirstStart = Random.nextBoolean()

        every { preferencesRepo.isFirstStart } returns isFirstStart

        assertEquals(viewModel.isFirstStart, isFirstStart)

        verifySequence {
            preferencesRepo.isFirstStart
        }
    }

}