package sgtmelon.scriptum.domain.useCase.database.alarm

import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource

/**
 * Test for [DeleteNotificationUseCaseImpl].
 */
class DeleteNotificationUseCaseImplTest : ParentTest() {

    @MockK lateinit var dataSource: AlarmDataSource

    private val useCase by lazy { DeleteNotificationUseCaseImpl(dataSource) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(dataSource)
    }

    @Test fun delete() {
        val id = Random.nextLong()

        runBlocking {
            useCase(id)
        }

        coVerifySequence {
            dataSource.delete(id)
        }
    }
}