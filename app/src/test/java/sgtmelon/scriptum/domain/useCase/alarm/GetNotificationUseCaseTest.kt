package sgtmelon.scriptum.domain.useCase.alarm

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.data.repository.database.AlarmRepo

/**
 * Test for [GetNotificationUseCase].
 */
class GetNotificationUseCaseTest : ParentTest() {

    @MockK lateinit var repository: AlarmRepo

    private val useCase by lazy { GetNotificationUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun invoke() {
        val id = Random.nextLong()
        val item = mockk<NotificationItem>()

        coEvery { repository.getItem(id) } returns null

        runBlocking {
            assertNull(useCase(id))
        }

        coEvery { repository.getItem(id) } returns item

        runBlocking {
            assertEquals(item, useCase(id))
        }

        coVerifySequence {
            repository.getItem(id)
            repository.getItem(id)
        }
    }
}