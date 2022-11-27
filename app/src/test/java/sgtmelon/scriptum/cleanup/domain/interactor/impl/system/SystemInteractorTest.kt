package sgtmelon.scriptum.cleanup.domain.interactor.impl.system

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import java.util.Calendar
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.extensions.isBeforeNow
import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BindRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.parent.ParentInteractorTest
import sgtmelon.scriptum.cleanup.presentation.screen.system.ISystemBridge
import sgtmelon.scriptum.data.repository.database.AlarmRepo
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.testing.getRandomSize
import sgtmelon.test.common.nextString

/**
 * Test for [SystemInteractor]
 */
@ExperimentalCoroutinesApi
class SystemInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var bindRepo: BindRepo
    @MockK lateinit var alarmRepo: AlarmRepo
    @MockK lateinit var noteRepo: NoteRepo

    @MockK lateinit var callback: ISystemBridge

    private val interactor by lazy {
        SystemInteractor(preferencesRepo, bindRepo, alarmRepo, noteRepo, callback)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferencesRepo, bindRepo, alarmRepo, noteRepo, callback)
    }

    @Test override fun onDestroy() {
        assertNotNull(interactor.callback)
        interactor.onDestroy()
        assertNull(interactor.callback)
    }

    @Test fun tidyUpAlarm() = startCoTest {
        val size = getRandomSize()
        val list = List<NotificationItem>(size) { mockk() }
        val noteList = List<NotificationItem.Note>(size) { mockk() }
        val idList = List(size) { Random.nextLong() }
        val alarmList = List<NotificationItem.Alarm>(size) { mockk() }
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
            every { dateList[i].toCalendar() } returns calendarList[i]
            every { calendarList[i].isBeforeNow() } returns beforeList[i]
        }

        interactor.tidyUpAlarm()

        coVerifySequence {
            alarmRepo.getList()

            for ((i, item) in list.withIndex()) {
                item.note
                noteList[i].id

                item.alarm
                alarmList[i].date
                dateList[i].toCalendar()

                calendarList[i].isBeforeNow()
                if (beforeList[i]) {
                    callback.cancelAlarm(idList[i])
                    alarmRepo.delete(idList[i])
                } else {
                    callback.setAlarm(idList[i], calendarList[i], showToast = false)
                }
            }
        }
    }

    @Test fun notifyCountBind() = startCoTest {
        val count = Random.nextInt()

        coEvery { bindRepo.getNotificationsCount() } returns count

        interactor.notifyCountBind()

        coVerifySequence {
            bindRepo.getNotificationsCount()
            callback.notifyCountBind(count)
        }
    }

    @Test fun unbindNote() = startCoTest {
        val id = Random.nextLong()

        interactor.unbindNote(id)

        coVerifySequence {
            bindRepo.unbindNote(id)
        }
    }
}