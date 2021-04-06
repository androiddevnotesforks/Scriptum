package sgtmelon.scriptum.data.repository.room

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentRoomRepoTest
import sgtmelon.scriptum.data.room.entity.*
import sgtmelon.scriptum.domain.model.item.PrintItem
import sgtmelon.scriptum.getRandomSize
import kotlin.random.Random

/**
 * Test for [DevelopRepo].
 */
@ExperimentalCoroutinesApi
class DevelopRepoTest : ParentRoomRepoTest() {

    private val developRepo by lazy { DevelopRepo(roomProvider) }
    private val spyDevelopRepo by lazy { spyk(developRepo) }

    @Test fun getPrintNoteList() = startCoTest {
        val isBin = Random.nextBoolean()
        val list = List(getRandomSize()) { mockk<NoteEntity>() }
        val resultList = list.map { PrintItem.Note(it) }

        coEvery { noteDao.get(isBin) } returns list

        assertEquals(resultList, developRepo.getPrintNoteList(isBin))

        coVerifySequence {
            roomProvider.openRoom()
            noteDao.get(isBin)
        }
    }

    @Test fun getPrintRollList() = startCoTest {
        val list = List(getRandomSize()) { mockk<RollEntity>() }
        val resultList = list.map { PrintItem.Roll(it) }

        coEvery { rollDao.get() } returns list

        assertEquals(resultList, developRepo.getPrintRollList())

        coVerifySequence {
            roomProvider.openRoom()
            rollDao.get()
        }
    }

    @Test fun getPrintVisibleList() = startCoTest {
        val list = List(getRandomSize()) { mockk<RollVisibleEntity>() }
        val resultList = list.map { PrintItem.Visible(it) }

        coEvery { rollVisibleDao.get() } returns list

        assertEquals(resultList, developRepo.getPrintVisibleList())

        coVerifySequence {
            roomProvider.openRoom()
            rollVisibleDao.get()
        }
    }

    @Test fun getPrintRankList() = startCoTest {
        val list = List(getRandomSize()) { mockk<RankEntity>() }
        val resultList = list.map { PrintItem.Rank(it) }

        coEvery { rankDao.get() } returns list

        assertEquals(resultList, developRepo.getPrintRankList())

        coVerifySequence {
            roomProvider.openRoom()
            rankDao.get()
        }
    }

    @Test fun getPrintAlarmList() = startCoTest {
        val list = List(getRandomSize()) { mockk<AlarmEntity>() }
        val resultList = list.map { PrintItem.Alarm(it) }

        coEvery { alarmDao.get() } returns list

        assertEquals(resultList, developRepo.getPrintAlarmList())

        coVerifySequence {
            roomProvider.openRoom()
            alarmDao.get()
        }
    }

    @Test fun getRandomNoteId() = startCoTest {
        val entityList = List(getRandomSize()) { mockk<NoteEntity>() }
        val id = Random.nextLong()

        for (entity in entityList) {
            every { entity.id } returns id
        }

        coEvery { noteDao.get(bin = false) } returns entityList

        assertEquals(id, developRepo.getRandomNoteId())

        coVerifySequence {
            roomProvider.openRoom()
            noteDao.get(bin = false)
        }
    }
}