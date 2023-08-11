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
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.infrastructure.database.dao.RollVisibleDao
import sgtmelon.scriptum.infrastructure.database.dao.safe.getListSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.insertSafe
import kotlin.random.Random

/**
 * Test for [RollVisibleDataSourceImpl].
 */
class RollVisibleDataSourceImplTest : sgtmelon.tests.uniter.ParentTest() {

    @MockK lateinit var dao: RollVisibleDao

    private val dataSource by lazy { RollVisibleDataSourceImpl(dao) }

    override fun tearDown() {
        super.tearDown()
        confirmVerified(dao)
    }

    @Test fun insert() {
        val entity = mockk<RollVisibleEntity>()
        val id = Random.nextLong()

        FastMock.Dao.rollVisibleDaoSafe()
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

    @Test fun update() {
        val noteId = Random.nextLong()
        val value = Random.nextBoolean()

        runBlocking {
            dataSource.update(noteId, value)
        }

        coVerifySequence {
            dao.update(noteId, value)
        }
    }

    @Test fun getList() {
        val list = mockk<List<RollVisibleEntity>>()

        coEvery { dao.getList() } returns list

        runBlocking {
            assertEquals(dataSource.getList(), list)
        }

        coVerifySequence {
            dao.getList()
        }
    }

    @Test fun `getList by noteIdList`() {
        val noteIdList = mockk<List<Long>>()
        val list = mockk<List<RollVisibleEntity>>()

        FastMock.Dao.rollVisibleDaoSafe()
        coEvery { dao.getListSafe(noteIdList) } returns list

        runBlocking {
            assertEquals(dataSource.getList(noteIdList), list)
        }

        coVerifySequence {
            dao.getListSafe(noteIdList)
        }
    }

    @Test fun getVisible() {
        val noteId = Random.nextLong()
        val value = Random.nextBoolean()

        coEvery { dao.getVisible(noteId) } returns null

        runBlocking {
            assertNull(dataSource.getVisible(noteId))
        }

        coEvery { dao.getVisible(noteId) } returns value

        runBlocking {
            assertEquals(dataSource.getVisible(noteId), value)
        }

        coVerifySequence {
            dao.getVisible(noteId)
            dao.getVisible(noteId)
        }
    }
}