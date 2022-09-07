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
import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.parent.ParentTest

/**
 * Test for [GetNotificationDateListUseCaseImpl].
 */
class GetNotificationDateListUseCaseImplTest : ParentTest() {

    @MockK lateinit var repository: AlarmRepo

    private val useCase by lazy { GetNotificationDateListUseCaseImpl(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun invoke() {
        val dateList = mockk<List<String>>()

        coEvery { repository.getDateList() } returns dateList

        runBlocking {
            assertEquals(useCase(), dateList)
        }

        coVerifySequence {
            repository.getDateList()
        }
    }
}