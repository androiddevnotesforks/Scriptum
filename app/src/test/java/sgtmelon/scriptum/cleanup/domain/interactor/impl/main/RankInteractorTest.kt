package sgtmelon.scriptum.cleanup.domain.interactor.impl.main

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.parent.ParentInteractorTest

/**
 * Test for [RankInteractor].
 */
@ExperimentalCoroutinesApi
class RankInteractorTest : ParentInteractorTest() {

    @MockK lateinit var rankRepo: RankRepo

    private val interactor by lazy { RankInteractor(rankRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(rankRepo)
    }


    @Test fun getCount() = startCoTest {
        val count = Random.nextInt()

        coEvery { rankRepo.getCount() } returns count
        assertEquals(count, interactor.getCount())

        coVerifySequence {
            rankRepo.getCount()
        }
    }

    @Test fun updatePosition() = startCoTest {
        val list = mockk<List<RankItem>>()
        val idList = mockk<List<Long>>()

        interactor.updatePositions(list, idList)

        coVerifySequence {
            rankRepo.updatePositions(list, idList)
        }
    }
}