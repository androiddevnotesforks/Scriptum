package sgtmelon.scriptum.domain.interactor.impl.notification

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.getCalendarOrNull
import sgtmelon.extension.getRandomFutureTime
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.INotificationBridge
import kotlin.random.Random

/**
 * Test for [NotificationInteractor].
 */
@ExperimentalCoroutinesApi
class NotificationInteractorTest : ParentInteractorTest() {

    private val data = TestData.Notification

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var noteRepo: INoteRepo
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var bindRepo: IBindRepo
    @MockK lateinit var callback: INotificationBridge

    private val interactor by lazy {
        NotificationInteractor(preferenceRepo, noteRepo, alarmRepo, bindRepo, callback)
    }

    @Test override fun onDestroy() {
        assertNotNull(interactor.callback)
        interactor.onDestroy()
        assertNull(interactor.callback)
    }


    @Test fun getTheme() = FastTest.getTheme(preferenceRepo) { interactor.theme }

    @Test fun getCount() = startCoTest {
        val countList = listOf(Random.nextInt(), Random.nextInt())

        for (it in countList) {
            coEvery { bindRepo.getNotificationCount() } returns it
            assertEquals(it, interactor.getCount())
        }

        coVerifySequence {
            repeat(countList.size) { bindRepo.getNotificationCount() }
        }
    }

    @Test fun getList() = startCoTest {
        val list = data.itemList

        coEvery { alarmRepo.getList() } returns list
        assertEquals(list, interactor.getList())

        coVerifySequence {
            alarmRepo.getList()
        }
    }


    @Test fun setNotification() = startCoTest {
        val notificationItem = mockk<NotificationItem>()
        val newNotificationItem = mockk<NotificationItem>()
        val noteItem = mockk<NoteItem>()

        val note = mockk<NotificationItem.Note>()
        val alarm = mockk<NotificationItem.Alarm>()

        val id = Random.nextLong()
        val date = getRandomFutureTime()
        val calendar = date.getCalendarOrNull() ?: throw NullPointerException()

        every { notificationItem.note } returns note
        every { notificationItem.alarm } returns alarm
        every { note.id } returns id

        every { alarm.date } returns nextString()
        coEvery { noteRepo.getItem(id, isOptimal = true) } returns null
        assertNull(interactor.setNotification(notificationItem))

        coEvery { noteRepo.getItem(id, isOptimal = true) } returns noteItem
        assertNull(interactor.setNotification(notificationItem))

        every { alarm.date } returns date
        coEvery { alarmRepo.getItem(id) } returns null
        assertNull(interactor.setNotification(notificationItem))

        coEvery { alarmRepo.getItem(id) } returns newNotificationItem
        assertEquals(newNotificationItem, interactor.setNotification(notificationItem))

        coVerifySequence {
            notificationItem.note
            note.id
            notificationItem.alarm
            alarm.date
            noteRepo.getItem(id, isOptimal = true)

            notificationItem.note
            note.id
            notificationItem.alarm
            alarm.date
            noteRepo.getItem(id, isOptimal = true)

            repeat(times = 2) {
                notificationItem.note
                note.id
                notificationItem.alarm
                alarm.date
                noteRepo.getItem(id, isOptimal = true)
                alarmRepo.insertOrUpdate(noteItem, date)
                callback.setAlarm(calendar, id)
                alarmRepo.getItem(id)
            }
        }
    }

    @Test fun cancelNotification() = startCoTest {
        val item = data.itemList.random()
        val id = item.note.id

        interactor.cancelNotification(item)

        coVerifySequence {
            alarmRepo.delete(id)
            callback.cancelAlarm(id)
        }
    }

}