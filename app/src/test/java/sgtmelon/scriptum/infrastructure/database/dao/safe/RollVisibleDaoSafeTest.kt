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
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst
import sgtmelon.scriptum.infrastructure.database.dao.RollVisibleDao
import sgtmelon.scriptum.infrastructure.utils.record
import sgtmelon.test.common.OverflowDelegator

/**
 * Test for RollVisibleDaoSafe.
 */
@Suppress("DEPRECATION")
class RollVisibleDaoSafeTest : ParentTest() {

    @MockK lateinit var rollVisibleDao: RollVisibleDao

    private val overflowDelegator = OverflowDelegator(DaoConst.OVERFLOW_COUNT)

    override fun tearDown() {
        super.tearDown()
        confirmVerified(rollVisibleDao)
    }

    @Test fun `insertSafe with conflict`() {
        val entity = mockk<RollVisibleEntity>()

        coEvery { rollVisibleDao.insert(entity) } returns DaoConst.UNIQUE_ERROR_ID

        runBlocking {
            assertNull(rollVisibleDao.insertSafe(entity))
        }

        coVerifySequence {
            rollVisibleDao.insert(entity)
        }
    }

    @Test fun `insertSafe with throw`() {
        val entity = mockk<RollVisibleEntity>()
        val throwable = mockk<Throwable>()

        coEvery { rollVisibleDao.insert(entity) } throws throwable
        FastMock.fireExtensions()
        every { throwable.record() } returns Unit

        runBlocking {
            assertNull(rollVisibleDao.insertSafe(entity))
        }

        coVerifySequence {
            rollVisibleDao.insert(entity)
        }
    }

    @Test fun `insertSafe with normal result`() {
        val entity = mockk<RollVisibleEntity>()
        val id = abs(Random.nextLong())

        coEvery { rollVisibleDao.insert(entity) } returns id

        runBlocking {
            assertEquals(rollVisibleDao.insertSafe(entity), id)
        }

        coVerifySequence {
            rollVisibleDao.insert(entity)
        }
    }

    @Test fun getListSafe() {
        val (list, dividedList) = overflowDelegator.getListPair { Random.nextLong() }
        val (entityList, entityDividedList) = overflowDelegator.getListPair(list.size) { mockk<RollVisibleEntity>() }

        assertEquals(list.size, entityList.size)

        for ((i, divided) in dividedList.withIndex()) {
            coEvery { rollVisibleDao.getList(divided) } returns entityDividedList[i]
        }

        runBlocking {
            assertEquals(rollVisibleDao.getListSafe(list), entityList)
        }

        coVerifySequence {
            for (divided in dividedList) {
                rollVisibleDao.getList(divided)
            }
        }
    }
}