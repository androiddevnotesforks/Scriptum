package sgtmelon.scriptum.infrastructure.database.dataSource

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.scriptum.infrastructure.database.dao.RollDao
import sgtmelon.scriptum.infrastructure.database.dao.safe.deleteSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.getListSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.insertSafe
import sgtmelon.test.common.nextString

/**
 * Test for [RollDataSourceImpl].
 */
class RollDataSourceImplTest : ParentTest() {

    @MockK lateinit var dao: RollDao

    private val dataSource by lazy { RollDataSourceImpl(dao) }

    override fun tearDown() {
        super.tearDown()
        confirmVerified(dao)
    }

    @Test fun insert() {
        val entity = mockk<RollEntity>()
        val id = Random.nextLong()

        FastMock.Dao.rollDaoSafe()
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
        val id = Random.nextLong()
        val position = Random.nextInt()
        val text = nextString()

        runBlocking {
            dataSource.update(id, position, text)
        }

        coVerifySequence {
            dao.update(id, position, text)
        }
    }

    @Test fun updateCheck() {
        val id = Random.nextLong()
        val isCheck = Random.nextBoolean()

        runBlocking {
            dataSource.updateCheck(id, isCheck)
        }

        coVerifySequence {
            dao.updateCheck(id, isCheck)
        }
    }

    @Test fun delete() {
        val noteId = Random.nextLong()

        runBlocking {
            dataSource.delete(noteId)
        }

        coVerifySequence {
            dao.delete(noteId)
        }
    }

    @Test fun `delete with excludes`() {
        val noteId = Random.nextLong()
        val excludeIdList = mockk<List<Long>>()

        FastMock.Dao.rollDaoSafe()
        coEvery { dao.deleteSafe(noteId, excludeIdList) } returns Unit

        runBlocking {
            dataSource.delete(noteId, excludeIdList)
        }

        coVerifySequence {
            dao.deleteSafe(noteId, excludeIdList)
        }
    }

    @Test fun getList() {
        val list = mockk<List<RollEntity>>()

        coEvery { dao.getList() } returns list

        runBlocking {
            assertEquals(dataSource.getList(), list)
        }

        coVerifySequence {
            dao.getList()
        }
    }

    @Test fun `getList by noteId`() {
        val noteId = Random.nextLong()
        val list = mockk<MutableList<RollEntity>>()

        coEvery { dao.getList(noteId) } returns list

        runBlocking {
            assertEquals(dataSource.getList(noteId), list)
        }

        coVerifySequence {
            dao.getList(noteId)
        }
    }

    @Test fun `getList by noteIdList`() {
        val noteIdList = mockk<List<Long>>()
        val list = mockk<List<RollEntity>>()

        FastMock.Dao.rollDaoSafe()
        coEvery { dao.getListSafe(noteIdList) } returns list

        runBlocking {
            assertEquals(dataSource.getList(noteIdList), list)
        }

        coVerifySequence {
            dao.getListSafe(noteIdList)
        }
    }

    @Test fun getPreviewList() {
        val noteId = Random.nextLong()
        val list = mockk<MutableList<RollEntity>>()

        coEvery { dao.getPreviewList(noteId) } returns list

        runBlocking {
            assertEquals(dataSource.getPreviewList(noteId), list)
        }

        coVerifySequence {
            dao.getPreviewList(noteId)
        }
    }

    @Test fun getPreviewHideList() {
        val noteId = Random.nextLong()
        val list = mockk<MutableList<RollEntity>>()

        coEvery { dao.getPreviewHideList(noteId) } returns list

        runBlocking {
            assertEquals(dataSource.getPreviewHideList(noteId), list)
        }

        coVerifySequence {
            dao.getPreviewHideList(noteId)
        }
    }
}