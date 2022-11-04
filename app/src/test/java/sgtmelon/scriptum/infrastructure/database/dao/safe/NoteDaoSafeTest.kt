package sgtmelon.scriptum.infrastructure.database.dao.safe

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlin.math.abs
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.infrastructure.database.dao.NoteDao
import sgtmelon.scriptum.infrastructure.database.model.DaoConst
import sgtmelon.scriptum.infrastructure.model.exception.dao.DaoConflictIdException
import sgtmelon.scriptum.infrastructure.utils.record
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.test.common.OverflowDelegator

/**
 * Test for NoteDaoSafe.
 */
@Suppress("DEPRECATION")
class NoteDaoSafeTest : ParentTest() {

    @MockK lateinit var dao: NoteDao

    private val overflowDelegator = OverflowDelegator(DaoConst.OVERFLOW_COUNT)

    override fun tearDown() {
        super.tearDown()
        confirmVerified(dao)
    }

    @Test fun `insertSafe with conflict`() {
        val entity = mockk<NoteEntity>()

        coEvery { dao.insert(entity) } returns DaoConst.UNIQUE_ERROR_ID
        FastMock.fireExtensions()
        every { any<DaoConflictIdException>().record() } returns Unit

        runBlocking {
            assertNull(dao.insertSafe(entity))
        }

        coVerifySequence {
            dao.insert(entity)
        }
    }

    @Test fun `insertSafe with normal result`() {
        val entity = mockk<NoteEntity>()
        val id = abs(Random.nextLong())

        coEvery { dao.insert(entity) } returns id

        runBlocking {
            assertEquals(dao.insertSafe(entity), id)
        }

        coVerifySequence {
            dao.insert(entity)
        }
    }


    @Test fun getBindCountSafe() {
        val (list, dividedList) = overflowDelegator.getListPair { Random.nextLong() }
        val countList = List(size = dividedList.size) { abs(Random.nextInt()) }

        for ((i, divided) in dividedList.withIndex()) {
            coEvery { dao.getBindCount(divided) } returns countList[i]
        }

        runBlocking {
            assertEquals(dao.getBindCountSafe(list), countList.sum())
        }

        coVerifySequence {
            for (divided in dividedList) {
                dao.getBindCount(divided)
            }
        }
    }

    @Test fun getListSafe() {
        val (list, dividedList) = overflowDelegator.getListPair { Random.nextLong() }
        val (entityList, entityDividedList) = overflowDelegator.getListPair(list.size) { mockk<NoteEntity>() }

        assertEquals(list.size, entityList.size)

        for ((i, divided) in dividedList.withIndex()) {
            coEvery { dao.getList(divided) } returns entityDividedList[i]
        }

        runBlocking {
            assertEquals(dao.getListSafe(list), entityList)
        }

        coVerifySequence {
            for (divided in dividedList) {
                dao.getList(divided)
            }
        }
    }
}