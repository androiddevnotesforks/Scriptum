package sgtmelon.scriptum.infrastructure.database.dataSource

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.infrastructure.database.dao.NoteDao
import sgtmelon.scriptum.infrastructure.database.dao.safe.getBindCountSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.getListSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.insertSafe
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import kotlin.random.Random

/**
 * Test for [NoteDataSourceImpl].
 */
class NoteDataSourceImplTest : sgtmelon.tests.uniter.ParentTest() {

    @MockK lateinit var dao: NoteDao

    private val dataSource by lazy { NoteDataSourceImpl(dao) }

    override fun tearDown() {
        super.tearDown()
        confirmVerified(dao)
    }

    @Test fun insert() {
        val entity = mockk<NoteEntity>()
        val id = Random.nextLong()

        FastMock.Dao.noteDaoSafe()
        coEvery { dao.insertSafe(entity) } returns null

        runBlocking {
            assertNull(dataSource.insert(entity))
        }

        coEvery { dao.insertSafe(entity) } returns id

        runBlocking {
            assertEquals(dataSource.insert(entity), id)
        }

        coVerifySequence {
            dao.insertSafe(entity)
            dao.insertSafe(entity)
        }
    }

    @Test fun `delete by entity`() {
        val entity = mockk<NoteEntity>()

        runBlocking {
            dataSource.delete(entity)
        }

        coVerifySequence {
            dao.delete(entity)
        }
    }

    @Test fun `delete by list`() {
        val list = mockk<List<NoteEntity>>()

        runBlocking {
            dataSource.delete(list)
        }

        coVerifySequence {
            dao.delete(list)
        }
    }

    @Test fun `update by entity`() {
        val entity = mockk<NoteEntity>()

        runBlocking {
            dataSource.update(entity)
        }

        coVerifySequence {
            dao.update(entity)
        }
    }

    @Test fun `update by list`() {
        val list = mockk<List<NoteEntity>>()

        runBlocking {
            dataSource.update(list)
        }

        coVerifySequence {
            dao.update(list)
        }
    }

    @Test fun getBindCount() {
        val idList = mockk<List<Long>>()
        val count = Random.nextInt()

        FastMock.Dao.noteDaoSafe()
        coEvery { dao.getBindCountSafe(idList) } returns count

        runBlocking {
            assertEquals(dataSource.getBindCount(idList), count)
        }

        coVerifySequence {
            dao.getBindCountSafe(idList)
        }
    }

    @Test fun get() {
        val id = Random.nextLong()
        val entity = mockk<NoteEntity>()

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

    @Test fun `getList by idList`() {
        val idList = mockk<List<Long>>()
        val list = mockk<List<NoteEntity>>()

        FastMock.Dao.noteDaoSafe()
        coEvery { dao.getListSafe(idList) } returns list

        runBlocking {
            assertEquals(dataSource.getList(idList), list)
        }

        coVerifySequence {
            dao.getListSafe(idList)
        }
    }

    @Test fun `getList by isBin`() {
        val isBin = Random.nextBoolean()
        val list = mockk<List<NoteEntity>>()

        coEvery { dao.getList(isBin) } returns list

        runBlocking {
            assertEquals(dataSource.getList(isBin), list)
        }

        coVerifySequence {
            dao.getList(isBin)
        }
    }

    @Test fun `getList by sort`() {
        val isBin = Random.nextBoolean()
        val mockList = List(Sort.values().size) { mockk<List<NoteEntity>>() }

        for ((i, sort) in Sort.values().withIndex()) {
            when (sort) {
                Sort.CHANGE -> coEvery { dao.getListByChange(isBin) } returns mockList[i]
                Sort.CREATE -> coEvery { dao.getListByCreate(isBin) } returns mockList[i]
                Sort.RANK -> coEvery { dao.getListByRank(isBin) } returns mockList[i]
                Sort.COLOR -> coEvery { dao.getListByColor(isBin) } returns mockList[i]
            }
        }

        runBlocking {
            for ((i, sort) in Sort.values().withIndex()) {
                assertEquals(dataSource.getList(sort, isBin), mockList[i])
            }
        }

        coVerifySequence {
            for (sort in Sort.values()) {
                when (sort) {
                    Sort.CHANGE -> dao.getListByChange(isBin)
                    Sort.CREATE -> dao.getListByCreate(isBin)
                    Sort.RANK -> dao.getListByRank(isBin)
                    Sort.COLOR -> dao.getListByColor(isBin)
                }
            }
        }
    }
}