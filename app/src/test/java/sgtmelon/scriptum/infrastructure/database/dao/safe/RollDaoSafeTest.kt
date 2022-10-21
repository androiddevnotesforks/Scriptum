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
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst
import sgtmelon.scriptum.infrastructure.database.dao.RollDao
import sgtmelon.scriptum.infrastructure.model.exception.dao.DaoConflictIdException
import sgtmelon.scriptum.infrastructure.model.exception.dao.DaoForeignException
import sgtmelon.scriptum.infrastructure.utils.record
import sgtmelon.test.common.OverflowDelegator

/**
 * Test for RollDaoSafe.
 */
@Suppress("DEPRECATION")
class RollDaoSafeTest : ParentTest() {

    @MockK lateinit var dao: RollDao

    private val overflowDelegator = OverflowDelegator(DaoConst.OVERFLOW_COUNT)

    override fun tearDown() {
        super.tearDown()
        confirmVerified(dao)
    }

    @Test fun `insertSafe with conflict`() {
        val entity = mockk<RollEntity>()

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

    @Test fun `insertSafe with throw`() {
        val entity = mockk<RollEntity>()

        coEvery { dao.insert(entity) } throws mockk()
        FastMock.fireExtensions()
        every { any<DaoForeignException>().record() } returns Unit

        runBlocking {
            assertNull(dao.insertSafe(entity))
        }

        coVerifySequence {
            dao.insert(entity)
        }
    }

    @Test fun `insertSafe with normal result`() {
        val entity = mockk<RollEntity>()
        val id = abs(Random.nextLong())

        coEvery { dao.insert(entity) } returns id

        runBlocking {
            assertEquals(dao.insertSafe(entity), id)
        }

        coVerifySequence {
            dao.insert(entity)
        }
    }

    @Test fun `deleteSafe without overflow`() {
        val noteId = Random.nextLong()
        val excludeIdList = List((1..DaoConst.OVERFLOW_COUNT).random()) { Random.nextLong() }

        runBlocking {
            dao.deleteSafe(noteId, excludeIdList)
        }

        coVerifySequence {
            dao.delete(noteId, excludeIdList)
        }
    }


    @Test fun `deleteSafe with overflow`() {
        val noteId = Random.nextLong()
        val excludeIdList = overflowDelegator.getList { Random.nextLong() }

        val idList = overflowDelegator.getList { Random.nextLong() }
            .filter { !excludeIdList.contains(it) }
        val dividedIdList = overflowDelegator.getDividedList(idList)

        coEvery { dao.getIdList(noteId) } returns idList

        runBlocking {
            dao.deleteSafe(noteId, excludeIdList)
        }

        coVerifySequence {
            dao.getIdList(noteId)
            for (divided in dividedIdList) {
                dao.delete(divided)
            }
        }
    }

    @Test fun getListSafe() {
        val (list, dividedList) = overflowDelegator.getListPair { Random.nextLong() }
        val (entityList, entityDividedList) = overflowDelegator.getListPair(list.size) { mockk<RollEntity>() }

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