package sgtmelon.scriptum.data.repository.room

import io.mockk.coEvery
import io.mockk.coVerifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentRoomRepoTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.data.room.converter.model.RankConverter
import sgtmelon.scriptum.data.room.converter.model.RollConverter
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RollEntity
import sgtmelon.scriptum.domain.model.key.NoteType

/**
 * Test for [DevelopRepo].
 */
@ExperimentalCoroutinesApi
class DevelopRepoTest : ParentRoomRepoTest() {

    private val developRepo by lazy { DevelopRepo(roomProvider) }

    @Test fun getNoteList() = startCoTest {
        val entityList = NoteConverter().toEntity(TestData.Note.itemList)
        val finalList = ArrayList<NoteEntity>().apply {
            addAll(entityList)
            addAll(entityList.reversed())
        }

        coEvery { noteDao.getByChange(bin = false) } returns entityList
        coEvery { noteDao.getByChange(bin = true) } returns entityList.reversed()

        assertEquals(finalList, developRepo.getNoteList())

        coVerifySequence {
            roomProvider.openRoom()
            noteDao.getByChange(bin = false)
            noteDao.getByChange(bin = true)
        }
    }

    @Test fun getRollList() = startCoTest {
        val entityList = NoteConverter().toEntity(TestData.Note.itemList)

        val firstList = entityList.filter { it.type == NoteType.ROLL }.map { it.id }
        val secondList = entityList.reversed().filter { it.type == NoteType.ROLL }.map { it.id }

        val rollList = TestData.Note.rollList
        val resultList = ArrayList<RollEntity>().apply {
            with(RollConverter()) {
                addAll(toEntity(firstList.first(), rollList))
                addAll(toEntity(firstList.last(), rollList))
                addAll(toEntity(secondList.first(), rollList))
                addAll(toEntity(secondList.last(), rollList))
            }
        }

        coEvery { noteDao.getByChange(bin = false) } returns entityList
        coEvery { noteDao.getByChange(bin = true) } returns entityList.reversed()
        firstList.forEach {
            coEvery { rollDao.get(it) } returns RollConverter().toEntity(it, rollList)
        }
        secondList.forEach {
            coEvery { rollDao.get(it) } returns RollConverter().toEntity(it, rollList)
        }

        assertEquals(resultList, developRepo.getRollList())

        coVerifySequence {
            roomProvider.openRoom()
            noteDao.getByChange(bin = false)
            firstList.forEach { rollDao.get(it) }
            noteDao.getByChange(bin = true)
            secondList.forEach { rollDao.get(it) }
        }
    }

    @Test fun getRankList() = startCoTest {
        val entityList = RankConverter().toEntity(TestData.Rank.itemList)

        coEvery { rankDao.get() } returns entityList

        assertEquals(entityList, developRepo.getRankList())

        coVerifySequence {
            roomProvider.openRoom()
            rankDao.get()
        }
    }

}