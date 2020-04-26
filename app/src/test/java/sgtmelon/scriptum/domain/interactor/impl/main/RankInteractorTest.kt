package sgtmelon.scriptum.domain.interactor.impl.main

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.model.item.RankItem
import kotlin.random.Random

/**
 * Test for [RankInteractor].
 */
@ExperimentalCoroutinesApi
class RankInteractorTest : ParentInteractorTest() {

    private val data = TestData.Rank

    @MockK lateinit var rankRepo: IRankRepo

    private val interactor by lazy { RankInteractor(rankRepo) }

    @Test fun getCount() = startCoTest {
        val count = Random.nextInt()

        coEvery { rankRepo.getCount() } returns count

        assertEquals(count, interactor.getCount())

        coVerifySequence {
            rankRepo.getCount()
        }
    }

    @Test fun getList() = startCoTest {
        val list = data.itemList

        coEvery { rankRepo.getList() } returns list

        assertEquals(list, interactor.getList())

        coVerifySequence {
            rankRepo.getList()
        }
    }

    @Test fun getBind() = startCoTest {
        val noteId = List(size = 5) { Random.nextLong() }
        val hasBind = Random.nextBoolean()

        coEvery { rankRepo.getBind(noteId) } returns hasBind

        assertEquals(hasBind, interactor.getBind(noteId))

        coVerifySequence {
            rankRepo.getBind(noteId)
        }
    }


    @Test fun insert() = startCoTest {
        val id = Random.nextLong()
        val name = TestData.uniqueString
        val item = RankItem(id, name = name)

        coEvery { rankRepo.insert(name) } returns id

        assertEquals(item, interactor.insert(name))

        coVerifySequence {
            rankRepo.insert(name)
        }
    }

    @Test fun delete() = startCoTest {
        val item = data.itemList.random()

        interactor.delete(item)

        coVerifySequence {
            rankRepo.delete(item)
        }
    }

    @Test fun updateItem() = startCoTest {
        val item = data.itemList.random()

        interactor.update(item)

        coVerifySequence {
            rankRepo.update(item)
        }
    }

    @Test fun updateList() = startCoTest {
        val list = data.itemList

        interactor.update(list)

        coVerifySequence {
            rankRepo.update(list)
        }
    }

    @Test fun updatePosition() = startCoTest {
        val list = data.itemList
        val noteIdList = ArrayList<Long>().apply { repeat(times = 10) { add(Random.nextLong()) } }

        interactor.updatePosition(list, noteIdList)

        coVerifySequence {
            rankRepo.updatePosition(list, noteIdList)
        }
    }

}