package sgtmelon.scriptum.domain.interactor.impl.preference.develop

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IDevelopRepo
import sgtmelon.scriptum.parent.ParentInteractorTest
import kotlin.random.Random

/**
 * Test for [DevelopInteractor]
 */
@ExperimentalCoroutinesApi
class DevelopInteractorTest : ParentInteractorTest() {

    @MockK lateinit var developRepo: IDevelopRepo
    @MockK lateinit var preferenceRepo: IPreferenceRepo

    private val interactor by lazy { DevelopInteractor(developRepo, preferenceRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(developRepo, preferenceRepo)
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
            preferenceRepo.clear()
        }
    }
}