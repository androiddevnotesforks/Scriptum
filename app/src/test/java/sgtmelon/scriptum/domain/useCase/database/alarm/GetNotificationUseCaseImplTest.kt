package sgtmelon.scriptum.domain.useCase.database.alarm

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
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource

/**
 * Test for [GetNotificationUseCaseImpl].
 */
class GetNotificationUseCaseImplTest : ParentTest() {

    @MockK lateinit var dataSource: AlarmDataSource

    private val useCase by lazy { GetNotificationUseCaseImpl(dataSource) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(dataSource)
    }

    @Test fun invoke() {
        val id = Random.nextLong()
        val item = mockk<NotificationItem>()

        coEvery { dataSource.getItem(id) } returns null

        runBlocking {
            assertNull(useCase(id))
        }

        coEvery { dataSource.getItem(id) } returns item

        runBlocking {
            assertEquals(item, useCase(id))
        }

        coVerifySequence {
            dataSource.getItem(id)
            dataSource.getItem(id)
        }
    }
}