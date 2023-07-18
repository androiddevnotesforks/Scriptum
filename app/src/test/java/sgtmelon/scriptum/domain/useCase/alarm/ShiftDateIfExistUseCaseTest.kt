package sgtmelon.scriptum.domain.useCase.alarm

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.extensions.getClearCalendar
import sgtmelon.extensions.toText
import sgtmelon.scriptum.data.repository.database.AlarmRepo
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.test.common.getRandomSize
import java.util.Calendar

/**
 * Test fun [ShiftDateIfExistUseCase].
 */
class ShiftDateIfExistUseCaseTest : ParentTest() {

    @MockK lateinit var repository: AlarmRepo

    private val useCase by lazy { ShiftDateIfExistUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun invoke() {
        val dateList = List(getRandomSize()) { getClearCalendar(it).toText() }

        val currentCalendar = getClearCalendar()
        val minute = currentCalendar.get(Calendar.MINUTE)

        coEvery { repository.getDateList() } returns dateList

        runBlocking {
            useCase(currentCalendar)
        }

        /** Dividing needed because otherwise you will get error on last hour minutes. */
        val expected = (minute + dateList.size) % 60
        assertEquals(expected, currentCalendar.get(Calendar.MINUTE))

        coVerifySequence {
            repository.getDateList()
        }
    }

    @Test fun `invoke hour overflow`() {
        val calendar = getClearCalendar()
        val addMinutes = 60 - calendar.get(Calendar.MINUTE)
        val overflowHour = calendar.get(Calendar.HOUR) + 1

        val dateList = List(getRandomSize()) {
            getClearCalendar(addMinutes = addMinutes + it)
                .toText()
        }

        val currentCalendar = getClearCalendar(addMinutes)
        val minute = currentCalendar.get(Calendar.MINUTE)

        coEvery { repository.getDateList() } returns dateList

        runBlocking {
            useCase(currentCalendar)
        }

        /** Dividing needed because otherwise you will get error on last hour minutes. */
        val expectedMinutes = (minute + dateList.size) % 60
        val expectedHour = overflowHour % 12

        assertEquals(expectedMinutes, currentCalendar.get(Calendar.MINUTE))
        assertEquals(expectedHour, currentCalendar.get(Calendar.HOUR))

        coVerifySequence {
            repository.getDateList()
        }
    }
}