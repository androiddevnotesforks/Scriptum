package sgtmelon.scriptum.infrastructure.database.dataSource

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.parent.ParentCoTest
import sgtmelon.scriptum.infrastructure.database.dao.AlarmDao

/**
 * Test for [AlarmDataSourceImpl].
 */
@ExperimentalCoroutinesApi
class AlarmDataSourceImplTest : ParentCoTest() {

    /*@MockK lateinit*/ var dao: AlarmDao = mockk()

    private val dataSource by lazy { AlarmDataSourceImpl(dao) }

    override fun tearDown() {
        super.tearDown()
        confirmVerified(dao)
    }

    //    @Test fun insert() {
    //        val entity = mockk<AlarmEntity>()
    //        val id = Random.nextLong()
    //
    //        coEvery { dao.insert(entity) } returns id
    //
    //        runBlocking {
    //            assertEquals(dataSource.insert(entity), id)
    //        }
    //
    //        coVerifySequence {
    //            dao.insert(entity)
    //        }
    //    }
    //
    //    @Test fun delete() {
    //        val noteId = Random.nextLong()
    //
    //        runBlocking {
    //            dataSource.delete(noteId)
    //        }
    //
    //        coVerifySequence {
    //            dao.delete(noteId)
    //        }
    //    }
    //
    //    @Test fun update() {
    //        val entity = mockk<AlarmEntity>()
    //
    //        runBlocking {
    //            dataSource.update(entity)
    //        }
    //
    //        coVerifySequence {
    //            dao.update(entity)
    //        }
    //    }
    //
    //    @Test fun `get by noteId`() {
    //        val id = mockk<Long>()
    //        val entity = mockk<AlarmEntity>()
    //
    //        coEvery { dao.get(id) } returns null
    //
    //        runBlocking {
    //            assertNull(dataSource.get(id))
    //        }
    //
    //        coEvery { dao.get(id) } returns entity
    //
    //        runBlocking {
    //            assertEquals(dataSource.get(id), entity)
    //        }
    //
    //        coVerifySequence {
    //            dao.get(id)
    //            dao.get(id)
    //        }
    //    }
    //
    //    @Test fun `get list`() {
    //        val list = mockk<List<AlarmEntity>>()
    //
    //        coEvery { dao.get() } returns list
    //
    //        runBlocking {
    //            assertEquals(dataSource.get(), list)
    //        }
    //
    //        coVerifySequence {
    //            dao.get()
    //        }
    //    }
    //
    //    @Test fun `get by noteIdList`() {
    //        val noteIdList = mockk<List<Long>>()
    //        val list = mockk<List<AlarmEntity>>()
    //
    //        FastMock.Dao.alarmDaoSafe()
    //        coEvery { dao.getSafe(noteIdList) } returns list
    //
    //        runBlocking {
    //            assertEquals(dataSource.get(noteIdList), list)
    //        }
    //
    //        coVerifySequence {
    //            dao.getSafe(noteIdList)
    //        }
    //    }

    @Test fun getItem() {
        //        TODO()
        val id = mockk<Long>()
        val entity = mockk<AlarmEntity>()

        coEvery { dao.get(id) } returns null

        runBlocking {
            assertNull(dataSource.get(id))
        }

        coEvery { dao.get(id) } returns entity

        runBlocking {
            assertEquals(dataSource.get(id), entity)
        }

        coVerifySequence {
            dao.get(id)
            dao.get(id)
        }
    }
    //
    //    @Test fun getItemList() {
    //        val list = mockk<List<NotificationItem>>()
    //
    //        coEvery { dao.getItemList() } returns list
    //
    //        runBlocking {
    //            assertEquals(dataSource.getItemList(), list)
    //        }
    //
    //        coVerifySequence {
    //            dao.getItemList()
    //        }
    //    }
    //
    //    @Test fun getCount() {
    //        val count = Random.nextInt()
    //
    //        coEvery { dao.getCount() } returns count
    //
    //        runBlocking {
    //            assertEquals(dataSource.getCount(), count)
    //        }
    //
    //        coVerifySequence {
    //            dao.getCount()
    //        }
    //    }
    //
    //    @Test fun `getCount by noteIdList`() {
    //        val noteIdList = mockk<List<Long>>()
    //        val count = Random.nextInt()
    //
    //        FastMock.Dao.alarmDaoSafe()
    //        coEvery { dao.getCountSafe(noteIdList) } returns count
    //
    //        runBlocking {
    //            assertEquals(dataSource.getCount(noteIdList), count)
    //        }
    //
    //        coVerifySequence {
    //            dao.getCountSafe(noteIdList)
    //        }
    //    }
}