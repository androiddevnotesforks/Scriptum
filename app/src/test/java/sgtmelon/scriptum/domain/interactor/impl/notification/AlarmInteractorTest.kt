package sgtmelon.scriptum.domain.interactor.impl.notification

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.clearSeconds
import sgtmelon.extension.getText
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastMock
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.domain.model.item.NotificationItem.Alarm
import sgtmelon.scriptum.domain.model.item.NotificationItem.Note
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.IAlarmBridge
import java.util.*
import kotlin.random.Random

/**
 * Test for [AlarmInteractor].
 */
@ExperimentalCoroutinesApi
class AlarmInteractorTest : ParentInteractorTest() {

    private val data = TestData.Note

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var noteRepo: INoteRepo
    @MockK lateinit var callback: IAlarmBridge

    private val interactor by lazy {
        AlarmInteractor(preferenceRepo, alarmRepo, noteRepo, callback)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    override fun tearDown() {
        super.tearDown()

        confirmVerified(preferenceRepo, alarmRepo, noteRepo, callback)
    }

    @Test override fun onDestroy() {
        assertNotNull(interactor.callback)
        interactor.onDestroy()
        assertNull(interactor.callback)
    }


    @Test fun getTheme() = FastTest.getTheme(preferenceRepo) { interactor.theme }

    @Test fun getRepeat() = FastTest.getRepeat(preferenceRepo) { interactor.repeat }

    @Test fun getVolume() = FastTest.getVolume(preferenceRepo) { interactor.volume }

    @Test fun getVolumeIncrease() {
        fun checkRequestGet(value: Boolean) {
            every { preferenceRepo.volumeIncrease } returns value
            assertEquals(value, interactor.volumeIncrease)
        }

        val valueList = listOf(Random.nextBoolean(), Random.nextBoolean())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.volumeIncrease }
        }
    }

    @Test fun getModel() = startCoTest {
        val noteId = Random.nextLong()
        val item = data.itemList.random()

        coEvery { noteRepo.getItem(noteId, isOptimal = true) } returns null
        assertEquals(null, interactor.getModel(noteId))

        coEvery { noteRepo.getItem(noteId, isOptimal = true) } returns item
        assertEquals(item, interactor.getModel(noteId))

        coVerifySequence {
            alarmRepo.delete(noteId)
            noteRepo.getItem(noteId, isOptimal = true)

            alarmRepo.delete(noteId)
            noteRepo.getItem(noteId, isOptimal = true)
        }
    }

    @Test fun setupRepeat() = startCoTest {
        val item = data.itemList.random()

        val timeArray = intArrayOf(1, 2, 3, 4)
        val repeat = timeArray.indices.random()
        val minute = timeArray[repeat]

        val calendar = mockk<Calendar>()
        val calendarText = Random.nextString()

        every { spyInteractor.getCalendarWithAdd(minute) } returns calendar
        coEvery { spyInteractor.checkDateExist(calendar) } returns Unit

        FastMock.timeExtension()
        every { calendar.getText() } returns calendarText

        spyInteractor.setupRepeat(item, intArrayOf(), Random.nextInt())
        spyInteractor.setupRepeat(item, timeArray, repeat)

        coVerifyOrder {
            spyInteractor.getCalendarWithAdd(minute)
            spyInteractor.checkDateExist(calendar)

            alarmRepo.insertOrUpdate(item, calendarText)
            callback.setAlarm(calendar, item.id)
        }
    }

    @Test fun getCalendarWithAdd() {
        listOf(1, 10, 30, 43).forEach {
            val calendar = Calendar.getInstance().clearSeconds().apply { add(Calendar.MINUTE, it) }
            assertEquals(calendar, interactor.getCalendarWithAdd(it))
        }
    }

    @Test fun checkDateExist() = startCoTest {
        val dateList = List(size = 3) { interactor.getCalendarWithAdd(it).getText() }

        val itemList = MutableList(dateList.size) {
            val id = it.toLong()
            val type = if (Random.nextBoolean()) NoteType.TEXT else NoteType.ROLL

            return@MutableList NotificationItem(
                    Note(id, name = "name_$it", color = it, type = type), Alarm(id, dateList[it])
            )
        }

        val currentCalendar = Calendar.getInstance().clearSeconds()
        val minute = currentCalendar.get(Calendar.MINUTE)

        coEvery { alarmRepo.getList() } returns itemList
        interactor.checkDateExist(currentCalendar)

        /**
         * Get error on last hour minutes.
         */
        val expected = (minute + dateList.size) % 60
        assertEquals(expected, currentCalendar.get(Calendar.MINUTE))

        coVerifySequence {
            alarmRepo.getList()
        }
    }

}