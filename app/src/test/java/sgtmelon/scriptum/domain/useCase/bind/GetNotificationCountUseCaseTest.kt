package sgtmelon.scriptum.domain.useCase.bind

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.data.repository.database.BindRepo
import sgtmelon.tests.uniter.ParentTest
import kotlin.random.Random

/**
 * Test for [GetNotificationCountUseCase].
 */
class GetNotificationCountUseCaseTest : ParentTest() {

    @MockK lateinit var repository: BindRepo

    private val useCase by lazy { GetNotificationCountUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun invoke() {
        val count = Random.nextInt()

        coEvery { repository.getNotificationsCount() } returns count

        runBlocking {
            assertEquals(count, useCase())
        }

        coVerifySequence {
            repository.getNotificationsCount()
        }
    }
}