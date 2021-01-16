package sgtmelon.scriptum.domain.interactor.impl.main

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastMock
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.domain.model.item.NotificationItem.Alarm
import sgtmelon.scriptum.domain.model.item.NotificationItem.Note
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IMainBridge
import java.util.*
import kotlin.random.Random

/**
 * Test for [MainInteractor].
 */
@ExperimentalCoroutinesApi
class MainInteractorTest : ParentInteractorTest() {

    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var callback: IMainBridge

    private val interactor by lazy { MainInteractor(alarmRepo, callback) }

    override fun tearDown() {
        super.tearDown()
        confirmVerified(alarmRepo, callback)
    }

    @Test override fun onDestroy() {
        assertNotNull(interactor.callback)
        interactor.onDestroy()
        assertNull(interactor.callback)
    }

    @Test fun tidyUpAlarm() = startCoTest {
        val size = getRandomSize()
        val list = MutableList<NotificationItem>(size) { mockk() }
        val noteList = List<Note>(size) { mockk() }
        val idList = List(size) { Random.nextLong() }
        val alarmList = List<Alarm>(size) { mockk() }
        val dateList = List(size) { nextString() }
        val calendarList = List<Calendar>(size) { mockk() }
        val beforeList = List(size) { Random.nextBoolean() }

        FastMock.timeExtension()

        coEvery { alarmRepo.getList() } returns list

        for ((i, item) in list.withIndex()) {
            every { item.note } returns noteList[i]
            every { noteList[i].id } returns idList[i]
            every { item.alarm } returns alarmList[i]
            every { alarmList[i].date } returns dateList[i]
            every { dateList[i].getCalendar() } returns calendarList[i]
            every { calendarList[i].beforeNow() } returns beforeList[i]
        }

        interactor.tidyUpAlarm()

        coVerifySequence {
            alarmRepo.getList()

            for ((i, item) in list.withIndex()) {
                item.note
                noteList[i].id

                item.alarm
                alarmList[i].date
                dateList[i].getCalendar()

                calendarList[i].beforeNow()
                if (beforeList[i]) {
                    callback.cancelAlarm(idList[i])
                    alarmRepo.delete(idList[i])
                } else {
                    callback.setAlarm(calendarList[i], idList[i])
                }
            }
        }
    }
}