package sgtmelon.scriptum.domain.interactor.impl.main

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.extension.nextString
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
        val countList = listOf(Random.nextInt(), Random.nextInt())

        countList.forEach {
            coEvery { rankRepo.getCount() } returns it
            assertEquals(it, interactor.getCount())
        }

        coVerifySequence {
            repeat(countList.size) { rankRepo.getCount() }
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
        val bindList = listOf(Random.nextBoolean(), Random.nextBoolean())
        val noteIdList = List(size = 5) { Random.nextLong() }

        bindList.forEach {
            coEvery { rankRepo.getBind(noteIdList) } returns it
            assertEquals(it, interactor.getBind(noteIdList))
        }

        coVerifySequence {
            repeat(bindList.size) { rankRepo.getBind(noteIdList) }
        }
    }


    @Test fun insert() = startCoTest {
        val idList = listOf(Random.nextLong(),  Random.nextLong())
        val name = Random.nextString()

        idList.forEach {
            coEvery { rankRepo.insert(name) } returns it
            assertEquals(RankItem(it, name = name), interactor.insert(name))
        }

        coVerifySequence {
            repeat(idList.size) { rankRepo.insert(name) }
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