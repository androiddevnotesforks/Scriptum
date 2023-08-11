package sgtmelon.scriptum.infrastructure.database.dao.safe

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.infrastructure.database.dao.AlarmDao
import sgtmelon.scriptum.infrastructure.database.model.DaoConst
import sgtmelon.scriptum.infrastructure.model.exception.dao.DaoForeignException
import sgtmelon.scriptum.infrastructure.utils.extensions.record
import sgtmelon.test.common.OverflowDelegator
import sgtmelon.test.common.nextString
import kotlin.math.abs
import kotlin.random.Random

/**
 * Test for AlarmDaoSafe.
 */
@Suppress("DEPRECATION")
class AlarmDaoSafeTest : sgtmelon.tests.uniter.ParentTest() {

    @MockK lateinit var dao: AlarmDao

    private val overflowDelegator = OverflowDelegator(DaoConst.OVERFLOW_COUNT)

    override fun tearDown() {
        super.tearDown()
        confirmVerified(dao)
    }

    @Test fun `insertSafe with throw`() {
        val entity = mockk<AlarmEntity>()
        val throwable = mockk<Throwable>()
        val message = nextString()

        coEvery { dao.insert(entity) } throws throwable
        every { throwable.message } returns message
        coEvery { dao.insert(entity) } throws throwable
        FastMock.fireExtensions()
        every { any<DaoForeignException>().record() } returns mockk()

        runBlocking {
            assertNull(dao.insertSafe(entity))
        }

        coVerifySequence {
            dao.insert(entity)
        }
    }

    @Test fun `insertSafe with normal result`() {
        val entity = mockk<AlarmEntity>()
        val id = abs(Random.nextLong())

        coEvery { dao.insert(entity) } returns id

        runBlocking {
            assertEquals(dao.insertSafe(entity), id)
        }

        coVerifySequence {
            dao.insert(entity)
        }
    }

    @Test fun getListSafe() {
        val (list, dividedList) = overflowDelegator.getListPair { Random.nextLong() }
        val (entityList, entityDividedList) = overflowDelegator.getListPair(list.size) { mockk<AlarmEntity>() }

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

    @Test fun getCountSafe() {
        val (list, dividedList) = overflowDelegator.getListPair { Random.nextLong() }
        val countList = List(size = dividedList.size) { abs(Random.nextInt()) }

        for ((i, divided) in dividedList.withIndex()) {
            coEvery { dao.getCount(divided) } returns countList[i]
        }

        runBlocking {
            assertEquals(dao.getCountSafe(list), countList.sum())
        }

        coVerifySequence {
            for (divided in dividedList) {
                dao.getCount(divided)
            }
        }
    }
}