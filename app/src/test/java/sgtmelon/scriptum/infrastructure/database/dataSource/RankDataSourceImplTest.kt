package sgtmelon.scriptum.infrastructure.database.dataSource

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.infrastructure.database.dao.RankDao
import sgtmelon.scriptum.infrastructure.database.dao.safe.insertSafe
import sgtmelon.test.common.nextString

/**
 * Test for [RankDataSourceImpl].
 */
class RankDataSourceImplTest : ParentTest() {

    @MockK lateinit var dao: RankDao

    private val dataSource by lazy { RankDataSourceImpl(dao) }

    override fun tearDown() {
        super.tearDown()
        confirmVerified(dao)
    }

    @Test fun insert() {
        val entity = mockk<RankEntity>()
        val id = Random.nextLong()

        FastMock.Dao.rankDaoSafe()
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

    @Test fun delete() {
        val item = mockk<RankItem>()
        val name = nextString()

        every { item.name } returns name

        runBlocking {
            dataSource.delete(item)
        }

        coVerifySequence {
            item.name
            dao.delete(name)
        }
    }

    @Test fun `update item`() {
        val entity = mockk<RankEntity>()

        runBlocking {
            dataSource.update(entity)
        }

        coVerifySequence {
            dao.update(entity)
        }
    }

    @Test fun `update list`() {
        val list = mockk<List<RankEntity>>()

        runBlocking {
            dataSource.update(list)
        }

        coVerifySequence {
            dao.update(list)
        }
    }

    @Test fun getCount() {
        val count = Random.nextInt()

        coEvery { dao.getCount() } returns count

        runBlocking {
            assertEquals(dataSource.getCount(), count)
        }

        coVerifySequence {
            dao.getCount()
        }
    }

    @Test fun get() {
        val id = Random.nextLong()
        val entity = mockk<RankEntity>()

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

    @Test fun getList() {
        val list = mockk<List<RankEntity>>()

        coEvery { dao.getList() } returns list

        runBlocking {
            assertEquals(dataSource.getList(), list)
        }

        coVerifySequence {
            dao.getList()
        }
    }

    @Test fun getIdVisibleList() {
        val list = mockk<List<Long>>()

        coEvery { dao.getIdVisibleList() } returns list

        runBlocking {
            assertEquals(dataSource.getIdVisibleList(), list)
        }

        coVerifySequence {
            dao.getIdVisibleList()
        }
    }

    @Test fun getIdList() {
        val list = mockk<List<Long>>()

        coEvery { dao.getIdList() } returns list

        runBlocking {
            assertEquals(dataSource.getIdList(), list)
        }

        coVerifySequence {
            dao.getIdList()
        }
    }

    @Test fun getNameList() {
        val list = mockk<List<String>>()

        coEvery { dao.getNameList() } returns list

        runBlocking {
            assertEquals(dataSource.getNameList(), list)
        }

        coVerifySequence {
            dao.getNameList()
        }
    }

    @Test fun getId() {
        val position = Random.nextInt()
        val id = Random.nextLong()

        coEvery { dao.getId(position) } returns null

        runBlocking {
            assertNull(dataSource.getId(position))
        }

        coEvery { dao.getId(position) } returns id

        runBlocking {
            assertEquals(dataSource.getId(position), id)
        }

        coVerifySequence {
            dao.getId(position)
            dao.getId(position)
        }
    }
}