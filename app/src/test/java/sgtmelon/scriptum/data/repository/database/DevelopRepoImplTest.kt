package sgtmelon.scriptum.data.repository.database

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
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

    private val repo by lazy {
        DevelopRepoImpl(
            noteDataSource, rollDataSource, rollVisibleDataSource,
            rankDataSource, alarmDataSource
        )
    }

    @Test fun getPrintNoteList() {
        val isBin = Random.nextBoolean()
        val list = List(getRandomSize()) { mockk<NoteEntity>() }
        val resultList = list.map { PrintItem.Note(it) }

        coEvery { noteDataSource.getList(isBin) } returns list

        runBlocking {
            assertEquals(repo.getPrintNoteList(isBin), resultList)
        }

        coVerifySequence {
            noteDataSource.getList(isBin)
        }
    }

    @Test fun getPrintRollList() {
        val list = List(getRandomSize()) { mockk<RollEntity>() }
        val resultList = list.map { PrintItem.Roll(it) }

        coEvery { rollDataSource.getList() } returns list

        runBlocking {
            assertEquals(repo.getPrintRollList(), resultList)
        }

        coVerifySequence {
            rollDataSource.getList()
        }
    }

    @Test fun getPrintVisibleList() {
        val list = List(getRandomSize()) { mockk<RollVisibleEntity>() }
        val resultList = list.map { PrintItem.Visible(it) }

        coEvery { rollVisibleDataSource.getList() } returns list

        runBlocking {
            assertEquals(repo.getPrintVisibleList(), resultList)
        }

        coVerifySequence {
            rollVisibleDataSource.getList()
        }
    }

    @Test fun getPrintRankList() {
        val list = List(getRandomSize()) { mockk<RankEntity>() }
        val resultList = list.map { PrintItem.Rank(it) }

        coEvery { rankDataSource.getList() } returns list

        runBlocking {
            assertEquals(repo.getPrintRankList(), resultList)
        }

        coVerifySequence {
            rankDataSource.getList()
        }
    }

    @Test fun getPrintAlarmList() {
        val list = List(getRandomSize()) { mockk<AlarmEntity>() }
        val resultList = list.map { PrintItem.Alarm(it) }

        coEvery { alarmDataSource.getList() } returns list

        runBlocking {
            assertEquals(repo.getPrintAlarmList(), resultList)
        }

        coVerifySequence {
            alarmDataSource.getList()
        }
    }

    @Test fun getRandomNoteId() {
        val entityList = List(getRandomSize()) { mockk<NoteEntity>() }
        val id = Random.nextLong()

        for (entity in entityList) {
            every { entity.id } returns id
        }

        coEvery { noteDataSource.getList(isBin = false) } returns entityList

        runBlocking {
            assertEquals(repo.getRandomNoteId(), id)
        }

        coVerifySequence {
            noteDataSource.getList(isBin = false)
        }
    }
}