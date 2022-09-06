package sgtmelon.scriptum.domain.useCase.database.alarm

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import java.util.Calendar
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.common.utils.clearSeconds
import sgtmelon.common.utils.getCalendarWithAdd
import sgtmelon.common.utils.getNewCalendar
import sgtmelon.common.utils.getText
import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.parent.ParentTest

/**
 * Test fun [ShiftDateIfExistUseCaseImpl].
 */
class ShiftDateIfExistUseCaseImplTest : ParentTest() {

    @MockK lateinit var repository: AlarmRepo

    private val useCase by lazy { ShiftDateIfExistUseCaseImpl(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun invoke() {
        val dateList = List(size = 3) { getCalendarWithAdd(it).getText() }

        val currentCalendar = getNewCalendar().clearSeconds()
        val minute = currentCalendar.get(Calendar.MINUTE)

        coEvery { repository.getDateList() } returns dateList

        runBlocking {
            useCase(currentCalendar)
        }

        /** Get error on last hour minutes. */
        val expected = (minute + dateList.size) % 60
        assertEquals(expected, currentCalendar.get(Calendar.MINUTE))

        coVerifySequence {
            repository.getDateList()
        }
    }
}