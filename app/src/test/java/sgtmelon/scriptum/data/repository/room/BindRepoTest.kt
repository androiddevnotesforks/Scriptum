package sgtmelon.scriptum.data.repository.room

import io.mockk.coEvery
import io.mockk.coVerifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentRoomRepoTest
import sgtmelon.scriptum.data.room.entity.NoteEntity
import kotlin.random.Random

/**
 * Test for [BindRepo].
 */
@ExperimentalCoroutinesApi
class BindRepoTest : ParentRoomRepoTest() {

    private val badBindRepo by lazy { BindRepo(badRoomProvider) }
    private val goodBindRepo by lazy { BindRepo(goodRoomProvider) }

    @Test fun unbindNote() = startCoTest {
        val id = Random.nextLong()
        val entity = NoteEntity(name = "testEntity", isStatus = true)

        assertNull(badBindRepo.unbindNote(Random.nextLong()))

        coEvery { noteDao.get(id) } returns null
        assertEquals(false, goodBindRepo.unbindNote(id))
        assertTrue(entity.isStatus)

        coEvery { noteDao.get(id) } returns entity
        assertEquals(true, goodBindRepo.unbindNote(id))
        assertFalse(entity.isStatus)

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            noteDao.get(id)

            goodRoomProvider.openRoom()
            noteDao.get(id)
            noteDao.update(entity)
        }
    }

    @Test fun getNotificationCount() = startCoTest{
        val count = Random.nextInt()

        coEvery { alarmDao.getCount() } returns count

        assertNull(badBindRepo.getNotificationCount())
        assertEquals(count, goodBindRepo.getNotificationCount())

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            alarmDao.getCount()
        }
    }

}