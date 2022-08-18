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
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst
import sgtmelon.scriptum.infrastructure.database.dao.AlarmDao
import sgtmelon.scriptum.infrastructure.utils.record
import sgtmelon.test.common.OverflowDelegator

/**
 * Test for AlarmDaoSafe.
 */
@Suppress("DEPRECATION")
class AlarmDaoSafeTest : ParentTest() {

    @MockK lateinit var alarmDao: AlarmDao

    private val overflowDelegator = OverflowDelegator(DaoConst.OVERFLOW_COUNT)

    override fun tearDown() {
        super.tearDown()
        confirmVerified(alarmDao)
    }

    @Test fun `insertSafe with throw`() {
        val entity = mockk<AlarmEntity>()
        val throwable = mockk<Throwable>()

        coEvery { alarmDao.insert(entity) } throws throwable
        FastMock.fireExtensions()
        every { throwable.record() } returns Unit

        runBlocking {
            assertNull(alarmDao.insertSafe(entity))
        }

        coVerifySequence {
            alarmDao.insert(entity)
        }
    }

    @Test fun `insertSafe with normal result`() {
        val entity = mockk<AlarmEntity>()
        val id = abs(Random.nextLong())

        coEvery { alarmDao.insert(entity) } returns id

        runBlocking {
            assertEquals(alarmDao.insertSafe(entity), id)
        }

        coVerifySequence {
            alarmDao.insert(entity)
        }
    }

    @Test fun getListSafe() {
        val (list, dividedList) = overflowDelegator.getListPair { Random.nextLong() }
        val (entityList, entityDividedList) = overflowDelegator.getListPair(list.size) { mockk<AlarmEntity>() }

        assertEquals(list.size, entityList.size)

        for ((i, divided) in dividedList.withIndex()) {
            coEvery { alarmDao.getList(divided) } returns entityDividedList[i]
        }

        runBlocking {
            assertEquals(alarmDao.getListSafe(list), entityList)
        }

        coVerifySequence {
            for (divided in dividedList) {
                alarmDao.getList(divided)
            }
        }
    }

    @Test fun getCountSafe() {
        val (list, dividedList) = overflowDelegator.getListPair { Random.nextLong() }
        val countList = List(size = dividedList.size) { abs(Random.nextInt()) }

        for ((i, divided) in dividedList.withIndex()) {
            coEvery { alarmDao.getCount(divided) } returns countList[i]
        }

        runBlocking {
            assertEquals(alarmDao.getCountSafe(list), countList.sum())
        }

        coVerifySequence {
            for (divided in dividedList) {
                alarmDao.getCount(divided)
            }
        }
    }
}