package sgtmelon.scriptum.data.repository.room

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
        val noteEntity = mockk<NoteEntity>()

        every { noteEntity.isStatus = false } returns Unit

        assertNull(badBindRepo.unbindNote(Random.nextLong()))

        coEvery { noteDao.get(id) } returns null
        assertEquals(false, goodBindRepo.unbindNote(id))

        coEvery { noteDao.get(id) } returns noteEntity
        assertEquals(true, goodBindRepo.unbindNote(id))

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            noteDao.get(id)

            goodRoomProvider.openRoom()
            noteDao.get(id)
            noteEntity.isStatus = false
            noteDao.update(noteEntity)
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