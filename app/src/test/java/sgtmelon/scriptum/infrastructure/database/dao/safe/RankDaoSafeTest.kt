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
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst
import sgtmelon.scriptum.infrastructure.database.dao.RankDao
import sgtmelon.scriptum.infrastructure.model.exception.dao.DaoConflictIdException
import sgtmelon.scriptum.infrastructure.utils.record

/**
 * Test for RankDaoSafe.
 */
@Suppress("DEPRECATION")
class RankDaoSafeTest : ParentTest() {

    @MockK lateinit var dao: RankDao

    override fun tearDown() {
        super.tearDown()
        confirmVerified(dao)
    }

    @Test fun `insertSafe with conflict`() {
        val entity = mockk<RankEntity>()

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
        val entity = mockk<RankEntity>()
        val id = abs(Random.nextLong())

        coEvery { dao.insert(entity) } returns id

        runBlocking {
            assertEquals(dao.insertSafe(entity), id)
        }

        coVerifySequence {
            dao.insert(entity)
        }
    }
}