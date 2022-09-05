package sgtmelon.scriptum.domain.useCase.database.alarm

import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource

/**
 * Test for [GetNotificationDateListUseCaseImpl].
 */
class GetNotificationDateListUseCaseImplTest : ParentTest() {

    @MockK lateinit var dataSource: AlarmDataSource

    private val useCase by lazy { GetNotificationDateListUseCaseImpl(dataSource) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(dataSource)
    }

    @Test fun delete() {
        val dateList = mockk<List<String>>()

        runBlocking {
            assertEquals(useCase(), dateList)
        }

        coVerifySequence {
            dataSource.getDateList()
        }
    }
}