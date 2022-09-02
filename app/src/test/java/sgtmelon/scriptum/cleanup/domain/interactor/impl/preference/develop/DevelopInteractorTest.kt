package sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.develop

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.parent.ParentInteractorTest
import sgtmelon.scriptum.data.repository.database.DevelopRepo
import sgtmelon.scriptum.infrastructure.preferences.Preferences

/**
 * Test for [DevelopInteractor]
 */
@ExperimentalCoroutinesApi
class DevelopInteractorTest : ParentInteractorTest() {

    @MockK lateinit var developRepo: DevelopRepo
    @MockK lateinit var preferences: Preferences

    private val interactor by lazy { DevelopInteractor(developRepo, preferences) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(developRepo, preferences)
    }

    @Test fun getRandomNoteId() = startCoTest {
        val id = Random.nextLong()

        coEvery { developRepo.getRandomNoteId() } returns id

        assertEquals(id, interactor.getRandomNoteId())

        coVerifySequence {
            developRepo.getRandomNoteId()
        }
    }

    @Test fun resetPreferences() {
        interactor.resetPreferences()

        verifySequence {
            preferences.clear()
        }
    }
}