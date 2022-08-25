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
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.infrastructure.database.dao.NoteDao
import sgtmelon.scriptum.infrastructure.database.dao.safe.insertSafe

/**
 * Test for [NoteDataSourceImpl].
 */
class NoteDataSourceImplTest : ParentTest() {

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

    @Test fun getRankVisibleCount() {
        TODO()
    }

    @Test fun getBindCount() {
        TODO()
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
        TODO()
    }

    @Test fun `getList by isBin`() {
        TODO()
    }

    @Test fun `getList by sort`() {
        TODO()
    }
}