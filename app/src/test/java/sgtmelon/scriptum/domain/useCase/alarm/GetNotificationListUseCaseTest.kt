package sgtmelon.scriptum.domain.useCase.alarm

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.data.repository.database.AlarmRepo
import sgtmelon.tests.uniter.ParentTest

/**
 * Test for [GetNotificationListUseCase].
 */
class GetNotificationListUseCaseTest : ParentTest() {

    @MockK lateinit var repository: AlarmRepo

    private val useCase by lazy { GetNotificationListUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun getList() {
        val itemList = mockk<List<NotificationItem>>()

        coEvery { repository.getList() } returns itemList

        runBlocking {
            assertEquals(useCase(), itemList)
        }

        coVerifySequence {
            repository.getList()
        }
    }
}