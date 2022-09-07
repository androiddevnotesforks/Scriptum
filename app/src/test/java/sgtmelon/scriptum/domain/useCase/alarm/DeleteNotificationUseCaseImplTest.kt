package sgtmelon.scriptum.domain.useCase.alarm

import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.parent.ParentTest

/**
 * Test for [DeleteNotificationUseCaseImpl].
 */
class DeleteNotificationUseCaseImplTest : ParentTest() {

    @MockK lateinit var repository: AlarmRepo

    private val useCase by lazy { DeleteNotificationUseCaseImpl(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun delete() {
        val id = Random.nextLong()

        runBlocking {
            useCase(id)
        }

        coVerifySequence {
            repository.delete(id)
        }
    }
}