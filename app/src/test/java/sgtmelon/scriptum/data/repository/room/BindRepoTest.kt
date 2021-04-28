package sgtmelon.scriptum.data.repository.room

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentRoomRepoTest
import sgtmelon.scriptum.data.room.entity.NoteEntity
import kotlin.random.Random

/**
 * Test for [BindRepo].
 */
@ExperimentalCoroutinesApi
class BindRepoTest : ParentRoomRepoTest() {

    private val bindRepo by lazy { BindRepo(roomProvider) }

    @Test fun unbindNote() = startCoTest {
        val id = Random.nextLong()
        val noteEntity = mockk<NoteEntity>()

        coEvery { noteDao.get(id) } returns null
        bindRepo.unbindNote(id)

        coEvery { noteDao.get(id) } returns noteEntity
        every { noteEntity.isStatus } returns false
        bindRepo.unbindNote(id)

        every { noteEntity.isStatus } returns true
        every { noteEntity.isStatus = false } returns Unit
        bindRepo.unbindNote(id)

        coVerifySequence {
            roomProvider.openRoom()
            noteDao.get(id)

            roomProvider.openRoom()
            noteDao.get(id)
            noteEntity.isStatus

            roomProvider.openRoom()
            noteDao.get(id)
            noteEntity.isStatus
            noteEntity.isStatus = false
            noteDao.update(noteEntity)
        }
    }

    @Test fun getNotificationCount() = startCoTest{
        val count = Random.nextInt()

        coEvery { alarmDao.getCount() } returns count

        assertEquals(count, bindRepo.getNotificationCount())

        coVerifySequence {
            roomProvider.openRoom()
            alarmDao.getCount()
        }
    }
}