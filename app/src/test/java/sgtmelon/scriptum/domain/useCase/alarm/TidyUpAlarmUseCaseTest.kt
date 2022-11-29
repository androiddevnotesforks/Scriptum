package sgtmelon.scriptum.domain.useCase.alarm

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.data.repository.database.AlarmRepo
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Tests for [TidyUpAlarmUseCase].
 */
class TidyUpAlarmUseCaseTest : ParentTest() {

    @MockK lateinit var repository: AlarmRepo

    private val useCase by lazy { TidyUpAlarmUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    //    val size = getRandomSize()
    //    val list = List<NotificationItem>(size) { mockk() }
    //    val noteList = List<NotificationItem.Note>(size) { mockk() }
    //    val idList = List(size) { Random.nextLong() }
    //    val alarmList = List<NotificationItem.Alarm>(size) { mockk() }
    //    val dateList = List(size) { nextString() }
    //    val calendarList = List<Calendar>(size) { mockk() }
    //    val beforeList = List(size) { Random.nextBoolean() }
    //
    //    FastMock.timeExtension()
    //
    //    coEvery { alarmRepo.getList() } returns list
    //
    //    for ((i, item) in list.withIndex()) {
    //        every { item.note } returns noteList[i]
    //        every { noteList[i].id } returns idList[i]
    //        every { item.alarm } returns alarmList[i]
    //        every { alarmList[i].date } returns dateList[i]
    //        every { dateList[i].toCalendar() } returns calendarList[i]
    //        every { calendarList[i].isBeforeNow() } returns beforeList[i]
    //    }
    //
    //    interactor.tidyUpAlarm()
    //
    //    coVerifySequence {
    //        alarmRepo.getList()
    //
    //        for ((i, item) in list.withIndex()) {
    //            item.note
    //            noteList[i].id
    //
    //            item.alarm
    //            alarmList[i].date
    //            dateList[i].toCalendar()
    //
    //            calendarList[i].isBeforeNow()
    //            if (beforeList[i]) {
    //                callback.cancelAlarm(idList[i])
    //                alarmRepo.delete(idList[i])
    //            } else {
    //                callback.setAlarm(idList[i], calendarList[i], showToast = false)
    //            }
    //        }
    //    }

    @Test fun invoke() {
        TODO("don't know how to test flow")
    }
}