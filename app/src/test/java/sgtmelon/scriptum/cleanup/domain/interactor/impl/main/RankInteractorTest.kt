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
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.parent.ParentInteractorTest
import sgtmelon.test.common.nextString

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

    @Test fun getList() = startCoTest {
        val list = mockk<MutableList<RankItem>>()

        coEvery { rankRepo.getList() } returns list
        assertEquals(list, interactor.getList())

        coVerifySequence {
            rankRepo.getList()
        }
    }


    @Test fun insert_byName() = startCoTest {
        val id = Random.nextLong()
        val name = nextString()
        val item = RankItem(id, name = name)

        coEvery { rankRepo.insert(name) } returns null
        assertNull(interactor.insert(name))

        coEvery { rankRepo.insert(name) } returns id
        assertEquals(item, interactor.insert(name))

        coVerifySequence {
            rankRepo.insert(name)
            rankRepo.insert(name)
        }
    }

    @Test fun insert_byItem() = startCoTest {
        val item = mockk<RankItem>()

        interactor.insert(item)

        coVerifySequence {
            rankRepo.insert(item)
        }
    }

    @Test fun delete() = startCoTest {
        val item = mockk<RankItem>()

        interactor.delete(item)

        coVerifySequence {
            rankRepo.delete(item)
        }
    }

    @Test fun updateItem() = startCoTest {
        val item = mockk<RankItem>()

        interactor.update(item)

        coVerifySequence {
            rankRepo.update(item)
        }
    }

    @Test fun updateList() = startCoTest {
        val list = mockk<List<RankItem>>()

        interactor.update(list)

        coVerifySequence {
            rankRepo.update(list)
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