package sgtmelon.scriptum.domain.useCase.alarm

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.data.repository.database.AlarmRepo
import kotlin.random.Random

/**
 * Test for [DeleteNotificationUseCase].
 */
class DeleteNotificationUseCaseTest : sgtmelon.tests.uniter.ParentTest() {

    @MockK lateinit var repository: AlarmRepo

    private val useCase by lazy { DeleteNotificationUseCase(repository) }
    private val spyUseCase by lazy { spyk(useCase) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun `invoke with noteItem`() {
        val item = mockk<NoteItem>()
        val id = Random.nextLong()

        every { item.id } returns id
        coEvery { spyUseCase(id) } returns Unit

        runBlocking {
            spyUseCase(item)
        }

        coVerifySequence {
            spyUseCase(item)
            item.id
            spyUseCase(id)
        }
    }

    @Test fun `invoke with notificationItem`() {
        val item = mockk<NotificationItem>()
        val note = mockk<NotificationItem.Note>()
        val id = Random.nextLong()

        every { item.note } returns note
        every { note.id } returns id
        coEvery { spyUseCase(id) } returns Unit

        runBlocking {
            spyUseCase(item)
        }

        coVerifySequence {
            spyUseCase(item)
            item.note
            note.id
            spyUseCase(id)
        }
    }

    @Test fun `invoke with id`() {
        val id = Random.nextLong()

        runBlocking {
            useCase(id)
        }

        coVerifySequence {
            repository.delete(id)
        }
    }
}