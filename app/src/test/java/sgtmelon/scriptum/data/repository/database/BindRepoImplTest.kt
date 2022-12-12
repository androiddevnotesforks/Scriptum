package sgtmelon.scriptum.data.repository.database

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.parent.ParentRepoTest

/**
 * Test for [BindRepoImpl].
 */
class BindRepoImplTest : ParentRepoTest() {

    private val repository by lazy { BindRepoImpl(noteDataSource, alarmDataSource) }

    @Test fun unbindNote() {
        val id = Random.nextLong()
        val noteEntity = mockk<NoteEntity>()

        coEvery { noteDataSource.get(id) } returns null

        runBlocking {
            repository.unbindNote(id)
        }

        coEvery { noteDataSource.get(id) } returns noteEntity
        every { noteEntity.isStatus } returns false

        runBlocking {
            repository.unbindNote(id)
        }

        every { noteEntity.isStatus } returns true
        every { noteEntity.isStatus = false } returns Unit

        runBlocking {
            repository.unbindNote(id)
        }

        coVerifySequence {
            noteDataSource.get(id)

            noteDataSource.get(id)
            noteEntity.isStatus

            noteDataSource.get(id)
            noteEntity.isStatus
            noteEntity.isStatus = false
            noteDataSource.update(noteEntity)
        }
    }

    @Test fun getNotificationsCount() {
        val count = Random.nextInt()

        coEvery { alarmDataSource.getCount() } returns count

        runBlocking {
            assertEquals(repository.getNotificationsCount(), count)
        }

        coVerifySequence {
            alarmDataSource.getCount()
        }
    }
}