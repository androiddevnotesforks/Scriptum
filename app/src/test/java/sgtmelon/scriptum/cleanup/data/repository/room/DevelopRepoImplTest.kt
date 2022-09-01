package sgtmelon.scriptum.cleanup.data.repository.room

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem
import sgtmelon.scriptum.cleanup.getRandomSize
import sgtmelon.scriptum.cleanup.parent.ParentRepoTest

/**
 * Test for [DevelopRepoImpl].
 */
class DevelopRepoImplTest : ParentRepoTest() {

    private val developRepo by lazy { DevelopRepoImpl(roomProvider) }

    @Test fun getPrintNoteList() = startCoTest {
        val isBin = Random.nextBoolean()
        val list = List(getRandomSize()) { mockk<NoteEntity>() }
        val resultList = list.map { PrintItem.Note(it) }

        coEvery { noteDao.getList(isBin) } returns list

        assertEquals(resultList, developRepo.getPrintNoteList(isBin))

        coVerifySequence {
            roomProvider.openRoom()
            noteDao.getList(isBin)
        }
    }

    @Test fun getPrintRollList() = startCoTest {
        val list = List(getRandomSize()) { mockk<RollEntity>() }
        val resultList = list.map { PrintItem.Roll(it) }

        coEvery { rollDao.getList() } returns list

        assertEquals(resultList, developRepo.getPrintRollList())

        coVerifySequence {
            roomProvider.openRoom()
            rollDao.getList()
        }
    }

    @Test fun getPrintVisibleList() = startCoTest {
        val list = List(getRandomSize()) { mockk<RollVisibleEntity>() }
        val resultList = list.map { PrintItem.Visible(it) }

        coEvery { rollVisibleDao.getList() } returns list

        assertEquals(resultList, developRepo.getPrintVisibleList())

        coVerifySequence {
            roomProvider.openRoom()
            rollVisibleDao.getList()
        }
    }

    @Test fun getPrintRankList() = startCoTest {
        val list = List(getRandomSize()) { mockk<RankEntity>() }
        val resultList = list.map { PrintItem.Rank(it) }

        coEvery { rankDao.getList() } returns list

        assertEquals(resultList, developRepo.getPrintRankList())

        coVerifySequence {
            roomProvider.openRoom()
            rankDao.getList()
        }
    }

    @Test fun getPrintAlarmList() = startCoTest {
        val list = List(getRandomSize()) { mockk<AlarmEntity>() }
        val resultList = list.map { PrintItem.Alarm(it) }

        coEvery { alarmDao.getList() } returns list

        assertEquals(resultList, developRepo.getPrintAlarmList())

        coVerifySequence {
            roomProvider.openRoom()
            alarmDao.getList()
        }
    }

    @Test fun getRandomNoteId() = startCoTest {
        val entityList = List(getRandomSize()) { mockk<NoteEntity>() }
        val id = Random.nextLong()

        for (entity in entityList) {
            every { entity.id } returns id
        }

        coEvery { noteDao.getList(isBin = false) } returns entityList

        assertEquals(id, developRepo.getRandomNoteId())

        coVerifySequence {
            roomProvider.openRoom()
            noteDao.getList(isBin = false)
        }
    }
}