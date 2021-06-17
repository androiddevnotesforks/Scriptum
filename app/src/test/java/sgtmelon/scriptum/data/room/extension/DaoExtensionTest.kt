package sgtmelon.scriptum.data.room.extension

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.FastMock
import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.parent.ParentRoomRepoTest
import kotlin.random.Random

/**
 * Tests for [DaoExtension].
 */
@ExperimentalCoroutinesApi
class DaoExtensionTest : ParentRoomRepoTest() {

    @Test fun rankSafeInsert() = startCoTest {
        val entity = mockk<RankEntity>()

        val daoId = Random.nextLong()
        val returnId = Random.nextLong()

        coEvery { rankDao.insert(entity) } returns daoId
        FastMock.daoHelpExtension()
        every { daoId.checkSafe() } returns returnId

        assertEquals(returnId, rankDao.safeInsert(entity))

        every { daoId.checkSafe() } returns null

        assertNull(rankDao.safeInsert(entity))

        coVerifySequence {
            repeat(times = 2) {
                rankDao.insert(entity)
                daoId.checkSafe()
            }
        }
    }

    @Test fun rankSafeDelete() {
        TODO()
    }

}